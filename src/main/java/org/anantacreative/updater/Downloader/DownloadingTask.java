package org.anantacreative.updater.Downloader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Задание на загрузку файлов
 */
public class DownloadingTask implements Observer {
    private List<DownloadingItem> downloadItems = new ArrayList<>();

    private TaskCompleteListener completer;
    private Iterator<DownloadingItem> iterator;
    private ExtendedDownloader currentDownloader;
    private boolean reloadAll;
    private   DownloadingItem currentDownloadingItem;

    public DownloadingTask(TaskCompleteListener completer) {
        this.completer = completer;
    }

    public DownloadingTask(Collection<DownloadingItem> downloadItems, TaskCompleteListener completer) {
        this.downloadItems.addAll(downloadItems);
        this.completer = completer;
    }

    private DownloadingTask() {
    }

    public void addItem(DownloadingItem item) {
        downloadItems.add(item);

    }

    public void addItem(URL url, File path) {
        downloadItems.add(new DownloadingItem(url, path));

    }


    public void download(boolean reloadAll) {
        this.reloadAll = reloadAll;
        stop();
        iterator = downloadItems.iterator();
        update(null, null);
    }

    private void stop() {
        if (currentDownloader != null) {
            currentDownloader.deleteObserver(this);
            currentDownloader.cancel();
            currentDownloader = null;
            currentDownloadingItem=null;
        }
    }

    private boolean next() throws Exception {
        if (currentDownloader != null) currentDownloader.deleteObserver(this);
        if (!iterator.hasNext()) return false;
        currentDownloadingItem = iterator.next();
        currentDownloader = new ExtendedDownloader(currentDownloadingItem.getUrl(), currentDownloadingItem.getDstPath(), reloadAll, 0);

        currentDownloader.addObserver(this);
        currentDownloader.startDownload();

        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == null) {
            try {
                if (!next()) completer.complete();
                else completer.nextFileStartDownloading(currentDownloadingItem);
            } catch (Exception e) {
                completer.error(e.getMessage());
            }
            return;
        }

        int status = currentDownloader.getStatus();
        switch (status) {
            case ExtendedDownloader.COMPLETE:

                try {
                    completer.completeFile(currentDownloadingItem);
                    if (!next()) completer.complete();
                    else completer.nextFileStartDownloading(currentDownloadingItem);
                } catch (Exception e) {
                    completer.error(e.getMessage());
                }
                break;
            case ExtendedDownloader.BREAKINGLINK:
            case ExtendedDownloader.ERROR:
                //битая ссылка  или проблемы с доступом
                if (status == ExtendedDownloader.BREAKINGLINK) {
                    completer.error("BREAKING LINK");

                } else completer.error("");
                stop();
                break;
            case ExtendedDownloader.DOWNLOADING:
                completer.currentFileProgress(currentDownloader.getProgress());
                break;
            case ExtendedDownloader.CANCELLED:
                completer.canceled();
                break;
        }
    }


    @Data
    @AllArgsConstructor
    public static class DownloadingItem {
        /**
         * URL закачки файла
         */
        private URL url;
        /**
         * Путь к закачиваемому файлу на диске
         */
        private File dstPath;

    }

    public interface TaskCompleteListener {
        void complete();

        void error(String msg);

        /**
         * Завершена загрузка очередного файла
         * @param item DownloadingItem, который загружен
         */
        void completeFile(DownloadingItem item);

        void currentFileProgress(float progress);

        void canceled();
        /**
         * Завершена загрузка очередного файла
         * @param item DownloadingItem, который загружается
         */
        void nextFileStartDownloading(DownloadingItem item);
    }

    public static boolean checkInternet(URL url) {

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
           return true;

        } catch (IOException ex) {
           return false;

        } finally {

            if (connection != null) connection.disconnect();
        }

    }

}


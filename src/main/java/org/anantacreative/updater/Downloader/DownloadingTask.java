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
        currentDownloader = new ExtendedDownloader(currentDownloadingItem.getUrl(), currentDownloadingItem.getDstPath(), reloadAll);

        currentDownloader.addObserver(this);
        currentDownloader.download();

        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == null) {
            initialHandling();
            return;
        }

        ExtendedDownloader.DownloadingState status = currentDownloader.getState();
        switch (status) {
            case COMPLETE:
                completeStateHandler();
                break;
            case BREAKING_LINK:
                breakingLinkStateHandler();
                break;
            case ERROR:
                errorStateHandler();
                break;
            case DOWNLOADING:
                downloadingStateHandler();
                break;
            case CANCELLED:
                cancelledStateHandler();
                break;
        }
    }

    private void cancelledStateHandler() {
        completer.canceled();
    }

    private void downloadingStateHandler() {
        completer.currentFileProgress(currentDownloader.getProgress());
    }

    private void errorStateHandler() {
        Throwable e = currentDownloader.getException();
        completer.error(e==null?"":e.getMessage());
        stop();
    }

    private void breakingLinkStateHandler() {
        completer.error("BREAKING LINK");
        stop();
    }

    private void completeStateHandler() {
        try {
            completer.completeFile(currentDownloadingItem);
            if (!next()) completer.complete();
            else completer.nextFileStartDownloading(currentDownloadingItem);
        } catch (Exception e) {
            completer.error(e.getMessage());
        }
    }

    /**
     * Вызывается в самом начале, чтобы запустить процесс Observable
     */
    private void initialHandling() {
        try {
            if (!next()) completer.complete();
            else completer.nextFileStartDownloading(currentDownloadingItem);
        } catch (Exception e) {
            completer.error(e.getMessage());
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


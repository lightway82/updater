package org.anantacreative.updater.Downloader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 *Осуществляет закачку файла.
 */
class Downloader {

    private long sizeDownloadingFile=-1; // size of download in bytes
    private long downloaded = 0; // number of bytes downloaded
    private final URL url;
    private final File pathToFile;
    private final boolean reloadFile;
    private final int connectionTimeOut;
    private final int readingTimeOut;
    private final int MAX_BUFFER_SIZE=1024;
    private boolean stop = false;
    private boolean cancel = false;
    private Listener listener;


    private Downloader(URL url, File pathToFile, boolean reload, int connectionTimeOut, int readingTimeOut, Listener listener) {
        this.connectionTimeOut = connectionTimeOut;
        this.readingTimeOut = readingTimeOut;
        this.url =url;
        this.pathToFile=pathToFile;
        reloadFile = reload;
        this.listener = listener;
    }


    /**
     * Остановка закачки
     */
    public void stop(){
        stop =true;
    }

    /**
     * Отмена закачки. Произойдет удаление файла
     */
    public void cancel() {
        cancel =true;
    }

    public  interface Listener{
        void completed();
        void error(Throwable e);
        void breakingLink();
        void progress(float p);
        void stopped();
        void cancelled();
    }

    private boolean isStop() {
        return stop;
    }

    private boolean isCancel() {
        return cancel;
    }

    /**
     * Запускает процесс закачки(докачки) файла
     * @return
     */
    public static  Downloader download(URL url, File pathToFile, boolean reload, int connectionTimeOut, int readingTimeOut, Listener listener){
        if(listener == null) throw new RuntimeException(new NullPointerException("Listener must be not null."));
        Downloader downloader = new Downloader( url,  pathToFile,  reload,  connectionTimeOut,  readingTimeOut, listener);
        download(listener, downloader);
        return downloader;
    }

    private static void download(Listener listener, Downloader downloader) {
        CompletableFuture.runAsync(() -> downloader.run())
                         .thenAccept(a->{
                             if(downloader.isCancel()) listener.cancelled();
                             else  if(downloader.isStop()) listener.stopped();
                             else  listener.completed();
                             downloader.removeListener();
                         })
                         .exceptionally(e->{
                             listener.error(e);
                             downloader.removeListener();
                             return null;
                         });
    }

    private float getProgress() {
        if (sizeDownloadingFile == -1) return 0;
        return ((float) downloaded / sizeDownloadingFile) * 100;
    }

    private void removeListener(){listener=null;}

    private void run() {
        if(!createDirIfNotExists(pathToFile)) throw new RuntimeException("Dir "+pathToFile.getParent()+" not created!");
        RandomAccessFile file = null;
        InputStream stream = null;
        int downloadedPrev = 0; //ранее загруженно
        try {

            HttpURLConnection connection = getConnection(url);

            if (isNeedReloadFile()) {
                downloaded = 0;
                downloadedPrev = 0;
            }else {
                downloadedPrev = (int) pathToFile.length();
                downloaded = downloadedPrev;
            }

            connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
            connection.connect();

            //если файл уже скачан, то вернется 416
            if (connection.getResponseCode() == 416 && !reloadFile) return;
            else if (connection.getResponseCode() / 100 != 2) {
              listener.breakingLink();
              return;
            }

            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                listener.breakingLink();
                return;
            }

            sizeDownloadingFile = contentLength + downloadedPrev;
            listener.progress(getProgress());

            file = new RandomAccessFile(pathToFile, "rw");
            if (downloaded == 0) file.setLength(0);
            file.seek(downloaded);

            stream = connection.getInputStream();
            byte buffer[];


            while (!stop && !cancel) {
                if (sizeDownloadingFile - downloaded > MAX_BUFFER_SIZE) {
                    buffer = new byte[MAX_BUFFER_SIZE];
                } else {
                    if (sizeDownloadingFile - downloaded != 0)
                        buffer = new byte[(int) (sizeDownloadingFile - downloaded)];
                    else break;
                }
                int read = stream.read(buffer);
                if (read == -1) break;
                file.write(buffer, 0, read);
                downloaded += read;
                listener.progress(getProgress());
            }

        } catch (FileNotFoundException e) {
          throw  new RuntimeException(e);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (Exception e) {
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                    if(cancel){
                        pathToFile.delete();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(connectionTimeOut);
        connection.setReadTimeout(readingTimeOut);
        return connection;
    }

    public boolean isNeedReloadFile() {
        return reloadFile;
    }

    private boolean createDirIfNotExists(File filePath) {
        File parentDir = filePath.getParentFile();
        if (!parentDir.exists()) return parentDir.mkdirs();
        else return true;
    }
}

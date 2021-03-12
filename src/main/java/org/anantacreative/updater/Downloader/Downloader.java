package org.anantacreative.updater.Downloader;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

/**
 *Осуществляет закачку файла.
 */
public class Downloader {

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
        CompletableFuture.supplyAsync(() -> downloader.run())
                         .thenAccept(a->{
                             if(a == COMPLETED_BREAKING_LINK) listener.breakingLink();
                             else {
                                 if(downloader.isCancel()) listener.cancelled();
                                 else  if(downloader.isStop()) listener.stopped();
                                 else  listener.completed();
                             }
                             downloader.removeListener();
                         })
                         .exceptionally(e->{
                             listener.error(e);
                             downloader.removeListener();
                             return null;
                         });
    }

    private float calculateProgress() {
        if (sizeDownloadingFile == -1) return 0;
        return ((float) downloaded / sizeDownloadingFile) * 100;
    }

    private void removeListener(){listener=null;}

    private static boolean COMPLETED_NORMAL = true;
    private static boolean COMPLETED_BREAKING_LINK = false;

    private boolean run() {
        if(!createDirIfNotExists(pathToFile)) throw new RuntimeException("Dir "+pathToFile.getParent()+" not created!");

        long downloadedPrev = defineDownloadedPrevSize(); //ранее загруженно
        downloaded = downloadedPrev;
        ConnectionTuple connection = prepareConnection();

        if (isAlsoDownloaded(connection.getStatusCode(), isNeedReloadFile())) return COMPLETED_NORMAL;
        else if (isConnectionSuccessfully(connection.getStatusCode())) return COMPLETED_BREAKING_LINK;

        if (connection.getContentLength() < 1) return COMPLETED_BREAKING_LINK;
        sizeDownloadingFile = connection.getContentLength() + downloadedPrev;
        listener.progress(calculateProgress());
        downloadFile( connection, sizeDownloadingFile, pathToFile);

        return COMPLETED_NORMAL;
    }

    @Data
    @AllArgsConstructor
    private static class ConnectionTuple {
       private HttpURLConnection connection;
       private int statusCode;
       private int contentLength;
    }

    private void downloadFile(ConnectionTuple conn, long sizeDownloadingFile, File pathToFile){
        try( RandomAccessFile file = prepareDstFile( pathToFile,  downloaded); InputStream stream  = prepareDownloadingStream(conn.getConnection())){

            downloadingCycle(file, stream, sizeDownloadingFile);
            if(cancel) pathToFile.delete();
        } catch (FileNotFoundException e) {
            throw  new RuntimeException(e);
        }catch (IOException e) {
            throw  new RuntimeException(e);
        }
        catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    private ConnectionTuple prepareConnection(){
        HttpURLConnection connection;
        int statusCode;
        int contentLength;
        try {
            connection = getConnection(url);
            connection.connect();
            statusCode = connection.getResponseCode();
            contentLength = connection.getContentLength();
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }
        catch (Exception e) {
            throw  new RuntimeException(e);
        }

        return new ConnectionTuple(connection, statusCode, contentLength);
    }

    private InputStream prepareDownloadingStream(HttpURLConnection connection) throws IOException {
        return connection.getInputStream();
    }

    private RandomAccessFile prepareDstFile(File pathToFile, long downloaded) throws IOException {
        RandomAccessFile file = new RandomAccessFile(pathToFile, "rw");
        if (downloaded == 0) file.setLength(0);
        file.seek(downloaded);
        return file;
    }
    private boolean isConnectionSuccessfully(int  statusCode)  {
        return statusCode / 100 != 2;
    }

    private boolean isAlsoDownloaded(int  statusCode, boolean reloadFile) {
        return statusCode == 416 && !reloadFile;//если файл уже скачан, то вернется 416
    }

    private long defineDownloadedPrevSize(){
        if (isNeedReloadFile()) {
           return 0;
        }else {
           return pathToFile.length();
        }
    }

    private void downloadingCycle(RandomAccessFile file, InputStream stream, long sizeDownloadingFile) throws IOException {
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
            listener.progress(calculateProgress());
        }
    }

    private HttpURLConnection getConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(connectionTimeOut);
        connection.setReadTimeout(readingTimeOut);
        connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
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

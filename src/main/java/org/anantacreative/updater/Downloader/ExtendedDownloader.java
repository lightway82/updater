/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anantacreative.updater.Downloader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

import static org.anantacreative.updater.Downloader.ExtendedDownloader.DownloadingStatus.*;

/**
 *
 * Загрузчик файлов с докачкой и прогрессом.
 */
public class ExtendedDownloader  extends Observable implements Runnable 
{
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READING_TIMEOUT = 30000;

    private static final int MAX_BUFFER_SIZE = 1024;

    public enum DownloadingStatus {
        DOWNLOADING,
        PAUSED,
        COMPLETE,
        CANCELLED,
        ERROR,
        BREAKINGLINK,
    }

  private URL url; // download URL
  private int size; // size of download in bytes
  private int downloaded; // number of bytes downloaded
  private DownloadingStatus status; // current status of download
  private File pathToFile;
  private boolean reloadFile;

  
 public Thread thread;

    /**
     *
     * @param url url закачки
     * @param dst файл в который закачивается, не папка, а именно файл.
     * @param reloadFile
     */
  public ExtendedDownloader(URL url, File dst, boolean reloadFile) {
    this.url = url;
    size = -1;
    downloaded = 0;
    status = DOWNLOADING;
    this.pathToFile=dst;
    this.reloadFile =reloadFile;
  }

    /**
     * Путь файлу в который закачиваются данные
     * @return
     */
  public File getFile(){
      return pathToFile;
  }

    /**
     * Стартует закачку
     */
  public void startDownload(){
    download();
  }


    /**
     * Получает URL закачки
     * @return
     */
  public String getUrl() {
    return url.toString();
  }

  // Get this download's size.
  public int getSize() {
    return size;
  }

  // Get this download's progress.
  public float getProgress() {
    return ((float) downloaded / size) * 100;
  }

    /**
     * Текущий статус закачки
     * @return
     */
  public DownloadingStatus getStatus() {
    return status;
  }

    /**
     * Приостоновка закачки
     */
  public void pause() {
    status = PAUSED;
    stateChanged();
  }

    /**
     * Возобновление закачки
     */
  public void resume() {
    status = DOWNLOADING;
    stateChanged();
    download();
  }

    /**
     * Отмена закачки
     */
  public void cancel() {
    status = CANCELLED;
    stateChanged();
  }


  private void setErrorState() {
    status = ERROR;
    stateChanged();
  }

  private void setBreakingLinkState() {
    status = BREAKINGLINK;
    stateChanged();
  }
 
  

  private void download() {
     thread = new Thread(this);
     thread.setName("Downloader="+this.pathToFile.getName());
     if(!createDirIfNotExists(pathToFile)) {
         setErrorState();
         return;
     }
     thread.setDaemon(true);
     thread.start();
  }

 


  public void run() {
    RandomAccessFile file = null;
    InputStream stream = null;
    int downloadedPrev=0; //ранее загруженно
    try {


        HttpURLConnection connection = getConnection();
      if(size==-1)
      {
      //заново создаем объект закачки
            if(status == DOWNLOADING && reloadFile)downloaded=0;
            if(status == DOWNLOADING)
            {
              //определим размер скаченного уже. Работает в случае если прерывали закачку не по паузе.

              downloadedPrev  = (int)pathToFile.length();
              downloaded =downloadedPrev;
            }
      }

      if(reloadFile){
          downloaded=0;
          downloadedPrev=0;
      }

      // Specify what portion of file to download.
      connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

      // Connect to server.
      connection.connect();

        //файл уже скачан, проверка только в режиме докачки

        if (connection.getResponseCode() == 416 && !reloadFile){
                status = COMPLETE;
                stateChanged();
                return;
        }else if (connection.getResponseCode() / 100 != 2)
        {
            this.setBreakingLinkState();
            return;
        }



      // Check for valid content length.
      int contentLength = connection.getContentLength();
      if (contentLength < 1) {
       this.setBreakingLinkState();
        return;
      }

      /* Set the size for this download if it
         hasn't been already set. */
      if (size == -1) {
        size = contentLength+downloadedPrev;
        stateChanged();
      }

      // Open file and seek to the end of it.
      file = new RandomAccessFile(this.pathToFile, "rw");
      if(downloaded==0)file.setLength(0);//усечем файл
      
      
      file.seek(downloaded);

      stream = connection.getInputStream();
      
      // System.out.println("Стартовые : size ="+size+" downloaded ="+downloaded);
      while (status == DOWNLOADING) {
        /* Size buffer according to how much of the
           file is left to download. */
        byte buffer[];
        if (size - downloaded > MAX_BUFFER_SIZE) {
          buffer = new byte[MAX_BUFFER_SIZE];
        } else {
          if(size - downloaded !=0)buffer = new byte[size - downloaded];
          else break;
        }

        // Read from server into buffer.
        int read = stream.read(buffer);
        if (read == -1)
          break;

        // Write buffer to file.
        file.write(buffer, 0, read);
        downloaded += read;
        stateChanged();
      }

      /* Change status to complete if this point was
         reached because downloading has finished. */
      if (status == DOWNLOADING) {
        status = COMPLETE;
        stateChanged();
      }
      
    
      
      
    }catch(FileNotFoundException e)
    {
        e.printStackTrace();
       this.setErrorState();
    }
    catch (Exception e) {
      setErrorState();
     e.printStackTrace();//убрать по окончанию отладки
    
    } finally {
      // Close file.
      if (file != null) {
        try {
          file.close();
        } catch (Exception e) {}
      }

      // Close connection to server.
      if (stream != null) {
        try {
          stream.close();
        } catch (Exception e) {}
      }
    }
  }

    private HttpURLConnection getConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(CONNECTION_TIMEOUT);
        connection.setReadTimeout(READING_TIMEOUT);
        return connection;
    }

    /**
     * Новая закачка
     * @return
     */
  private boolean isReloadFile(){
      return size ==-1;
  }

  // Notify observers that this download's status has changed.
  private void stateChanged() {
    setChanged();
    notifyObservers();
  }



  private boolean createDirIfNotExists(File filePath){
    File parentDir = filePath.getParentFile();
    if(!parentDir.exists()) return parentDir.mkdirs();
    else return true;
  }

}

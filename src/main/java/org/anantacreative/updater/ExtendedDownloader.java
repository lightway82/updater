/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anantacreative.updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 *
 * @author Anama
 */
public class ExtendedDownloader  extends Observable implements Runnable 
{

     // Max size of download buffer.
  private static final int MAX_BUFFER_SIZE = 1024;

  // These are the status names.
  public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error","BreakingLink"};

  // These are the status codes.
  public static final int DOWNLOADING = 0;
  public static final int PAUSED = 1;
  public static final int COMPLETE = 2;
  public static final int CANCELLED = 3;
  public static final int ERROR = 4;
   public static final int BREAKINGLINK = 5;
 

  private URL url; // download URL
  private int size; // size of download in bytes
  private int downloaded; // number of bytes downloaded
  private int status; // current status of download
  private File pathToFile;
  private boolean newDownload;
  private Object param;
  
 public Thread thread;

  // Constructor for Download.
  public ExtendedDownloader(URL url, File pathToFile,boolean newDownload, Object param) {
    this.url = url;
    size = -1;
    downloaded = 0;
    status = DOWNLOADING;
    this.pathToFile=pathToFile;
    this.newDownload=newDownload;
    this.param=param;

  }
  public File getFile(){return pathToFile;}
  public void startDownload(){
// Begin the download.
    download();
  }
public Object getParam(){return param; }
  // Get this download's URL.
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

  // Get this download's status.
  public int getStatus() {
    return status;
  }

  // Pause this download.
  public void pause() {
    status = PAUSED;
    stateChanged();
  }

  // Resume this download.
  public void resume() {
    status = DOWNLOADING;
    stateChanged();
    download();
  }

  // Cancel this download.
  public void cancel() {
    status = CANCELLED;
    stateChanged();
  }

  // Mark this download as having an error.
  private void error() {
    status = ERROR;
    stateChanged();
  }
private void breakinglink() {
    status = BREAKINGLINK;
    stateChanged();
    
  }
 
  
  // Start or resume downloading.
  private void download() {
     thread = new Thread(this);
     thread.setName("Downloader="+this.pathToFile.getName());
     if(!createDirIfNotExists(pathToFile)) {error();return;}
     thread.start();
  }

 

  // Download file.
  public void run() {
    RandomAccessFile file = null;
    InputStream stream = null;
   int downloadedPrev=0; //ранее загруженно 
    try {
      // Open connection to URL.
      HttpURLConnection connection =
        (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
      if(size==-1)
      {
      //заново создаем объект закачки
            if(status == DOWNLOADING && newDownload)downloaded=0;
            if(status == DOWNLOADING)
            {
              //определим размер скаченного уже. Работает в случае если прерывали закачку не по паузе.
               
              downloadedPrev  = (int)pathToFile.length();
              downloaded =downloadedPrev;
            }
      }
      
      // Specify what portion of file to download.
      connection.setRequestProperty("Range", "bytes=" + downloaded + "-");

      // Connect to server.
      connection.connect();

        //файл уже скачан, проверка только в режиме докачки
        if (connection.getResponseCode() == 416  && newDownload==false){
            status = COMPLETE;
            stateChanged();
            return;
        }
        // Make sure response code is in the 200 range.
      if (connection.getResponseCode() / 100 != 2)
      {
       this.breakinglink();
        return;
      }

      // Check for valid content length.
      int contentLength = connection.getContentLength();
      if (contentLength < 1) {
       this.breakinglink();
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
       this.breakinglink();
    }
    catch (Exception e) {
      error();
      //  System.out.println("size - downloaded ="+(size - downloaded ));
       // System.out.println("Текцщие: size ="+size+" downloaded ="+downloaded);
       System.out.println("ERROR DETECTED");
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

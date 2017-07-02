/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anantacreative.updater.Downloader;

import java.io.File;
import java.net.URL;
import java.util.Observable;

import static org.anantacreative.updater.Downloader.ExtendedDownloader.DownloadingState.*;

/**
 * Загрузчик файлов с докачкой и прогрессом.
 */
public class ExtendedDownloader extends Observable {
    private static final int CONNECTION_TIMEOUT = 10000;
    private static final int READING_TIMEOUT = 30000;
    private DownloadingState state; // current state of download

    public enum DownloadingState {
        DOWNLOADING,
        PAUSED,
        COMPLETE,
        CANCELLED,
        ERROR,
        BREAKING_LINK,
        INITIAL

    }

    private URL url;
    private File pathToFile;
    private boolean reloadFile;
    private float progress;
    private  Downloader downloader;
    private Throwable exception;

    /**
     * @param url        url закачки
     * @param dst        файл в который закачивается, не папка, а именно файл.
     * @param reloadFile загрузка файла с нуля
     */
    public ExtendedDownloader(URL url, File dst, boolean reloadFile) {
        this.url = url;
        pathToFile = dst;
        this.reloadFile = reloadFile;
        state=INITIAL;
    }


    /**
     * Путь файлу в который закачиваются данные
     *
     * @return
     */
    public File getFile() {
        return pathToFile;
    }

    /**
     * Получает URL закачки
     *
     * @return
     */
    public String getUrl() {
        return url.toString();
    }

    /**
     * Прогресс закачки в процентах
     * @return
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Текущий статус закачки
     *
     * @return
     */
    public DownloadingState getState() {
        return state;
    }

    /**
     * Приостановка закачки
     */
    public void pause() {
        if(downloader!=null) downloader.stop();
    }

    /**
     * Отмена закачки, файл удалится
     */
    public void cancel() {
        if(downloader!=null) downloader.cancel();
    }



    public Throwable getException() {
        return exception;
    }

    /**
     * Начало закачки
     */
    public void download(){
        process(reloadFile);
    }

    /**
     * Возврат к закачке
     */
    public void resume(){
        process(false);
    }
    /**
     * Возобновление закачки
     */
    public void process(boolean reloadFile) {
        if (getState() == DOWNLOADING) return;
         downloader = Downloader.download(url, pathToFile, reloadFile, CONNECTION_TIMEOUT, READING_TIMEOUT,
                new Downloader.Listener() {
                    @Override
                    public void completed() {
                        setCompletedState();
                    }

                    @Override
                    public void error(Throwable e) {
                        exception = e;
                        setErrorState();
                    }

                    @Override
                    public void breakingLink() {
                        setBreakingLinkState();
                    }

                    @Override
                    public void progress(float p) {
                        progress = p;
                        stateChanged();
                    }

                    @Override
                    public void stopped() {
                        setPausedState();
                    }

                    @Override
                    public void cancelled() {
                       setCancelledState();
                    }
                });

        setDownloadingState();

    }



    private void setDownloadingState() {
        state = DOWNLOADING;
        stateChanged();
    }

    private void setErrorState() {
        state = ERROR;
        stateChanged();
    }

    private void setBreakingLinkState() {
        state = BREAKING_LINK;
        stateChanged();
    }

    private void setCompletedState(){
        state = COMPLETE;
        stateChanged();
    }

    private void setCancelledState(){
        state = CANCELLED;
        stateChanged();
    }
    private void setPausedState(){
        state = PAUSED;
        stateChanged();
    }

    private void stateChanged() {
        setChanged();
        notifyObservers();
    }



}

package org.anantacreative.updater.Downloader;

import java.io.File;

/**
 * Абстрактный класс для реализации упрощенного слушателя событий загрузки.
 */
public abstract class SimpleDownloadTaskListener implements DownloadingTask.TaskCompleteListener {


    @Override
    public void completeFile(String url, File path) {

    }

    @Override
    public void currentFileProgress(float progress) {

    }

    @Override
    public void canceled() {

    }

    @Override
    public void nextFileStartDownloading(String url, File path) {

    }
}

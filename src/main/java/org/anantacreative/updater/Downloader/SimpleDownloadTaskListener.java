package org.anantacreative.updater.Downloader;

/**
 * Абстрактный класс для реализации упрощенного слушателя событий загрузки.
 */
public abstract class SimpleDownloadTaskListener implements DownloadingTask.TaskCompleteListener {


    @Override
    public void completeFile(DownloadingTask.DownloadingItem item) {

    }

    @Override
    public void currentFileProgress(float progress) {

    }

    @Override
    public void canceled() {

    }

    @Override
    public void nextFileStartDownloading(DownloadingTask.DownloadingItem item) {

    }
}

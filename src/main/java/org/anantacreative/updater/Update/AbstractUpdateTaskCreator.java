package org.anantacreative.updater.Update;

import org.anantacreative.updater.Downloader.DownloadingTask;

import java.io.File;
import java.net.URL;
import java.util.List;

/**
 * Закачивает файлы для обновления и создает UpdateTask для проведения процедуры обновления.
 * Функции получения списка файлов и построения UpdateTask возлагаются на подклассы
 */
public abstract class AbstractUpdateTaskCreator {

    private File downloadsDir;
    private Listener listener;
    private File rootDirApp;


    public File getDownloadsDir() {
        return downloadsDir;
    }

    private AbstractUpdateTaskCreator() {
    }

    public File getRootDirApp() {
        return rootDirApp;
    }

    /**
     * @param downloadsDir папка загрузки файлов
     * @param listener     слушатель событий создания UpdateTask
     * @param rootDirApp   корневая директория приложения. От нее устанавливаются dst пути файлов и экшенов
     */
    public AbstractUpdateTaskCreator(File downloadsDir, Listener listener, File rootDirApp) {
        this.downloadsDir = downloadsDir;
        this.listener = listener;
        this.rootDirApp = rootDirApp;
        if(!downloadsDir.exists()) downloadsDir.mkdirs();
    }

    private int currentProgress;
    private int downloadingSize;


    private void initProgress(int downloadingSize){
        this.downloadingSize=downloadingSize;
        currentProgress=0;
    }

    private void incProgress(){
        currentProgress++;
        if(currentProgress>downloadingSize)currentProgress=downloadingSize;
    }

    private float getProgress(){
        if(downloadingSize==0) return 0;
        return 100*(float)currentProgress/(float)downloadingSize;
    }



    /**
     * Основной метод для создания UpdateTask. Задание по созданию UpdateTask считается выполненым если получен файл со списком команд,
     * создан UpdateTask и закачены необходимые файлы
     *
     * @param reload
     * @throws CreateUpdateTaskError
     */
    public void createTask(boolean reload) throws CreateUpdateTaskError {

        try {
            List<DownloadingTask.DownloadingItem> downloadingFiles = getDownloadingFiles();
            initProgress(downloadingFiles.size());
            DownloadingTask dt = new DownloadingTask(downloadingFiles, new DownloadingTask.TaskCompleteListener() {
                @Override
                public void complete() {
                    try {
                        listener.taskCompleted(buildUpdateTask(downloadsDir));
                    } catch (CreateUpdateTaskError e) {
                        listener.error(e);
                    }
                }

                @Override
                public void error(String msg) {
                    listener.error(new CreateUpdateTaskError(new DownloadUpdateFilesError(msg)));
                }

                @Override
                public void completeFile(String url, File path) {
                    incProgress();
                    listener.totalProgress(getProgress());
                    listener.completeFile(url,path);
                }

                @Override
                public void currentFileProgress(float progress) {
                    listener.currentFileProgress(progress);
                }

                @Override
                public void canceled() {}

                @Override
                public void nextFileStartDownloading(String url, File path) {
                    listener.nextFileStartDownloading(url,path);
                }
            });
            dt.download(reload);
        } catch (GetUpdateFilesError e) {
            throw new CreateUpdateTaskError(e);
        } catch (Exception e) {
            throw new CreateUpdateTaskError(e);
        }

    }


    /**
     * Необходимо заполнить список на закачку файлов.
     *
     * @return
     * @throws GetUpdateFilesError все ошибки нужно привести к GetUpdateFilesError
     */
    public abstract List<DownloadingTask.DownloadingItem> getDownloadingFiles() throws GetUpdateFilesError;


    /**
     * Построение задания на обновление. Вызывается автоматически после загрузки всех файлов
     *
     * @param downloadsDir директория загрузки. Определяется при конструировании класса
     * @return
     * @throws CreateUpdateTaskError все ошибки нужно привести к CreateUpdateTaskError
     */
    public abstract UpdateTask buildUpdateTask(File downloadsDir) throws CreateUpdateTaskError;


    public String extractFileNameFromUrl(URL url){
        String[] split = url.getFile().split("/");
        return split[split.length-1];
    }
    /**
     * Ошибка плучения списка файлов для закачки
     */
    public static class GetUpdateFilesError extends Exception {
        public GetUpdateFilesError() {
        }

        public GetUpdateFilesError(String message) {
            super(message);
        }

        public GetUpdateFilesError(String message, Throwable cause) {
            super(message, cause);
        }

        public GetUpdateFilesError(Throwable cause) {
            super(cause);
        }

        public GetUpdateFilesError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }

    /**
     * Ошибка закачки файлов обновления
     */
    public static class DownloadUpdateFilesError extends Exception {
        public DownloadUpdateFilesError() {
        }

        public DownloadUpdateFilesError(String message) {
            super(message);
        }

        public DownloadUpdateFilesError(String message, Throwable cause) {
            super(message, cause);
        }

        public DownloadUpdateFilesError(Throwable cause) {
            super(cause);
        }

        public DownloadUpdateFilesError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    /**
     * Ошибка создания таски
     */
    public static class CreateUpdateTaskError extends Exception {
        public CreateUpdateTaskError() {
        }

        public CreateUpdateTaskError(String message) {
            super(message);
        }

        public CreateUpdateTaskError(String message, Throwable cause) {
            super(message, cause);
        }

        public CreateUpdateTaskError(Throwable cause) {
            super(cause);
        }

        public CreateUpdateTaskError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }


    public interface Listener {
         void taskCompleted(UpdateTask ut);
         void error(Exception e);
         void completeFile(String url, File path);
         void currentFileProgress(float progress);
         void nextFileStartDownloading(String url, File path);
         void totalProgress(float progress);


    }
}

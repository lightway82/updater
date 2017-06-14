package org.anantacreative.updater.Update;

import org.anantacreative.updater.Downloader.DownloadingTask;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Закачивает файлы для обновления и создает UpdateTask для проведения процедуры обновления.
 * Функции получения списка файлов и построения UpdateTask возлагаются на подклассы
 */
public abstract class AbstractUpdateTaskCreator {

    private File downloadsDir;
    private Listener listener;
    private File rootDirApp;
    private Map<DownloadingTask.DownloadingItem, List<UpdateActionFileItem>> itemsMap=new HashMap<>();
    private UpdateTask task;

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
        if(downloadsDir.getPath().isEmpty()) this.downloadsDir = new File(".");
        else this.downloadsDir = downloadsDir;
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



    private Map<DownloadingTask.DownloadingItem,List<UpdateActionFileItem>> updateActionFileItemToDownloadingItem(List<UpdateActionFileItem> downloadingFiles){
        itemsMap.clear();
        for (UpdateActionFileItem f : downloadingFiles) {
            DownloadingTask.DownloadingItem downloadingItem = new DownloadingTask.DownloadingItem(f.getUrl(),
                    new File(getDownloadsDir(), extractFileNameFromUrl(f.getUrl())));
            if(itemsMap.containsKey(downloadingItem))itemsMap.get(downloadingItem).add(f);
            else itemsMap.put(downloadingItem, new ArrayList<>(Arrays.asList(f)));

        }
        return itemsMap;
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

            task = buildUpdateTask(downloadsDir);
            if(task==null) throw new Exception("UpdateTask has not created.");

            List<UpdateActionFileItem> downloadingFiles = task.getDownloadingFilesItem();

            initProgress(downloadingFiles.size());


            DownloadingTask dt = new DownloadingTask(updateActionFileItemToDownloadingItem(downloadingFiles).keySet(),
                    new DownloadingTask.TaskCompleteListener() {
                @Override
                public void complete() {
                        listener.taskCompleted(task, getRootDirApp(), getDownloadsDir());
                }

                @Override
                public void error(String msg) {
                    listener.error(new CreateUpdateTaskError(new DownloadUpdateFilesError(msg)));
                }

                @Override
                public void completeFile(DownloadingTask.DownloadingItem item) {
                    incProgress();
                    itemsMap.get(item).forEach(i->i.setDownloadedFile(item.getDstPath()));
                    listener.totalProgress(getProgress());
                    listener.completeFile(item.getUrl().toString(),item.getDstPath());
                }

                @Override
                public void currentFileProgress(float progress) {
                    listener.currentFileProgress(progress);
                }

                @Override
                public void canceled() {}

                @Override
                public void nextFileStartDownloading(DownloadingTask.DownloadingItem item) {
                    listener.nextFileStartDownloading(item.getUrl().toString(),item.getDstPath());
                }
            });
            dt.download(reload);
            listener.totalProgress(0);
        }catch (Exception e) {
            throw new CreateUpdateTaskError(e);
        }

    }


    /**
     * Построение задания на обновление. Вызывается автоматически после загрузки всех файлов
     *
     * @param downloadsDir директория загрузки. Определяется при конструировании класса
     * @return
     * @throws CreateUpdateTaskError все ошибки нужно привести к CreateUpdateTaskError
     */
    protected abstract UpdateTask buildUpdateTask(File downloadsDir) throws CreateUpdateTaskError;


    public String extractFileNameFromUrl(URL url){
        String[] split = url.getFile().split("/");
        return split[split.length-1];
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
         void taskCompleted(UpdateTask ut, File rootDirApp, File downloadDir);
         void error(Exception e);
         void completeFile(String url, File path);
         void currentFileProgress(float progress);
         void nextFileStartDownloading(String url, File path);
         void totalProgress(float progress);


    }
}

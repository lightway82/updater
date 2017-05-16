package org.anantacreative.updater.Update;

import org.anantacreative.updater.Downloader.DownloadingTask;
import org.anantacreative.updater.Downloader.SimpleDownloadTaskListener;

import java.io.File;
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
            DownloadingTask dt = new DownloadingTask(getDownloadingFiles(), new SimpleDownloadTaskListener() {
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
    }
}

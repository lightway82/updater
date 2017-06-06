package org.anantacreative.updater.Update;

import java.io.File;
import java.net.URL;

/**
 * Представляет элемент файла для Action. Может представлять файл src на диске или на сервере через URL и dst путь для результата операции.
 * Конкретное назначение праметров определется конкретным Action
 */
public class UpdateActionFileItem {
    private File srcPath;
    private File dstPath;
    private URL url;
    private File downloadedFile;

    private UpdateActionFileItem(File srcPath, File dstPath, URL url) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
        this.url = url;
    }

    /**
     * Путь к загруженному файлу или null если файл не загружен или не загружался
     * @return
     */
    public File getDownloadedFile() {
        return downloadedFile;
    }

    /**
     * Устанавливает путь на диске к загруженному файлу, который был указан в URL
     * При создания собственной стратегии обновления, после загрузки файла необходимо использовать этот метод для указания расположения загруженного файла
     * @param downloadedFile
     */
    public void setDownloadedFile(File downloadedFile) {
        this.downloadedFile = downloadedFile;
    }

    public File getSrcPath() {
        return srcPath;
    }

    public File getDstPath() {
        return dstPath;
    }

    public URL getUrl() {
        return url;
    }

    private UpdateActionFileItem() {
    }

    public static Builder create() {
        return new Builder();
    }

    public static class Builder {
        private File srcPath;
        private File dstPath;
        private URL url;

        /**
         * Устанавливает путь к исходным данным src. Путь указываетя от корневой директории приложения.
         * Корневая директория приложения указывается  при создании UpdateTaskCreator
         * @param src путь от корневой директории приложения
         * @param rootDirApp корневая директория приложения
         * @return
         */
        public Builder setSrcPath(String src, File rootDirApp) {
            srcPath = new File(rootDirApp, src.replace("./",""));
            return this;
        }
        /**
         * Устанавливает путь к исходным данным dst. Путь указываетя от корневой директории приложения.
         * Корневая директория приложения указывается  при создании UpdateTaskCreator
         * @param dst путь от корневой директории приложения
         * @param rootDirApp корневая директория приложения
         * @return
         */
        public Builder setDstPath(String dst, File rootDirApp) {
            dstPath = new File(rootDirApp, dst.replace("./",""));
            return this;
        }

        /**
         * URL с которого будет загружаться файл, перед проведением Action
         * @param url
         * @return
         */
        public Builder setURL(URL url) {
            this.url = url;
            return this;
        }

        public UpdateActionFileItem build() {
            return new UpdateActionFileItem(srcPath, dstPath, url);
        }
    }



    @Override
    public String toString() {
        return "UpdateActionFileItem{" +
                "srcPath=" + srcPath +
                ", dstPath=" + dstPath +
                ", url=" + url +
                '}';
    }
}

package org.anantacreative.updater.Update;

import java.io.File;
import java.net.URL;


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

        public Builder setSrcPath(File src) {
            srcPath = src;
            return this;
        }

        public Builder setDstPath(File dst) {
            dstPath = dst;
            return this;
        }

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

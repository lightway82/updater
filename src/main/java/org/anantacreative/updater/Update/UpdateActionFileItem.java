package org.anantacreative.updater.Update;

import java.io.File;
import java.net.URL;


public class UpdateActionFileItem {
    private File srcPath;
    private File dstPath;
    private URL url;

    private UpdateActionFileItem(File srcPath, File dstPath, URL url) {
        this.srcPath = srcPath;
        this.dstPath = dstPath;
        this.url = url;
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
}

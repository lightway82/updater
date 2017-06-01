package org.anantacreative.updater.Pack;


import org.anantacreative.updater.Pack.Exceptions.PackException;
import org.anantacreative.updater.Pack.Utils.ZipUtil;

import java.io.File;
import java.util.List;

public class Packer {
    /**
     * Упаковывает директорию в архив
     * @param dir  директория для упаковки
     * @param toArch  конечноый архив
     * @throws PackException
     */
    static void packDir(File dir, File toArch) throws PackException {
        ZipUtil.zipDir(dir, toArch);
    }

    /**
     * Упаковывает файлы в архив
     * @param files  список файлов
     * @param toArch  конечноый архив
     */
    static void packFiles(List<File> files, File toArch) throws PackException {
        ZipUtil.zipFiles(files,toArch);
    }
}

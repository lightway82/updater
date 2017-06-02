package org.anantacreative.updater.Pack;


import org.anantacreative.updater.Pack.Exceptions.PackException;
import org.anantacreative.updater.Pack.Utils.ZipUtil;

import java.io.File;
import java.util.List;

public class Packer {
    /**
     * Упаковывает директорию в архив(файлы и директории указанной директории)
     * @param dir  директория для упаковки
     * @param toArch  конечноый архив
     * @throws PackException
     */
   public static void packDir(File dir, File toArch) throws PackException {
        ZipUtil.zipDir(dir, toArch);
    }

    /**
     * Упаковывает файлы в архив
     * @param files  список файлов
     * @param toArch  конечноый архив
     */
    public static void packFiles(List<File> files, File toArch) throws PackException {
        ZipUtil.zipFiles(files,toArch);
    }
}

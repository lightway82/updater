package org.anantacreative.updater;

import java.io.File;

/**
 *
 */
public class FilesUtil {


    /**
     * Рекурсивное удаление содержимого директории
     * @param diskPath
     * @return false в случае ошибки
     */
    public static boolean recursiveDelete(File diskPath) {

        return recursiveDeleteHelper(diskPath);

    }


    private static boolean recursiveDeleteHelper(File path)
    {

        // до конца рекурсивного цикла
        if (!path.exists())
            return false;

        //если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление всего, что там есть
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                // рекурсивный вызов
                recursiveDeleteHelper(f);
            }
        }
        // вызываем метод delete() для удаления файлов и пустых(!) папок
        return path.delete();
    }




}

package org.anantacreative.updater;

import java.io.File;

/**
 *
 */
public class FilesUtil {


    /**
     * Рекурсивное удаление  директории
     * @param diskPath
     * @return false в случае ошибки
     */
    public static boolean recursiveDelete(File diskPath) {

        return recursiveDeleteHelper(diskPath);

    }
    /**
     * Рекурсивная очистка  директории
     * @param diskPath
     * @return false в случае ошибки
     */
    public static boolean recursiveClear(File diskPath) {

        // до конца рекурсивного цикла
        if (!diskPath.exists())
            return false;

        //если это папка, то идем внутрь этой папки и вызываем рекурсивное удаление всего, что там есть
        if (diskPath.isDirectory()) {
            for (File f : diskPath.listFiles()) {
                // рекурсивный вызов
                if(!recursiveDeleteHelper(f)) return false;
            }
        }
        return true;

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

    /**
     * Проверка пути - файл. Если путь отсутствует, то в имени проверяется наличик точек, которые мы считаем признаком файла в путях
     * @param path
     * @return
     */
    public static boolean isFile(File path){
        if(path.exists()) return path.isFile();
        else   return checkNameFileForDots(path.getName());
    }

    /**
     * Проверка пути -  директория. Если путь отсутствует, то в имени проверяется наличик точек, которые мы считаем признаком файла в путях
     * @param path
     * @return
     */
    public static boolean isDirectory(File path){
        if(path.exists()) return path.isDirectory();
        else   return !checkNameFileForDots(path.getName());
    }

    /**
     * Проверка имени на наличие точке в названии.
     * @param name
     * @return
     */
    private static boolean checkNameFileForDots(String name){
        return name.contains(".");
    }


}

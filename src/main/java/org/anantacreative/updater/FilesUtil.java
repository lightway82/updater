package org.anantacreative.updater;

import java.io.File;
import java.util.List;

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


    /**
     * Удаление списка файлов
     * @param files
     * @throws Exception
     */
    public static void deleteFiles(List<File> files) throws Exception {
        for (File file : files) {
            if(!file.exists()) continue;
            if(!file.delete()) throw new Exception("Delete file error "+file.getAbsolutePath() );
        }

    }

    /**
     * Коприровать файлы в папку
     * @param srcFiles список файлов для копирования
     * @param toDir директория в которую будут копироваться файлы
     */
    public static void copyFiles(List<File> srcFiles,File toDir){

    }

    /**
     * Копировать папку в папку.
     * @param srcDir директория которая будет копироваться
     * @param toDir директория в которую скопируется srcDir
     */
    public static void copyDir(File srcDir, File toDir){

    }


}

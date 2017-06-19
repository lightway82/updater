package org.anantacreative.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
     * Коприровать файлы в директорию. Конечная дректория должна существовать
     * @param srcFiles список файлов для копирования
     * @param toDir директория в которую будут копироваться файлы
     */
    public static void copyFilesToDir(List<File> srcFiles, File toDir) throws IOException {
        for (File srcFile : srcFiles) {
            copyFileToDir(srcFile,toDir);
        }
    }

    /**
     * Копировать директорию в директорию. Конечная дректория должна существовать
     * @param srcDir директория которая будет копироваться
     * @param toDir директория в которую скопируется srcDir
     */
    public static void copyDirToDir(File srcDir, File toDir) throws IOException {
        File dstDir=new File(toDir,srcDir.getName());
        List<File> srcFiles = Stream.of(srcDir.listFiles()).collect(Collectors.toList());
        dstDir.mkdirs();
        copyFilesToDir(srcFiles,dstDir);
    }


    /**
     * Коприровать файл в директорию. Конечная дректория должна существовать
     * @param srcFile  файл для копирования
     * @param toDir директория в которую будут копироваться файлы
     */
    public static void copyFileToDir(File srcFile, File toDir) throws IOException {
        File toDstFile =new File(toDir,srcFile.getName());
        copyFileToFile(srcFile,toDstFile);
    }

    /**
     * Коприровать файл в файл. Конечная дректория должна существовать
     * @param srcFile  файл для копирования
     * @param toDstFile файл в который будет копирование
     */
    public static void copyFileToFile(File srcFile, File toDstFile) throws IOException {
        Files.copy(srcFile.toPath(),toDstFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * Удаляет пробелы из строки
     * @param src исходная строка
     * @return
     */
    public static String replaceAllSpaces(String src){
        return src.replaceAll("\\s+","");
    }

    /**
     * Заменяет все обратные слэши на прямые и устраняет их дубликаты( несколько бекслэшей аменится на один прямой)
     * @param src исходная строка
     * @return строка после замены
     */
    public static String replaceAllBackSlashes(String src){
        return src.replaceAll("\\\\+","/");
    }

    /**
     * Заменяет дибликаты прямых слэшей на один слэш
     * @param src
     * @return
     */
    public static String replaceDuplicatedSlashes(String src){
        return src.replaceAll("/+","/");
    }



    /*
    public static String getRelativePathAfromB(String a,String b){
        a = replaceAllBackSlashes(a);
        a = replaceAllSpaces(a).replace("./","");
        b = replaceAllBackSlashes(b);
        b = replaceAllSpaces(b).replace("./","");

        File f=new File("");
        f.toURI().re

    }
    */

    public static String extractRelativePathFrom(File commonPath, File forExtract){
        return commonPath.toURI().relativize(forExtract.toURI()).getPath();

    }

}

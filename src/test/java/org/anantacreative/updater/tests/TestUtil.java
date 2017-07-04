package org.anantacreative.updater.tests;

import org.anantacreative.updater.Utilites.FilesUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;


public class TestUtil {

    private static final String TEST_TEXT = "Big data is a term for data sets that are so large or complex that traditional data processing application software is inadequate to deal with them.";

    /**
     * Подготавливает тестовою директорию
     *
     * @param dir
     * @throws Exception
     */
    public static void initTestDir(File dir) throws Exception {
        if (!dir.exists()) {
            if (!dir.mkdir()) throw new Exception();
        } else assertTrue(FilesUtil.recursiveClear(dir));
    }

    /**
     * Подготавливает тестовою директорию
     *
     * @param dirName текстовый путь
     * @throws Exception
     */
    public static File initTestDir(String dirName) throws Exception {
        File dir = new File(dirName);
        if (!dir.exists()) {
            if (!dir.mkdir()) throw new Exception();
        } else assertTrue(FilesUtil.recursiveClear(dir));
        return dir;
    }

    /**
     * Инициализирует директорию файлами с указанными частями имени. Если директории не существует, то она будет создана
     * Имя файлов будет выглядеть, как префикс1.ext
     *
     * @param dir        директория
     * @param countFiles количество файлов
     * @param filePrefix начало имени файлов
     * @param ext        расширения
     * @return список созданных файлов
     * @throws Exception
     */
    public static List<File> initDirWithFiles(File dir, int countFiles, String filePrefix, String ext) throws Exception {

        List<String> files = new ArrayList<>();

        for (int i = 1; i < countFiles + 1; i++) files.add(filePrefix + i + "." + ext);
        return initDirectory(dir, files);


    }

    /**
     * Инициализирует директорию файлами с указанными именами. Если директории не существует, то она будет создана
     *
     * @param dir       директория
     * @param fileNames список имен файлов
     * @return список созданных файлов
     * @throws Exception
     */
    public static List<File> initDirectory(File dir, List<String> fileNames) throws Exception {

        if (!dir.exists()) assertTrue(dir.mkdir());
        else assertTrue(FilesUtil.recursiveClear(dir));

        List<File> files = new ArrayList<>();

        for (String file : fileNames) {
            File f = new File(dir, file);
            files.add(f);
            if (!f.exists()) {
                if (!f.createNewFile()) fail("Test file not created");
                writeTestTextToFile(f, TEST_TEXT, 100);
            }
            assertTrue("Test file not exists", f.exists());
        }
        return files;
    }

    /**
     * Создает файл со случайным текстовым содержимым
     *
     * @param file файл
     * @return результирующий(указанный в парметрах) файл
     * @throws IOException
     */
    public static File createFile(File file) throws IOException {
        Files.createFile(file.toPath());
        writeTestTextToFile(file, TEST_TEXT, 100);
        return file;
    }

    /**
     * Записывает в файл случайный тест(случайное количество строк templateLine в пределах указанного  1 - maxStrings) получаются файлы разных размеров. Строка предопределена этим классом
     *
     * @param f            текстовый файл для записи
     * @param templateLine шаблон строки
     * @param maxStrings   максимальное количество строк, которое может получиться при записи
     * @throws IOException
     */
    public static void writeTestTextToFile(File f, String templateLine, int maxStrings) throws IOException {
        try (FileWriter writer = new FileWriter(f, false)) {
            Random rnd = new Random();
            int num = rnd.nextInt(maxStrings);
            if(num==0) num++;
            for (int i = 0; i < num; i++) writer.write(templateLine);
            writer.flush();
        }

    }

    /**
     * Проверка существования директорий в директории. проверяется точное совпадение. Те не больше и не меньше директорий по списку.
     * Если директорий меньше или больше указанных или их имена отличаютися, то будет исключение
     *
     * @param dir  проверяемая директория
     * @param dirs список имен директорий для проверки
     * @return проверенные директории в директории проверки
     */
    public static File[] hasDirsInDirExectly(File dir, List<String> dirs) {
        File[] dirsForTest = dir.listFiles(File::isDirectory);
        assertTrue("Количество директорий не соответсвует списку", dirsForTest.length == dirs.size());
        int matchDirNameCounter = 0;
        for (File d : dirsForTest) {
            assertTrue("Проверяемая директория " + d.getName() + " отсутствует в проверочном списке",
                    dirs.contains(d.getName()));
            matchDirNameCounter++;
        }

        assertTrue("Количество проверяемых директорий не совпало со списком проверки",
                matchDirNameCounter == dirs.size());

        return dirsForTest;
    }

    /**
     * Проверка существования директорий в директории.  Проверяется только наличие директорий в списке, остальные игнорируются
     *
     * @param dir  проверяемая директория
     * @param dirs список имен директорий для проверки
     * @return проверенные директории в директории проверки
     */
    public static File[] hasDirsInDir(File dir, List<String> dirs) {
        File[] dirsForTest = dir.listFiles(File::isDirectory);
        assertTrue("Количество директорий не соответсвует списку",dirsForTest.length == dirs.size());

        for (File d : dirsForTest) {
            assertTrue("Проверяемая директория " + d.getName() + " отсутствует в проверочном списке",
                    dirs.contains(d.getName()));

        }
        return dirsForTest;
    }

    /**
     * Проверка существования файлов в директории.  Проверяется только наличие файлов в списке, остальные игнорируются
     *
     * @param dir   проверяемая директория
     * @param files список имен файлов для проверки
     * @param checkEmpty если файл пустой будет исключение. Если параметр false, то просто проверяется наличие файлов
     * @return проверенные файлы в директории проверки
     */
    public static File[] hasFilesInDir(File dir, List<String> files, boolean checkEmpty) {
        File[] filesForTest = dir.listFiles(File::isFile);
        assertTrue("Количество файлов не соответсвует списку",filesForTest.length == files.size());

        for (File f : filesForTest) {
            assertTrue("Проверяемый файл " + f.getName() + " отсутствует в проверочном списке", files.contains(f.getName()));
            if(checkEmpty) assertTrue("Файл " + f.getName() +" пустой",f.length()>0);
        }
        return filesForTest;
    }

    /**
     * Проверка существования файлов в директории. проверяется точное совпадение. Те не больше и не меньше файлов по списку.
     * Если файлов меньше или больше указанных или их имена отличаютися, то будет исключение
     *
     * @param dir   проверяемая директория
     * @param files список имен файлов для проверки
     * @param checkEmpty если файл пустой будет исключение. Если параметр false, то просто проверяется наличие файлов
     * @return проверенные файлы в директории проверки
     */
    public static File[] hasFilesInDirExectly(File dir, List<String> files, boolean checkEmpty) {
        File[] filesForTest = dir.listFiles(File::isFile);
        assertTrue("Количество файлов не соответсвует списку",filesForTest.length == files.size());
        int matchFileNameCounter = 0;
        for (File f : filesForTest) {
            assertTrue("Проверяемый файл " + f.getName() + " отсутствует в проверочном списке", files.contains(f.getName()));
            if(checkEmpty) assertTrue("Файл " + f.getName() +" пустой",f.length()>0);
            matchFileNameCounter++;
        }

        assertTrue("Количество проверяемых файлов не совпало со списком проверки",
                matchFileNameCounter == files.size());
        return filesForTest;
    }
}

package org.anantacreative.updater.tests;

import org.anantacreative.updater.FilesUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;


public class TestUtils {

    private static final String TEST_TEXT = "Big data is a term for data sets that are so large or complex that traditional data processing application software is inadequate to deal with them.";

    /**
     * Подготавливает тестовою директорию
     * @param dir
     * @throws Exception
     */
    public static void initTestDir(File dir) throws Exception {
        if(!dir.exists()) {
            if(!dir.mkdir()) throw new Exception();
        }else assertTrue(FilesUtil.recursiveClear(dir));
    }


    /**
     * Инициализирует директорию файлами с указанными частями имени. Если директории не существует, то она будет создана
     * Имя файлов будет выглядеть, как префикс1.ext
     * @param dir директория
     * @param countFiles количество файлов
     * @param filePrefix начало имени файлов
     * @param ext расширения
     * @return список созданных файлов
     * @throws Exception
     */
    public static  List<File>  initDirWithFiles(File dir, int countFiles, String filePrefix,String ext) throws Exception {

        List<String> files=new ArrayList<>();

        for(int i=1;i<countFiles+1; i++) files.add(filePrefix+i+"."+ext);
        return initDirectory(dir,files);


    }

    /**
     * Инициализирует директорию файлами с указанными именами. Если директории не существует, то она будет создана
     * @param dir директория
     * @param fileNames список имен файлов
     * @return  список созданных файлов
     * @throws Exception
     */
    public static List<File> initDirectory(File dir, List<String> fileNames) throws Exception {

        if(!dir.exists()) assertTrue(dir.mkdir());
        else assertTrue(FilesUtil.recursiveClear(dir));

        List<File> files =new ArrayList<>();

        for (String file : fileNames) {
            File f = new File(dir,file);
            files.add(f);
            if(!f.exists()){
                if(!f.createNewFile()) fail("Test file not created");
                writeTestTextToFile(f);
            }
            assertTrue("Test file not exists",f.exists());
        }
        return files;
    }

    private static void writeTestTextToFile(File f) throws IOException {
        try(FileWriter writer = new FileWriter(f, false))
        {
           int maxStrings = 100;
            Random rnd = new Random();
            rnd.nextInt(maxStrings);
            for(int i=0;i < maxStrings; i++) writer.write(TEST_TEXT);
            writer.flush();
        }

    }
}

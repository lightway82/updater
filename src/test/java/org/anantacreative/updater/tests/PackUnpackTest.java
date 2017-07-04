package org.anantacreative.updater.tests;


import org.anantacreative.updater.Pack.Packer;
import org.anantacreative.updater.Pack.UnPacker;
import org.anantacreative.updater.Utilites.ResourceUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.testng.AssertJUnit.assertTrue;

public class PackUnpackTest {


    @Test(dependsOnMethods = {"unPack"},groups = {"common"})
    public void packDir() throws Exception {

        File testDir = TestUtil.initTestDir("./tmp");
        File dir = new File(testDir, "dst");
        List<File> files = TestUtil.initDirWithFiles(dir, 10, "file", "txt");
        File dir2 = new File(dir, "dst2");
        List<File> files2 = TestUtil.initDirWithFiles(dir2, 5, "fff", "txt");

        File arch = new File(testDir, "pack_dir.zip");

        Packer.packDir(dir, arch);

        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);

        File unpackFilesDir = new File(testDir,"unpacked_files");
        UnPacker.unPack(arch, unpackFilesDir);
        TestUtil.hasFilesInDirExectly(unpackFilesDir,files.stream().map(File::getName).collect(Collectors.toList()),true);
        File dst2 = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dir2.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst2,files2.stream().map(File::getName).collect(Collectors.toList()),true);
    }




    @Test(dependsOnMethods = {"unPack"},groups = {"common"})
    public void packFiles() throws Exception {

        File testDir = TestUtil.initTestDir("./tmp");
        File toPackDir =new File(testDir,"to_pack");
        TestUtil.initDirectory(toPackDir, Collections.EMPTY_LIST);
        File dir1 = new File(toPackDir, "dst1");
        File dir2 = new File(toPackDir, "dst2");
        List<File> files1 = TestUtil.initDirWithFiles(dir1, 10, "file", "txt");
        List<File> files2 = TestUtil.initDirWithFiles(dir2, 5, "f", "txt");
        File dir3 = new File(dir2, "dst3");
        List<File> files3 = TestUtil.initDirWithFiles(dir3, 20, "fff", "txt");

        File file1 = new File(toPackDir, "file1.txt");
        TestUtil.createFile(file1);

        File file2 = new File(toPackDir, "file2.txt");
        TestUtil.createFile(file2);

        List<File> toPackFiles = new ArrayList<>();
        toPackFiles.add(dir1);
        toPackFiles.add(dir2);
        toPackFiles.add(file1);
        toPackFiles.add(file2);

        File arch = new File(testDir, "pack_files.zip");

        Packer.packFiles(toPackFiles, arch);

        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);

        File archToUnpack = new File(testDir, "pack_files.zip");
        File unpackFilesDir = new File(testDir,"unpacked_files");
        UnPacker.unPack(archToUnpack, unpackFilesDir);


        File[] files = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList("dst1", "dst2"));

        File dst1 = files[0].getName().equals("dst1")? files[0] : files[1];
        File dst2 = files[0].getName().equals("dst2")? files[0] : files[1];

        File dst3 = TestUtil.hasDirsInDirExectly(dst2, Arrays.asList("dst3"))[0];

        TestUtil.hasFilesInDirExectly(unpackFilesDir,Arrays.asList(file1.getName(),file2.getName()),true);
        TestUtil.hasFilesInDirExectly(dst1,files1.stream().map(File::getName).collect(Collectors.toList()), true);
        TestUtil.hasFilesInDirExectly(dst2,files2.stream().map(File::getName).collect(Collectors.toList()),true);
        TestUtil.hasFilesInDirExectly(dst3,files3.stream().map(File::getName).collect(Collectors.toList()),true);


    }



    /**
     * Проверяется распаковка заранее приготовленного архива. Архив берется из ресурсов тестов.
     * После прохождения этого теста можно использовать распаковку в тесте с упаковкой файлов для проверки результата
     */
    @Test(groups = {"common"})
    public void unPack() throws Exception {
        File testDir = TestUtil.initTestDir("./tmp");
        File zipArch = ResourceUtil.saveResource(testDir, "test.zip", "/test.zip", true);
        File toUnPackDir=new File(testDir,"unpack_dir");
        TestUtil.initDirectory(toUnPackDir, Collections.emptyList());

        UnPacker.unPack(zipArch,toUnPackDir);
        checkUnPackTestArch(toUnPackDir);

    }

    /**
     * Проверяет файлы из распакованного архива test.zip из ресурсов тестов.
     * Архив содержит несколько вложенных директорий с файлами по 5 шт. в директоии, все файлы не нулевой длины и с одинаковым содержимым.
     * @param toUnpackDir папка распаковки
     */
    public static void checkUnPackTestArch(File toUnpackDir){
        List<String> nameFiles = Arrays.asList("file1.txt", "file2.txt", "file3.txt", "file4.txt", "file5.txt");
        File dir = TestUtil.hasDirsInDirExectly(toUnpackDir, Arrays.asList("dir"))[0];
        File dir2 = TestUtil.hasDirsInDirExectly(dir, Arrays.asList("dir2"))[0];

        TestUtil.hasFilesInDirExectly(toUnpackDir, nameFiles,true);
        TestUtil.hasFilesInDirExectly(dir, nameFiles,true);
        TestUtil.hasFilesInDirExectly(dir2, nameFiles,true);

    }
}

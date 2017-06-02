package org.anantacreative.updater.tests;

import org.anantacreative.updater.FilesUtil;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;


public class FilesUtilTest {
    private static File TEST_DIR = new File("./tmp");

    @BeforeMethod
    public void beforeTests() throws Exception {
        TestUtil.initTestDir(TEST_DIR);
    }

    @Test
    public void initDirectoryTest() throws Exception {
        File dir =new File(TEST_DIR,"test");
        List<File> files = TestUtil.initDirectory(dir, Arrays.asList("file1.txt", "file2.txt"));

        List<String> dirFiles = Stream.of(dir.list()).collect(Collectors.toList());
        assertTrue(dir.exists());
        assertTrue(dirFiles.size() == files.size());

        assertTrue(dirFiles.contains("file1.txt"));
        assertTrue(dirFiles.contains("file2.txt"));

    }

    @Test
    public void initDirectoryWithFilesTest() throws Exception {
        File dir =new File(TEST_DIR,"test");
        List<File> files = TestUtil.initDirWithFiles(dir,2,"file","txt");
        List<String> dirFiles = Stream.of(dir.list()).collect(Collectors.toList());
        assertTrue(dir.exists());
        assertTrue(dirFiles.size() == files.size());

        assertTrue(dirFiles.contains("file1.txt"));
        assertTrue(dirFiles.contains("file2.txt"));

    }



    @Test
    public void recursiveDeleteTest() throws Exception {
            File dir = new File(TEST_DIR,"tmp2");
            TestUtil.initDirWithFiles(dir,4,"file","txt");

            if(!FilesUtil.recursiveDelete(dir)) fail();

            if(dir.exists()) fail("Директория "+dir.getPath()+" должна быть удалена");


    }

    @Test
    public void recursiveClearTest() throws Exception {

        File dir = new File(TEST_DIR,"tmp2");
        TestUtil.initDirWithFiles(dir,4,"file","txt");

        if(!FilesUtil.recursiveClear(dir)) fail();

        if(!dir.exists()) fail("Директория ./tmp не должна быть удалена");


    }

    @Test
    public void checkFileOrDirectory() throws Exception {

        File dir=TEST_DIR;

        boolean isDirectory =  FilesUtil.isDirectory(dir);
        assertTrue(isDirectory);

        boolean isFile =FilesUtil.isFile(new File(dir,"file1.txt"));
        assertTrue(isFile);

        dir=new File(TEST_DIR, UUID.randomUUID().toString());
        boolean isDirectoryNotExist =  FilesUtil.isDirectory(dir);
        assertTrue(isDirectoryNotExist);

        boolean isFileNotExist =FilesUtil.isFile(new File(TEST_DIR,"not_exist_file.txt"));
        assertTrue(isFileNotExist);

    }

    @Test
    public void deleteFiles() throws Exception {
        File dir = new File(TEST_DIR,"tmp2");
        List<File> files = TestUtil.initDirWithFiles(dir, 4, "file", "txt");
        FilesUtil.deleteFiles(files);

        assertTrue("Файлы не удалены",files.stream().filter(file -> file.exists()).count()==0);

    }


    @Test
    public void copyFilesToDir() throws Exception {

        File srcDir = new File(TEST_DIR,"src");
        List<File> files = TestUtil.initDirWithFiles(srcDir, 4, "file", "txt");

        File dstDir = new File(TEST_DIR,"dst");
        TestUtil.initDirectory(dstDir, Collections.emptyList());

        FilesUtil.copyFilesToDir(files, dstDir);

        assertTrue(dstDir.listFiles().length==files.size());

    }

    @Test
    public void copyDirToDir() throws Exception {

        File dstDir = new File(TEST_DIR,"dst");
        TestUtil.initDirectory(dstDir, Collections.emptyList());

        File srcDir = new File(TEST_DIR,"src");
        List<File> files = TestUtil.initDirWithFiles(srcDir, 4, "file", "txt");

        FilesUtil.copyDirToDir(srcDir, dstDir);


        assertTrue(dstDir.listFiles().length==1);
        File[] dstDirDirs = dstDir.listFiles(File::isDirectory);
        assertTrue("Directory not copied",dstDirDirs.length==1);
        assertTrue("Directory's files not copied",dstDirDirs[0].listFiles().length==files.size());

    }


    @Test
    public void copyFileToDir() throws Exception {
        File srcFile = new File(TEST_DIR,"test.txt");
        Files.createFile(srcFile.toPath());

        File dstDir = new File(TEST_DIR,"dst");
        TestUtil.initDirectory(dstDir,Collections.EMPTY_LIST);


        FilesUtil.copyFileToDir(srcFile,dstDir);
        File resFile = new File(dstDir,"test.txt");
        assertTrue("Файл не скопирован",resFile.exists());



    }
    @Test
    public void copyFileToFile() throws Exception {

        File srcFile = new File(TEST_DIR,"test.txt");
        Files.createFile(srcFile.toPath());
        File dstFile = new File(TEST_DIR,"dst_file.txt");

        FilesUtil.copyFileToFile(srcFile,dstFile);
        assertTrue("Файл не скопирован",dstFile.exists());

    }


}

package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Pack.UnPacker;
import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.testng.AssertJUnit.assertTrue;


public class PackFilesTest extends BaseActionTest {



    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

       checkFirstArchive();
       checkSecondArchive();

    }


    @Override
    protected void beforeTest() throws Exception {
        createFilesToFirstArchive();
        createFilesToSecondtArchive();
    }

    @Override
    protected String getUpdateXmlName() {
        return "pack_files.xml";
    }

    private void checkFirstArchive() throws Exception {
        File arch = new File(getTestDir(), "result1.zip");
        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);

        File dst1 = new File(getTestDir(), "dst1");
        List<File> files = Stream.of(dst1.listFiles(File::isFile)).collect(Collectors.toList());
        File dst2= new File(dst1, "dst2");
        List<File> files2 = Stream.of(dst2.listFiles(File::isFile)).collect(Collectors.toList());

        File unpackFilesDir = new File(getTestDir(),"unpacked_files1");
        UnPacker.unPack(arch, unpackFilesDir);

        File dst1_inUnpack = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dst1.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst1_inUnpack,files.stream().map(File::getName).collect(Collectors.toList()),true);
        File dst2_inUnpack = TestUtil.hasDirsInDirExectly(dst1_inUnpack, Arrays.asList(dst2.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst2_inUnpack,files2.stream().map(File::getName).collect(Collectors.toList()),true);

    }

    private void checkSecondArchive() throws Exception {
        File arch = new File(getTestDir(), "result2.zip");
        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);
        File unpackFilesDir = new File(getTestDir(),"unpacked_files2");
        UnPacker.unPack(arch, unpackFilesDir);

        File dst2 = new File(getTestDir(), "dst2");
        List<File> files = Stream.of(dst2.listFiles(File::isFile)).collect(Collectors.toList());

        File dst2_inUnpack = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dst2.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst2_inUnpack,files.stream().map(File::getName).collect(Collectors.toList()),true);
        TestUtil.hasFilesInDirExectly(unpackFilesDir,Arrays.asList("file1.txt"),true);
    }

    private void createFilesToFirstArchive() throws Exception {
        File dst1 = new File(getTestDir(), "dst1");
        TestUtil.initDirWithFiles(dst1, 10, "file", "txt");
        File dst2 = new File(dst1, "dst2");
        TestUtil.initDirWithFiles(dst2, 5, "fff", "txt");
    }

    private void createFilesToSecondtArchive() throws Exception {
        File dst2 = new File(getTestDir(), "dst2");
        TestUtil.initDirWithFiles(dst2, 10, "file", "txt");
        TestUtil.createFile(new File(getTestDir(),"file1.txt"));
    }

}

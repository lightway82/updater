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


public class PackDirsTest extends BaseActionTest {



    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

       checkFirstArchive();
      // checkSecondArchive();

    }


    @Override
    protected void beforeTest() throws Exception {
        createFilesToFirstArchive();
        createFilesToSecondtArchive();
    }

    @Override
    protected String getUpdateXmlName() {
        return "pack_dirs.xml";
    }

    private void checkFirstArchive() throws Exception {
        File arch = new File(getTestDir(), "result1.zip");
        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);

        File dir = new File(getTestDir(), "dst1");
        List<File> files = Stream.of(dir.listFiles(File::isFile)).collect(Collectors.toList());
        File dir2= new File(dir, "dst2");
        List<File> files2 = Stream.of(dir2.listFiles(File::isFile)).collect(Collectors.toList());

        File unpackFilesDir = new File(getTestDir(),"unpacked_files1");
        UnPacker.unPack(arch, unpackFilesDir);

        TestUtil.hasFilesInDirExectly(unpackFilesDir,files.stream().map(File::getName).collect(Collectors.toList()),true);
        File dst2 = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dir2.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst2,files2.stream().map(File::getName).collect(Collectors.toList()),true);

    }

    private void checkSecondArchive() throws Exception {
        File arch = new File(getTestDir(), "result2.zip");
        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);
        File unpackFilesDir = new File(getTestDir(),"unpacked_files2");
        UnPacker.unPack(arch, unpackFilesDir);

        File dir = new File(getTestDir(), "dst2");
        List<File> files = Stream.of(dir.listFiles(File::isFile)).collect(Collectors.toList());


        TestUtil.hasFilesInDirExectly(unpackFilesDir,files.stream().map(File::getName).collect(Collectors.toList()),true);
        TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dir.getName()));
        TestUtil.hasFilesInDirExectly(unpackFilesDir,Arrays.asList("file1.txt"),true);
    }

    private void createFilesToFirstArchive() throws Exception {
        File dir1 = new File(getTestDir(), "dst1");
        TestUtil.initDirWithFiles(dir1, 10, "file", "txt");
        File dir2 = new File(dir1, "dst2");
        TestUtil.initDirWithFiles(dir2, 5, "fff", "txt");
    }

    private void createFilesToSecondtArchive() throws Exception {
        File dir = new File(getTestDir(), "dst2");
        TestUtil.initDirWithFiles(dir, 10, "file", "txt");
        TestUtil.createFile(new File(getTestDir(),"file1.txt"));
    }

}

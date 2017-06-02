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


public class PackTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {


        File arch = new File(getTestDir(), "result.zip");

        assertTrue(arch.exists());
        assertTrue(arch.length() > 0);

        File unpackFilesDir = new File(getTestDir(),"unpacked_files");
        UnPacker.unPack(arch, unpackFilesDir);

        File dir = new File(getTestDir(), "dst");
        List<File> files = Stream.of(dir.listFiles(File::isFile)).collect(Collectors.toList());
        File dir2 = new File(dir, "dst2");
        List<File> files2 = Stream.of(dir2.listFiles(File::isFile)).collect(Collectors.toList());


        TestUtil.hasFilesInDirExectly(unpackFilesDir,files.stream().map(File::getName).collect(Collectors.toList()),true);
        File dst2 = TestUtil.hasDirsInDirExectly(unpackFilesDir, Arrays.asList(dir2.getName()))[0];
        TestUtil.hasFilesInDirExectly(dst2,files2.stream().map(File::getName).collect(Collectors.toList()),true);
    }

    @Override
    protected void beforeTest() throws Exception {

        File dir = new File(getTestDir(), "dst");
        TestUtil.initDirWithFiles(dir, 10, "file", "txt");
        File dir2 = new File(dir, "dst2");
        TestUtil.initDirWithFiles(dir2, 5, "fff", "txt");
    }

    @Override
    protected String getUpdateXmlName() {
        return "pack.xml";
    }
}

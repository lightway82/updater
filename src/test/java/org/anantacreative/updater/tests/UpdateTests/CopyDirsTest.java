package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;


public class CopyDirsTest extends BaseActionTest {

    private static File TEST_DIR = new File("./tmp");
    private static int NUM_FILES=4;

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        File resDir = new File(TEST_DIR,"dst/src");
        assertTrue(resDir.exists());
        assertTrue(resDir.listFiles().length == NUM_FILES);
    }

    @Override
    protected void beforeTest() throws Exception {
        TestUtils.initTestDir(TEST_DIR);

        File srcDir = new File(TEST_DIR,"src");
        List<File> files = TestUtils.initDirWithFiles(srcDir, 4, "file", "txt");

        File dstDir = new File(TEST_DIR,"dst");
        TestUtils.initDirectory(dstDir, Collections.emptyList());
    }

    @Override
    protected String getUpdateXmlName() {
        return "copy_dir.xml";
    }
}

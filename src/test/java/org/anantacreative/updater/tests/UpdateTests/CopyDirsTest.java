package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;


public class CopyDirsTest extends BaseActionTest {


    private static int NUM_FILES=4;

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        File resDir = new File(getTestDir(),"dst/src");
        assertTrue(resDir.exists());
        assertTrue(resDir.listFiles().length == NUM_FILES);
    }

    @Override
    protected void beforeTest() throws Exception {


        File srcDir = new File(getTestDir(),"src");
        List<File> files = TestUtil.initDirWithFiles(srcDir, 4, "file", "txt");

        File dstDir = new File(getTestDir(),"dst");
        TestUtil.initDirectory(dstDir, Collections.emptyList());
    }

    @Override
    protected String getUpdateXmlName() {
        return "copy_dir.xml";
    }
}

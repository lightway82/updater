package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.testng.AssertJUnit.assertTrue;


public class CopyFilesTest extends BaseActionTest {


    private static int NUM_FILES=4;

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        File dstDir = new File(getTestDir(),"dst");
        assertTrue(dstDir.listFiles().length == NUM_FILES);
    }

    @Override
    protected void beforeTest() throws Exception {


        File srcDir = new File(getTestDir(),"src");
        List<File> files = TestUtils.initDirWithFiles(srcDir, 4, "file", "txt");

        File dstDir = new File(getTestDir(),"dst");
        TestUtils.initDirectory(dstDir, Collections.emptyList());




    }

    @Override
    protected String getUpdateXmlName() {
        return "copy_files.xml";
    }
}

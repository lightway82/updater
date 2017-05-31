package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtils;

import java.io.File;

import static org.testng.AssertJUnit.assertTrue;


public class DeleteFilesTest extends BaseActionTest {

    private static final String DST_DIR_NAME="tmp2";

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

        File dir=new File(getTestDir(),DST_DIR_NAME);
        assertTrue("Files not deleted",dir.listFiles().length==0);


    }

    @Override
    protected void beforeTest() throws Exception {

        TestUtils.initDirWithFiles(new File(getTestDir(),DST_DIR_NAME),3,"file","txt");

    }


    @Override
    protected String getUpdateXmlName() {
        return "delete_files.xml";
    }
}

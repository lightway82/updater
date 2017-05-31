package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtils;

import java.io.File;

import static org.testng.AssertJUnit.assertFalse;


public class DeleteDirsTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

         File dir=new File(getTestDir(),"tmp2");
         assertFalse("Directory not deleted",dir.exists());


    }

    @Override
    protected void beforeTest() throws Exception {

        TestUtils.initDirWithFiles(new File(getTestDir(),"tmp2"),4,"test","txt");


    }

    @Override
    protected String getUpdateXmlName() {
        return "delete_dir.xml";
    }
}

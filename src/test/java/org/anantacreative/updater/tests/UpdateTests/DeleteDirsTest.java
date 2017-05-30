package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;
import java.util.UUID;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;


public class DeleteDirsTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

        File dir=new File("./tmp/tmp2");
         assertFalse("Directory not deleted",dir.exists());


    }

    @Override
    protected void beforeTest() throws Exception {
        File dir = new File("./tmp/tmp2");
        if(!dir.exists()) dir.mkdir();
        assertTrue(dir.exists());
        File f = new File(dir,UUID.randomUUID().toString());
        f.createNewFile();
        assertTrue(f.exists());
    }

    @Override
    protected String getUpdateXmlName() {
        return "delete_dir.xml";
    }
}

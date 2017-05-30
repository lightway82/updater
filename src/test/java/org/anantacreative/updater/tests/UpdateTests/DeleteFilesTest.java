package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;

import static org.testng.AssertJUnit.assertTrue;


public class DeleteFilesTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

        File dir=new File("./tmp/tmp2");
        assertTrue("Files not deleted",dir.listFiles().length==0);


    }

    @Override
    protected void beforeTest() throws Exception {
        File dir = new File("./tmp/tmp2");
        if(!dir.exists()) dir.mkdir();
        assertTrue(dir.exists());

        for(int i=1;i<4;i++){
            File f = new File(dir, "file"+i+".txt");
            f.createNewFile();
            assertTrue(f.exists());
        }

    }


    @Override
    protected String getUpdateXmlName() {
        return "delete_files.xml";
    }
}

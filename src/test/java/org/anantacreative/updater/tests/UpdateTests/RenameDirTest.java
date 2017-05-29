package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;

import static org.testng.AssertJUnit.fail;


public class RenameDirTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {


        File dir=new File("./tmp/tmp3");
        if(!dir.exists())  fail("Отсутствует конечная директория ./tmp/tmp3");

    }

    @Override
    protected void beforeTest() throws Exception {
        File dir=new File("./tmp/");
        File dir2=new File(dir,"tmp2");
        if(!dir2.exists()) dir2.mkdir();


    }

    @Override
    protected String getUpdateXmlName() {
        return "rename_dir.xml";
    }
}

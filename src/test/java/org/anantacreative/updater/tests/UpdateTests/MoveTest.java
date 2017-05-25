package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;

import static org.testng.AssertJUnit.fail;


public class MoveTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

        File dir=new File("./tmp");
        if(!dir.exists()) fail("Отсутствует директория ./tmp  конечной загрузки");
        File dir2=new File(dir,"tmp2");
        if(!dir2.exists())  fail("Отсутствует директория ./tmp/tmp2  конечной загрузки");

        File file1=new File(dir,"file1.txt");
        File file2=new File(dir2,"file2.txt");


        if(!file1.exists()) fail();
        if(!file2.exists()) fail();
    }

    @Override
    protected void beforeTest() {

    }

    @Override
    protected String getUpdateXmlName() {
        return "move.xml";
    }
}

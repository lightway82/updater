package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;
import java.nio.file.Files;

import static org.testng.AssertJUnit.fail;


public class MoveTest3 extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
    /*
    <Action type="Move">
            <File url="" src="tmp/f1.txt"  dst="tmp/tmp2/fff1.txt"/>
            <File url="" src="tmp/f2.txt"  dst="tmp/tmp2/fff2.txt"/>
        </Action>
     */



        File dir2=new File(getTestDir(),"tmp2");
        if(!dir2.exists())  fail("Отсутствует директория ./tmp/tmp2  конечной загрузки");

        File file1=new File(dir2,"fff1.txt");
        File file2=new File(dir2,"fff2.txt");


        if(!file1.exists()) fail();
        if(!file2.exists()) fail();
    }

    @Override
    protected void beforeTest() throws Exception {


        Files.createFile(new File(getTestDir(),"f1.txt").toPath());
        Files.createFile(new File(getTestDir(),"f2.txt").toPath());
    }


    @Override
    protected String getUpdateXmlName() {
        return "move3.xml";
    }
}

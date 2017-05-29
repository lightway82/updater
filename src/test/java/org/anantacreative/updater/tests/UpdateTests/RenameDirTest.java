package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;

import java.io.File;
import java.nio.file.Files;

import static org.testng.AssertJUnit.fail;


public class RenameDirTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {

        File dir=new File("./tmp");
        File dir2=new File(dir,"tmp2");
        if(!dir2.exists())  fail("Отсутствует директория ./tmp/tmp2  конечной загрузки");

        File file1=new File(dir2,"111.zip");


        if(!file1.exists()) fail("Результирующий файл отсутствует ");



    }

    @Override
    protected void beforeTest() throws Exception {
        File dir=new File("./tmp/");
        File dir2=new File(dir,"tmp2");
        if(!dir2.exists()) dir2.mkdir();

        Files.createFile(new File(dir2,"file1.txt").toPath());
    }

    @Override
    protected String getUpdateXmlName() {
        return "rename.xml";
    }
}

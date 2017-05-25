package org.anantacreative.updater.tests;

import org.anantacreative.updater.FilesUtil;
import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.Update.XML.XmlUpdateTaskCreator;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.*;

/**
 *
 */
public class UpdateTest {

private static class Value<T>{
    private T value;
    private boolean present;
    private boolean error;
    private boolean complete;


    public boolean isPresent(){return present && !error && complete;}

    public boolean isError() {
        return error;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setError() {
        error = true;
        complete=true;

    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        present=true;
        complete=true;
        this.value = value;
    }
}

    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }

    @BeforeTest
    public void clearing(){
       File dir = new File("./tmp");
        if(dir.exists())  assertTrue(FilesUtil.recursiveDelete(dir));
        File dirDownloading = new File("./downloading");
        if(dirDownloading.exists())  assertTrue(FilesUtil.recursiveDelete(dirDownloading));
    }

    @Test
    public void moveTest() throws UpdateActionException, MalformedURLException, SAXException, AbstractUpdateTaskCreator.CreateUpdateTaskError, InterruptedException {
            Value<UpdateTask> updateTaskValue = new Value<>();
         createTask("move.xml", new SimpleUpdateTaskCreatorListener() {
            @Override
            public void taskCompleted(UpdateTask ut) {
                updateTaskValue.setValue(ut);

            }

            @Override
            public void error(Exception e) {
                updateTaskValue.setError();
            }

        });

         while (!updateTaskValue.isComplete()){
             Thread.sleep(500);
         }

        if (!updateTaskValue.isPresent()) fail("Значение UpdateTask не получено");
        UpdateTask ut=updateTaskValue.getValue();

        assertNotNull("Не создан объект UpdateTask",ut);

        try {
            ut.update();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Ошибка выполнения Action Move обновления");
        }


        File dir=new File("./tmp");
        if(!dir.exists()) fail("Отсутствует директория ./tmp  конечной загрузки");
        File dir2=new File(dir,"tmp2");
        if(!dir2.exists())  fail("Отсутствует директория ./tmp/tmp2  конечной загрузки");

        File file1=new File(dir,"file1.txt");
        File file2=new File(dir2,"file2.txt");


        if(!file1.exists()) fail();
        if(!file2.exists()) fail();


    }


    private void createTask(String updateFileName, AbstractUpdateTaskCreator.Listener listener) throws MalformedURLException, SAXException, AbstractUpdateTaskCreator.CreateUpdateTaskError {
        UpdateTask task=null;

        XmlUpdateTaskCreator taskCreator=new XmlUpdateTaskCreator(
                new File("./downloading"),
                new File("./"),
                listener,
                new URL("http://localhost:"+TestingUpdateServer.getPort()+"/update_suit/"+updateFileName));

               taskCreator.createTask(true);



    }


    private abstract static class SimpleUpdateTaskCreatorListener implements AbstractUpdateTaskCreator.Listener{



        @Override
        public void completeFile(String url, File path) {

        }

        @Override
        public void currentFileProgress(float progress) {

        }

        @Override
        public void nextFileStartDownloading(String url, File path) {

        }

        @Override
        public void totalProgress(float progress) {

        }
    }



}

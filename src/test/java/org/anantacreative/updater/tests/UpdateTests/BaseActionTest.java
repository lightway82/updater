package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.FilesUtil;
import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
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
public abstract class BaseActionTest {

protected static class Value<T>{
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
    public void clearing() throws Exception {
       File dir = new File("./tmp");
        if(dir.exists())  assertTrue(FilesUtil.recursiveClear(dir));
        else dir.mkdir();
        File dirDownloading = new File("./downloading");
        if(dirDownloading.exists())  assertTrue(FilesUtil.recursiveDelete(dirDownloading));
        else dirDownloading.mkdir();

        beforeTest();
    }




        @Test
    public  void test() throws Exception {
        String updateXml = getUpdateXmlName();
        Value<UpdateTask> updateTaskValue = new Value<>();
        createTask(updateXml, new SimpleUpdateTaskCreatorListener() {
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
        testLogic(ut);
    }

    protected abstract void testLogic(UpdateTask ut) throws Exception;

    /**
     * логика до теста
     */
    protected abstract void beforeTest() throws Exception;
    /**
     * Должен вернуть имя файла update.xml для тесте из папки update_suit
     * @return
     */
    protected abstract String getUpdateXmlName();

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

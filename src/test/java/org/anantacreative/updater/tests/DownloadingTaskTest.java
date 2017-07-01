package org.anantacreative.updater.tests;

import org.anantacreative.updater.Downloader.DownloadingTask;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static org.testng.AssertJUnit.fail;

/**
 *
 */
@Test(groups = {"common"})
public class DownloadingTaskTest {
    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }

    public void downloadingCheck() throws Exception {
        Value<Boolean> val =new Value<>();
        DownloadingTask dt=new DownloadingTask(new DownloadingTask.TaskCompleteListener() {
            @Override
            public void complete() {
                val.setValue(true);
            }

            @Override
            public void error(String msg) {
                val.setError(new Exception(msg));
            }

            @Override
            public void completeFile(DownloadingTask.DownloadingItem item) {

            }

            @Override
            public void currentFileProgress(float progress) {

            }

            @Override
            public void canceled() {

            }

            @Override
            public void nextFileStartDownloading(DownloadingTask.DownloadingItem item) {

            }
        });

        File dir = TestUtil.initTestDir("./tmp");

        File file1 = new File(dir,"version.xml");
        File file2 = new File(dir,"update.xml");
        try {
            dt.addItem(new DownloadingTask.DownloadingItem(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"), file1));
            dt.addItem(new DownloadingTask.DownloadingItem(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/update.xml"), file2));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Downloading task not added.");
        }
        dt.download(true);
        try {
            while (!val.isPresent()){
                    Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TestUtil.hasFilesInDirExectly(dir, Arrays.asList(file1.getName(),file2.getName()),true);

    }
}

package org.anantacreative.updater.tests;

import org.anantacreative.updater.Downloader.DownloadingTask;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

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

    public void downloadingCheck(){
        DownloadingTask dt=new DownloadingTask(new DownloadingTask.TaskCompleteListener() {
            @Override
            public void complete() {

            }

            @Override
            public void error(String msg) {
                fail("Файлы не загружены. "+msg);
            }

            @Override
            public void completeFile(DownloadingTask.DownloadingItem item) {
                if(!item.getDstPath().exists()) fail("Файлы не загружены. "+item);

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
        try {
            dt.addItem(new DownloadingTask.DownloadingItem(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"),new File("./downloading/version.xml")));
            dt.addItem(new DownloadingTask.DownloadingItem(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/update.xml"),new File("./downloading/update.xml")));

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("Файлы не загружены. ");
        }
        dt.download(true);

    }
}

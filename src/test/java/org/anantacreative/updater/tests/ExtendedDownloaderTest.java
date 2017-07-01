package org.anantacreative.updater.tests;

import org.anantacreative.updater.Downloader.ExtendedDownloader;
import org.anantacreative.updater.ResourceUtil;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.AssertJUnit;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Optional;


public class ExtendedDownloaderTest {
    private ExtendedDownloader downloader;
    private  DownloadingObserver observer;

    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }

    @Test(groups = {"common"},timeOut = 10000)
    public void newDownload() throws Exception {

        File dir = TestUtil.initTestDir("./tmp");
        File dst = new File(dir, "tst.txt");
        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/version.xml");
        downloader = new ExtendedDownloader(url, dst, true);
        observer = new DownloadingObserver();
        downloader.addObserver(observer);
        downloader.startDownload();


        while (!observer.isComplete()){
          Thread.sleep(1000);
        }

        if(observer.isError()) AssertJUnit.fail(observer.getCause().map(e -> e.getMessage()).orElse(""));

        TestUtil.hasFilesInDir(dir, Arrays.asList(dst.getName()),true);

    }

    @Test(groups = {"common"})
    public void partedDownload() throws Exception {


        File dir = TestUtil.initTestDir("./tmp");
        File dst = new File(dir, "version.xml");
        ResourceUtil.saveResource(dir,dst.getName(),"/webroot/version.xml",true);


        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/version.xml");
        downloader = new ExtendedDownloader(url, dst, false);
        observer = new DownloadingObserver();
        downloader.addObserver(observer);
        downloader.startDownload();


        while (!observer.isComplete()){
            Thread.sleep(1000);
        }

        if(observer.isError()) AssertJUnit.fail(observer.getCause().map(e -> e.getMessage()).orElse(""));

        TestUtil.hasFilesInDir(dir, Arrays.asList(dst.getName()),true);

    }
    @Test(groups = {"common"})
    public void breakingLinkDownload() throws Exception {


        File dir = TestUtil.initTestDir("./tmp");

        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/version11.xml");
        downloader = new ExtendedDownloader(url, new File(dir,"file.txt"), false);
        observer = new DownloadingObserver();
        downloader.addObserver(observer);
        downloader.startDownload();


        while (!observer.isComplete()){
            Thread.sleep(1000);
        }

        if(observer.isError()) {
            AssertJUnit.assertTrue(observer.getCause().map(e -> e.getMessage()).orElse("").equals("BreakingLink"));
        }else AssertJUnit.fail("Ожидается ошибка!");



    }

    private static  class DownloadingObserver implements Observer{

        public final Value<Boolean> value = new Value<>();
        public Optional<Exception> getCause(){return value.getErrorCause();}
        public boolean isCompleteNormal(){ return value.isComplete(); }
        public boolean isError(){return value.isError();}
        public boolean isComplete(){return value.isComplete();}

        @Override
        public void update(Observable o, Object arg) {

            ExtendedDownloader downloader = null;

            if (o == null) {
                value.setError(new NullPointerException());
                return;
            }else {
                 downloader =(ExtendedDownloader)o;
            }

            ExtendedDownloader.DownloadingStatus status = downloader.getStatus();
            switch (status) {
                case COMPLETE:

                    value.setValue(true);
                    break;
                case BREAKINGLINK:

                    value.setError(new Exception("BreakingLink"));
                    break;
                case ERROR:

                    value.setError(new Exception("Error"));
                    break;
                case DOWNLOADING:

                    break;
                case CANCELLED:
                    break;
            }
        }
    }


}

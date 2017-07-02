package org.anantacreative.updater.tests;

import org.anantacreative.updater.Downloader.ExtendedDownloader;
import org.anantacreative.updater.FilesUtil;
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

@Test(groups = {"common"},timeOut = 40000)
public class ExtendedDownloaderTest {
    private ExtendedDownloader downloader;
    private  DownloadingObserver observer;
    private  String HASH;


    @BeforeClass
    public void init() throws Exception {
        TestingUpdateServer.startServer();
        File dir = TestUtil.initTestDir("./tmp");
        File file = ResourceUtil.saveResource(dir,"test.zip","/test.zip",true);
        HASH = FilesUtil.getHashOfFile(file);
    }


    public void newDownload() throws Exception {

        File dir = TestUtil.initTestDir("./test");
        File dst = new File(dir, "test.zip");
        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/test.zip");
        downloader = new ExtendedDownloader(url, dst, true);
        observer = new DownloadingCompleteObserver();
        downloader.addObserver(observer);
        downloader.download();


        while (!observer.isComplete()){
          Thread.sleep(1000);
        }

        if(observer.isError()) {
            observer.getCause().ifPresent(e -> e.printStackTrace());
            AssertJUnit.fail(observer.getCause().map(e -> e.getMessage()).orElse(""));
        }
        TestUtil.hasFilesInDir(dir, Arrays.asList(dst.getName()),true);
        AssertJUnit.assertEquals(HASH,FilesUtil.getHashOfFile(dst));

    }


    public void partedDownload() throws Exception {


        File dir = TestUtil.initTestDir("./test");
        File dst = new File(dir, "test.zip");
        ResourceUtil.saveResource(dir,dst.getName(),"/webroot/test.zip",true);


        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/test.zip");
        downloader = new ExtendedDownloader(url, dst, false);
        observer = new DownloadingCompleteObserver();
        downloader.addObserver(observer);
        downloader.download();


        while (!observer.isComplete()){
            Thread.sleep(1000);
        }

        if(observer.isError()){
            observer.getCause().ifPresent(e -> e.printStackTrace());
            AssertJUnit.fail(observer.getCause().map(e -> e.getMessage()).orElse(""));
        }

        TestUtil.hasFilesInDir(dir, Arrays.asList(dst.getName()),true);
        AssertJUnit.assertEquals(HASH,FilesUtil.getHashOfFile(dst));
    }


    public void resumeDownload() throws Exception {


        File dir = TestUtil.initTestDir("./test");
        File dst = new File(dir, "test.zip");

        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/test.zip");
        downloader = new ExtendedDownloader(url, dst, true);
        DownloadingResumeObserver observer = new DownloadingResumeObserver();
        downloader.addObserver(observer);
        downloader.download();

        while (!observer.isPartDownloaded()){
            Thread.sleep(5);
        }
        AssertJUnit.assertTrue("Загрузка здесь не должна быть завершена",!observer.isComplete());

        downloader.resume();

        while (!observer.isComplete()){
            Thread.sleep(1000);
        }

        if(observer.isError()) {
            observer.getCause().ifPresent(e -> e.printStackTrace());
            AssertJUnit.fail(observer.getCause().map(e -> e.getMessage()).orElse(""));
        }

        TestUtil.hasFilesInDir(dir, Arrays.asList(dst.getName()),true);
        AssertJUnit.assertEquals(HASH,FilesUtil.getHashOfFile(dst));

    }


    public void breakingLinkDownload() throws Exception {


        File dir = TestUtil.initTestDir("./test");

        URL url = new URL("http://localhost:" + TestingUpdateServer.getPort() + "/version11.xml");
        downloader = new ExtendedDownloader(url, new File(dir,"file.txt"), false);
        observer = new DownloadingCompleteObserver();
        downloader.addObserver(observer);
        downloader.download();

        while (!observer.isComplete()){
            Thread.sleep(1000);
        }

        if(observer.isError()) {
            AssertJUnit.assertTrue(observer.getCause().map(e -> e.getMessage()).orElse("").equals("BreakingLink"));
        }else AssertJUnit.fail("Ожидается ошибка!");



    }


    private abstract static  class DownloadingObserver implements Observer{

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


            stateLogic(downloader.getState(),downloader, value);
        }

        public abstract void stateLogic(ExtendedDownloader.DownloadingState status, ExtendedDownloader downloader, Value<Boolean> value);
    }




    private static  class DownloadingCompleteObserver  extends DownloadingObserver{


        @Override
        public void stateLogic(ExtendedDownloader.DownloadingState status, ExtendedDownloader downloader, Value<Boolean> value) {
            switch (status) {
                case COMPLETE:
                    value.setValue(true);
                    break;
                case BREAKING_LINK:
                    value.setError(new Exception("BreakingLink"));
                    break;
                case ERROR:
                    value.setError(new Exception(downloader.getException()));
                    break;
                case DOWNLOADING:
                    break;
                case CANCELLED:
                    break;
            }
        }
    }

    private static  class DownloadingResumeObserver extends DownloadingObserver{
        public boolean isPartDownloaded(){return partDownloaded;}
        private boolean partDownloaded=false;

        @Override
        public void stateLogic(ExtendedDownloader.DownloadingState status, ExtendedDownloader downloader, Value<Boolean> value) {
            switch (status) {
                case COMPLETE:

                    value.setValue(true);
                    break;
                case BREAKING_LINK:
                    value.setError(new Exception("BreakingLink"));
                    break;
                case ERROR:
                    value.setError(new Exception(downloader.getException()));
                    break;
                case DOWNLOADING:

                    if(!partDownloaded){

                        if(downloader.getProgress()>0) {
                            downloader.pause();
                            partDownloaded=true;

                        }
                    }
                    break;
                case CANCELLED:
                    break;
            }
        }
    }

}

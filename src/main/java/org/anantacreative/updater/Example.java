package org.anantacreative.updater;

import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.Update.XML.XmlUpdateTaskCreator;
import org.anantacreative.updater.VersionCheck.DefineActualVersionError;
import org.anantacreative.updater.VersionCheck.XML.XmlVersionChecker;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 */
public class Example {


    public static void main(String[] args){
        /*
        DownloadingTask dt=new DownloadingTask(new DownloadingTask.TaskCompleteListener() {
            @Override
            public void complete() {
                System.out.println("Все скачано");
            }

            @Override
            public void error(String msg) {
                System.out.println("Ошибка: "+msg);
            }

            @Override
            public void completeFile(String url, File path) {
                System.out.println("Файл закачан!");
            }

            @Override
            public void currentFileProgress(float progress) {
                progressPercentage((int)progress,100);

            }

            @Override
            public void canceled() {
                System.out.println("Отменено пользователем");
            }

            @Override
            public void nextFileStartDownloading(String url, File path) {
                System.out.println("Скачивается файл: "+ url+" путь: "+path.getAbsolutePath());
            }
        });
        try {
            dt.addItem(new URL("http://www.biomedis.ru/doc/bazovie_kompleksi/bazoviy_kompleks_utro.zip"),new File("./downloads/f1.zip"));
            dt.addItem(new URL("http://www.biomedis.ru/doc/bazovie_kompleksi/bazoviy_kompleks_gerpes.zip"),new File("./downloads/f2.zip"));
            dt.addItem(new URL("http://www.biomedis.ru/doc/bazovie_kompleksi/bazoviy_kompleks_prostatit.zip"),new File("./downloads/f3.zip"));
            dt.addItem(new URL("http://www.biomedis.ru/doc/bazovie_kompleksi/bazoviy_kompleks_gipoglikemia.zip"),new File("./downloads/f4.zip"));

            dt.download(false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
*/

        Version curVersion=new Version(1,0,0);

        try {
            XmlVersionChecker versionChecker=new XmlVersionChecker(curVersion,new URL("http://www.biomedis.ru/doc/b_mair/updater/version.xml"));
            if(versionChecker.checkNeedUpdate())   {
                System.out.println("Требуется обновление");
                System.out.println("Актуальная версия = "+ versionChecker.getActualVersion());
                System.out.println("Текцщая версия = "+ versionChecker.getCurrentVersion());

                XmlUpdateTaskCreator taskCreator=new XmlUpdateTaskCreator(
                        new File("./downloads/"),
                        new File("./"),
                        new AbstractUpdateTaskCreator.Listener() {
                            @Override
                            public void taskCompleted(UpdateTask ut) {
                                System.out.println("Update  Task создан");
                            }

                            @Override
                            public void error(Exception e) {
                                System.out.println("Ошибка создания Update  Task");
                                e.printStackTrace();
                            }
                        },
                        new URL("http://www.biomedis.ru/doc/b_mair/updater/update.xml"));

                taskCreator.createTask(true);
            }
            else   System.out.println("Обновление не требуется");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (DefineActualVersionError e) {
            e.printStackTrace();
        } catch (AbstractUpdateTaskCreator.CreateUpdateTaskError createUpdateTaskError) {
            createUpdateTaskError.printStackTrace();
        }
    }
    public static void progressPercentage(int remain, int total) {
        if (remain > total) {
            throw new IllegalArgumentException();
        }
        int maxBareSize = 10; // 10unit for 100%
        int remainProcent = ((100 * remain) / total) / maxBareSize;
        char defaultChar = '-';
        String icon = "*";
        String bare = new String(new char[maxBareSize]).replace('\0', defaultChar) + "]";
        StringBuilder bareDone = new StringBuilder();
        bareDone.append("[");
        for (int i = 0; i < remainProcent; i++) {
            bareDone.append(icon);
        }
        String bareRemain = bare.substring(remainProcent, bare.length());
        System.out.print("\r" + bareDone + bareRemain + " " + remainProcent * 10 + "%");
        if (remain == total) {
            System.out.print("\n");
        }
    }
}

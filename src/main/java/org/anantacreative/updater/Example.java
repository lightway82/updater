package org.anantacreative.updater;

import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
import org.anantacreative.updater.Update.UpdateException;
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

        Version curVersion=new Version(1,0,0);
        XmlUpdateTaskCreator taskCreator;
        try {
            XmlVersionChecker versionChecker=new XmlVersionChecker(curVersion,new URL("http://www.biomedis.ru/doc/b_mair/updater/version.xml"));
            if(versionChecker.checkNeedUpdate())   {
                System.out.println("Требуется обновление");
                System.out.println("Актуальная версия = "+ versionChecker.getActualVersion());
                System.out.println("Текцщая версия = "+ versionChecker.getCurrentVersion());

                 taskCreator=new XmlUpdateTaskCreator(
                        "downloads/",
                        new File("./"),
                        new AbstractUpdateTaskCreator.Listener() {
                            @Override
                            public void taskCompleted(UpdateTask ut,File rootDirApp, File downloadDir) {

                                System.out.println("Update  Task создан");

                                    ut.update(new UpdateTask.UpdateListener() {
                                        @Override
                                        public void progress(int persent) {

                                        }

                                        @Override
                                        public void completed() {

                                        }

                                        @Override
                                        public void error(UpdateException e) {

                                        }
                                    });

                            }

                            @Override
                            public void error(Exception e) {
                                System.out.println("Ошибка создания Update  Task");
                                e.printStackTrace();
                            }

                            @Override
                            public void completeFile(String url, File path) {
                                System.out.println("\nФайл закачан!");
                            }

                            @Override
                            public void currentFileProgress(float progress) {

                                progressPercentage((int)progress,100);
                            }

                            @Override
                            public void nextFileStartDownloading(String url, File path) {
                                System.out.println("Скачивается файл: "+ url+" путь: "+path.getAbsolutePath());
                            }

                            @Override
                            public void totalProgress(float progress) {
                                progressPercentage((int)progress,100);
                            }
                        },
                        new URL("http://www.biomedis.ru/doc/b_mair/updater/update.xml"));

                taskCreator.createTask(false);
            }
            else   System.out.println("Обновление не требуется");


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (DefineActualVersionError e) {
            e.printStackTrace();
        } catch (AbstractUpdateTaskCreator.CreateUpdateTaskError e) {
            e.printStackTrace();
        }catch (Exception e){e.printStackTrace();}
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

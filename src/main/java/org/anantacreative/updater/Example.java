package org.anantacreative.updater;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ananta on 04.05.2017.
 */
public class Example {


    public static void main(String[] args){
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

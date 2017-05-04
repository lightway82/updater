/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anantacreative.updater;


import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;


/**
 * @author Anama
 */
public class DownloadService implements Observer, ServicesInterface {

    private static DownloadService single = new DownloadService();

    private SimpleIntegerProperty countDownloadingThread = new SimpleIntegerProperty(0);
    private int maxThread = 0;
    private File downloadBasePath;

    private LinkedList<ExtendedDownloader> filesForDownload =new LinkedList<>();
    private List<File> filesDownloaded =new LinkedList<>();
    private boolean flagProcessing = false;

    private DownloadService() {

    }

    public static DownloadService getInstance() {
        return single;
    }

    @Override
    public void init(int maxConnections, File baseDir) {
        maxThread = maxConnections;
        downloadBasePath = baseDir;
        if (!downloadBasePath.exists()) downloadBasePath.mkdir();
        System.out.println(downloadBasePath.getAbsolutePath());

        System.out.println("Сервис загрузки инициализированн");
    }

    @Override
    public void startService() {
        flagProcessing = true;
        System.out.print("Сервис загрузки .....");
        countDownloadingThread.addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) ->
        {

            //запускаем только при рабочем сервисе и если значение счетчика уменьшается. 
            if (flagProcessing && newValue.intValue() < oldValue.intValue()) {

                if (newValue.intValue() >= 0) startDownloadFiles();
                else {
                    System.out.println("Отрицательный счетчик");
                    this.countDownloadingThread.set(0);
                }
            }

        });
        System.out.println("запущен");
        startDownloadFiles();

    }

    @Override
    public void stopService() {
        flagProcessing = false;
        countDownloadingThread.setValue(0);
        //незабыть остановить запущенные потоки
        Iterator<ExtendedDownloader> iterator = filesForDownload.iterator();
        while (iterator.hasNext()) {
            ExtendedDownloader next = iterator.next();
            System.out.println("Остановка закачки.");
            next.pause();


        }


        for (ExtendedDownloader e : filesForDownload) {
            if (e.thread.isAlive()) try {
                e.thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        filesForDownload.clear();
        filesDownloaded.clear();


    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    private boolean isInet = false;

    public boolean checkInternet(URL url) {
        boolean flag = false;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            flag = true;

        } catch (IOException ex) {
            flag = false;

        } finally {

            if (connection != null) connection.disconnect();

            return flag;
        }

    }

    /**
     * Активизирует запуск процессов закачки
     */
    synchronized private void startDownloadFiles() {
        Lections next = null;
        while (countDownloadingThread.getValue() < maxThread) {

            /****** Проверка инете ****/

            //Проверим инет тут есь или нет, недоверяем основному сервису, тк он не часто проверяет.
            //плодить битых ссылок
            try {
                //установим переменную. Ее проверяют в обработчике ошибки загрузчика, чтобы не устанавливать флаг битый ссылки
                this.isInet = checkInternet(new URL(BaseController.getApp().config.getString("serverUrl")));
            } catch (MalformedURLException ex) {
                this.isInet = false;
            }
            //сам сервис остановится позже
            if (this.isInet == false) return;//если нет инета не станим плодить новые загрузки

/********************************************************/
            ExtendedDownloader ld = null;

            //сначала возьмем еще недокаченные, если их нет то возьмем новое
            next = ps.lectionsFacade.findNextProcessDownloaded(this.filesDownloaded);
            if (next == null) next = ps.lectionsFacade.findNextNotDownloaded(this.filesDownloaded);

            if (next == null) break;//выход из сервиса, потоки отработают свое и все.

            System.out.print("Создается загрузка ID=" + next.getId() + "....");
            if (next.getAudioUrl() == null) {
                next.setAudioUrl("");
                try {

                    ps.lectionsFacade.edit(next);

                } catch (NonexistentEntityException ex) {
                    System.out.println("Ошибка обновления(JPA) AudioURL ID=" + next.getId());
                    Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    System.out.println("Ошибка обновления(Общая) AudioURL ID=" + next.getId());
                    Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            URL url = null;
            try {
                url = new URL(next.getAudioUrl());
            } catch (MalformedURLException ex) {
                System.out.println("Отсутствует URL ID=" + next.getId());
                continue;

            }

            //если мы его еще не загрузили(мы искали незагруженные) и не загружали(тк не прописали имя)
            if (next.getAudiofile() == null ? true : next.getAudiofile().isEmpty()) {
                System.out.println("Выбранна новая загрузка ID=" + next.getId());
                String name = next.getAudioUrl().substring(next.getAudioUrl().lastIndexOf('/') + 1);
                File file = new File(this.downloadBasePath, name);
                if (file.exists()) {
                    // если файлс с именем этой даты есть то добавим ID
                    name = next.getId() + "_" + name;
                    file = new File(this.downloadBasePath, name);
                    System.out.println("Файл существует, переименован ID=" + next.getId());
                }
                if (url != null) {
                    ld = new ExtendedDownloader(url, file, true, (Object) next.getId());

                    try {
                        next.setAudiofile(name);//будет далее индикатором начавшейся загрузки
                        ps.lectionsFacade.edit(next);

                    } catch (NonexistentEntityException ex) {
                        System.out.println("Ошибка сохранение ямени файла в БД(NonexistentEntityException) ID=" + next.getId());
                        Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        System.out.println("Ошибка сохранение ямени файла в БД(Общая) ID=" + next.getId());

                        Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {

                System.out.println("Выбранна докачка ID=" + next.getId());

                //если уже загружали но не загрузили
                File file = new File(this.downloadBasePath, next.getAudiofile());
                if (next.isErrordownloaded() != null ? next.isErrordownloaded() : false) {
                    System.out.println("Докачка файла с ошибкой, удаление файла ID=" + next.getId());

                    file.delete();
                    try {
                        next.setErrordownloaded(false);//будет далее индикатором начавшейся загрузки
                        ps.lectionsFacade.edit(next);

                    } catch (NonexistentEntityException ex) {
                        System.out.println("Ошибка сохранениЯ статуса Eror файла в БД(NonexistentEntityException) ID=" + next.getId());

                        Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (Exception ex) {
                        System.out.println("Ошибка сохранениЯ статуса Eror файла в БД(Общая) ID=" + next.getId());

                        Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    ld = new ExtendedDownloader(url, file, true, (Object) next.getId());//начнем заново
                } else ld = new ExtendedDownloader(url, file, false, (Object) next.getId());//продолжим загрузку


            }

            if (ld != null) {
                System.out.println("Запуск скачивания ID=" + next.getId());

                ld.addObserver((Observer) this);
                filesForDownload.add(ld);
                filesDownloaded.add(next);
                ld.startDownload();

                this.countDownloadingThread.setValue(this.countDownloadingThread.getValue() + 1);

                System.out.println("Запущенна ID=" + next.getId());

            } else System.out.println("Игнорируется, не создан объект загрузки ID=" + next.getId());


        }


    }

    @Override
    public void update(Observable o, Object arg) {

        synchronized (this) {
            if (o instanceof ExtendedDownloader) {
                Lections find = null;
                ExtendedDownloader ed = (ExtendedDownloader) o;
                int status = ed.getStatus();
                switch (status) {
                    case ExtendedDownloader.COMPLETE:
                        System.out.println("Статус закачки - Успех. Обновляем БД ID=" + (long) ed.getParam());

                        find = ps.lectionsFacade.find((long) ed.getParam());
                        if (find != null) {
                            try {
                                find.setDownloaded(true);
                                find.setErrordownloaded(false);
                                System.out.println("БД обновлена Успех. ID=" + (long) ed.getParam());
                                this.filesForDownload.remove(ed);
                                Lections sl = null;
                                for (Lections l : filesDownloaded) {
                                    if (l.getId().equals(find.getId())) {
                                        sl = l;
                                        break;
                                    }
                                }
                                filesDownloaded.remove(sl);
                                ps.lectionsFacade.edit(find);

                                System.out.println("Статус закачки - Успех. БД обновленна ID=" + (long) ed.getParam() + "\n\n");


                            } catch (NonexistentEntityException ex) {
                                System.out.println("Ошибка обновления БД(NonexistentEntityException). ID=" + (long) ed.getParam());
                                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                System.out.println("Ошибка обновления БД(общая). ID=" + (long) ed.getParam());
                                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                this.countDownloadingThread.setValue(this.countDownloadingThread.getValue() - 1);


                            }


                        } else {
                            System.out.println("не найдена сущность в БД, не сохранен статус(статус успеха) ID=" + (long) ed.getParam());
                        }
                        break;
                    case ExtendedDownloader.BREAKINGLINK:
                    case ExtendedDownloader.ERROR:
                        System.out.println("Статус закачки - Ошибка. Обновляем БД  ID=" + (long) ed.getParam());
                        find = ps.lectionsFacade.find((long) ed.getParam());


                        if (find != null) {
                            Lections sl = null;
                            for (Lections l : filesDownloaded) {
                                if (l.getId().equals(find.getId())) {
                                    sl = l;
                                    break;
                                }
                            }

                            if (sl == null) break;//обрубим попытку повторно обработать неверный статус.

                            try {
                                find.setDownloaded(false);
                                find.setErrordownloaded(true);
                                //битая ссылка на лекцию, при этом интернет должен быть, если нет то просто ошибка
                                if (status == ExtendedDownloader.BREAKINGLINK && this.isInet == true) {

                                    find.setBreakinglink(true);
                                    find.setAudiofile("");//обнулим имя файла
                                    ed.getFile().delete();//удалим файл
                                }
                                this.filesForDownload.remove(ed);

                                sl = null;
                                for (Lections l : filesDownloaded) {
                                    if (l.getId().equals(find.getId())) {
                                        sl = l;
                                        break;
                                    }
                                }
                                filesDownloaded.remove(sl);


                                ps.lectionsFacade.edit(find);

                                System.out.println("Статус закачки - Ошибка. БД обновленна ID=" + (long) ed.getParam());
                            } catch (NonexistentEntityException ex) {
                                System.out.println("Ошибка обновления БД(NonexistentEntityException) - err. ID=" + (long) ed.getParam());
                                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (Exception ex) {
                                System.out.println("Ошибка обновления БД(NonexistentEntityException) -err. ID=" + (long) ed.getParam());
                                Logger.getLogger(DownloadService.class.getName()).log(Level.SEVERE, null, ex);
                            } finally {
                                this.countDownloadingThread.setValue(this.countDownloadingThread.getValue() - 1);
                            }


                        } else {
                            System.out.println("не найдена сущность в БД, не сохранен статус (статус ошибки) ID=" + (long) ed.getParam());
                        }

                        break;


                }


            }
        }
    }


}

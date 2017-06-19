package org.anantacreative.updater.Update.XML;

import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
import org.anantacreative.updater.Update.UpdateTask;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.URL;

/**
 * Реализует процесс получения UpdateTask путем парсинга xml-файла
 */
public class XmlUpdateTaskCreator extends AbstractUpdateTaskCreator {
    private File file;
    private URL fileURL;


    /**
     * @param downloadsPath путь к директории загрузки относительно rootDirApp
     * @param rootDirApp   корневая директория приложения. От нее устанавливаются dst пути файлов и экшенов
     * @param listener     слушатель событий создания UpdateTask
     * @param fileURL      url файла update.xml
     */
    public XmlUpdateTaskCreator(String downloadsPath, File rootDirApp, Listener listener, URL fileURL) {
        super(downloadsPath, listener, rootDirApp);
        this.fileURL = fileURL;
    }

    /**
     * @param downloadsPath путь к директории загрузки относительно rootDirApp
     * @param rootDirApp   корневая директория приложения. От нее устанавливаются dst пути файлов и экшенов
     * @param listener     слушатель событий создания UpdateTask
     * @param file         файл update.xml
     */
    public XmlUpdateTaskCreator(String downloadsPath, File rootDirApp, Listener listener, File file) {
        super(downloadsPath, listener, rootDirApp);
        this.file = file;
    }




    @Override
    protected UpdateTask buildUpdateTask(File downloadsDir) throws CreateUpdateTaskError {
        XmlUpdateFileParser up;
        UpdateTask task;
        try {
            if (file != null) {
                up = new XmlUpdateFileParser(file, getRootDirApp());
            } else {
                up = new XmlUpdateFileParser(fileURL, getRootDirApp());
            }
            task = up.parse();


        } catch (SAXException e) {
            throw new CreateUpdateTaskError(e);
        } catch (Exception e) {
            throw new CreateUpdateTaskError(e);
        }
        return task;
    }
}

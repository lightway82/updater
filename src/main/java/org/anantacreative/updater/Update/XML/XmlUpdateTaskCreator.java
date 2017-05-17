package org.anantacreative.updater.Update.XML;

import org.anantacreative.updater.Downloader.DownloadingTask;
import org.anantacreative.updater.Update.AbstractUpdateTaskCreator;
import org.anantacreative.updater.Update.UpdateTask;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализует процесс получения UpdateTask путем парсинга xml-файла
 */
public class XmlUpdateTaskCreator extends AbstractUpdateTaskCreator {
    private File file;
    private URL fileURL;
    private UpdateTask task;

    /**
     * @param downloadsDir папка загрузки
     * @param rootDirApp   корневая директория приложения. От нее устанавливаются dst пути файлов и экшенов
     * @param listener     слушатель событий создания UpdateTask
     * @param fileURL      url файла update.xml
     */
    public XmlUpdateTaskCreator(File downloadsDir, File rootDirApp, Listener listener, URL fileURL) {
        super(downloadsDir, listener, rootDirApp);
        this.fileURL = fileURL;
    }

    /**
     * @param downloadsDir папка загрузки
     * @param rootDirApp   корневая директория приложения. От нее устанавливаются dst пути файлов и экшенов
     * @param listener     слушатель событий создания UpdateTask
     * @param file         файл update.xml
     */
    public XmlUpdateTaskCreator(File downloadsDir, File rootDirApp, Listener listener, File file) {
        super(downloadsDir, listener, rootDirApp);
        this.file = file;
    }


    @Override
    public List<DownloadingTask.DownloadingItem> getDownloadingFiles() throws GetUpdateFilesError {
        XmlUpdateFileParser up;
        List<DownloadingTask.DownloadingItem> res = new ArrayList<>();
        try {
            if (file != null) {
                up = new XmlUpdateFileParser(file, getRootDirApp());
            } else {
                up = new XmlUpdateFileParser(fileURL, getRootDirApp());
            }
            task = up.parse();
            res.addAll(task.getDownloadingFiles().stream()
                           .map(f -> new DownloadingTask.DownloadingItem(f.getUrl(), new File(getDownloadsDir(),extractFileNameFromUrl(f.getUrl()))))
                           .collect(Collectors.toList()));
            return res;
        } catch (SAXException e) {
            throw new GetUpdateFilesError(e);
        } catch (Exception e) {
            throw new GetUpdateFilesError(e);
        }
    }

    @Override
    public UpdateTask buildUpdateTask(File downloadsDir) throws CreateUpdateTaskError {
        return task;
    }
}

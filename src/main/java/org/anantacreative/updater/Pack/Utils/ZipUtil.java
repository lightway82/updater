package org.anantacreative.updater.Pack.Utils;

import org.anantacreative.updater.Pack.Exceptions.PackException;
import org.anantacreative.updater.Pack.Exceptions.UnPackException;

import java.io.*;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class ZipUtil {

    /**
     * Запаковать директорию в архив(файлы и директории указанной директории)
     *
     * @param dir     путь к папке, содержимое которой упаковывается
     * @param dstArch путь к файлу нового архива
     */
    public static void zipDir(File dir, File dstArch) throws PackException {


        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dstArch))) {

            doZipDir(dir, dir.getPath(), out);

        } catch (Exception e) {
            throw new PackException(e);
        }


    }

    /**
     * * Файлы и директории в списке должны быть из одной директории иначе при распаковке будут лишние пути
     * Также устроен обычный архиватор, он отказывается паковать файлы из разных директорий
     * @param files список файлов и директорий из одной общей директории!!!!
     * @param arch конечный архив
     * @throws PackException
     */
    public static void zipFiles(List<File> files, File arch) throws PackException {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arch))) {

            doZipFiles(files, out);

        } catch (Exception e) {
            throw new PackException(e);
        }
    }

    /**
     * Упаковывает в архив один файл
     * @param  f файл
     * @param arch  конечный архив
     * @throws PackException
     */
    public static void zipFile(File f, File arch) throws PackException {
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(arch))) {

            doZipFile(f,f.getParentFile().getPath(), out);

        } catch (Exception e) {
            throw new PackException(e);
        }
    }


    /**
     * Распаковка Zip архива в указанную директорию
     * @param zipArch
     * @param dstFolder
     * @throws UnPackException
     */
    public static void unZip(File zipArch, File dstFolder) throws UnPackException {
        boolean res = true;

        if (!zipArch.exists() || !zipArch.canRead()) {

            throw new UnPackException("Archive not exist or can`t read");
        }

        if (!dstFolder.exists())
            if (!dstFolder.mkdirs()) throw new UnPackException("Destination directory not exists or cant`t created");

        ZipFile zip = null;
        try {
            zip = new ZipFile(zipArch);
            Enumeration entries = zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();

                if (entry.isDirectory()) {
                    //здесь нужно в имени поправить слэши, тк если архив создан на винде, вместо папки будет файл с слешами в названии
                    new File(dstFolder, entry.getName()).mkdirs();
                } else {

                    int i = entry.getName().lastIndexOf(File.separator);
                    if (i != -1) {
                        new File(dstFolder, entry.getName().substring(0, i)).mkdirs();
                    }
                    writeUnZip(zip.getInputStream(entry),
                            new BufferedOutputStream(new FileOutputStream(new File(dstFolder, entry.getName()))));
                }
            }

        } catch (Exception e) {
            throw new UnPackException(e);
        } finally {

            if (zip != null) try {
                zip.close();
            } catch (IOException e) {
                throw new UnPackException(e);
            }
        }

    }

    /**
     * Файлы и директории в списке должны быть из одной директории иначе при распаковке будут лишние пути
     * Также устроен обычный архиватор, он отказывается паковать файлы из разных директорий
     * @param files
     * @param out
     * @throws IOException
     */
    private static void doZipFiles(List<File> files, ZipOutputStream out) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) doZipDir(file, file.getParentFile().getPath(), out);
            else doZipFile(file,file.getParentFile().getPath(),out);
        }

    }

    private static void doZipDir(File dir, String commonPath, ZipOutputStream out) throws IOException {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) doZipDir(f,commonPath, out);
            else doZipFile(f,commonPath, out);
        }
    }


    private static void doZipFile(File f, String commonPath, ZipOutputStream out) throws IOException {
        String entryName  = cutEntryNameFromFilePath(f,commonPath );
        out.putNextEntry(new ZipEntry(entryName));
        writeZip(f, out);
    }


    private static String cutEntryNameFromFilePath(File f,String commonPath ){
        String entryName;
        String fPath =f.getPath();
        if(fPath.contains(commonPath)){
            int startIndex = fPath.indexOf(commonPath)+commonPath.length()+1;
            if(startIndex >= fPath.length()) entryName = fPath;
            else entryName = fPath.substring(startIndex);
        }else entryName = fPath;

        return entryName;
    }

    private static void writeZip(File f, OutputStream out) throws IOException {
        try(InputStream in = new FileInputStream(f))
        {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) >= 0) {
                out.write(buffer, 0, len);
            }
        }
    }

    private static void writeUnZip(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) >= 0) {
            out.write(buffer, 0, len);
        }

        out.close();
        in.close();
    }
}

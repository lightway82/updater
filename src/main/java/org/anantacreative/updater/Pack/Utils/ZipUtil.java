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
     * @param dir     путь к папке
     * @param dstArch путь к файлу нового архива
     */
    public static void zipDir(File dir, File dstArch) throws PackException {


        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(dstArch))) {

            doZipDir(dir, out);

        } catch (Exception e) {
            throw new PackException(e);
        }


    }

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

            doZipFile(f,out);

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


    private static void doZipFiles(List<File> files, ZipOutputStream out) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) doZipDir(file, out);
            else doZipFile(file,out);
        }

    }

    private static void doZipDir(File dir, ZipOutputStream out) throws IOException {
        for (File f : dir.listFiles()) {
            if (f.isDirectory()) doZipDir(f, out);
            else doZipFile(f,out);
        }
    }


    private static void doZipFile(File f, ZipOutputStream out) throws IOException {
        out.putNextEntry(new ZipEntry(f.getPath()));
        writeZip(f, out);
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

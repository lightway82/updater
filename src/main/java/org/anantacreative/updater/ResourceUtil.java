package org.anantacreative.updater;

import java.io.*;


/**
 * Сохраняет файл из ресурсов jar в указанную директорию
 */
public class ResourceUtil {


    /**
     * Сохраняет файл из ресурсов jar в указанную директорию
     * @param outputDirectoryPath директория(путь) для сохранения файла
     * @param dstName имя файла, которым будет назван сохраненный файл
     * @param nameRes путь к ресурсу
     * @param replace перезаписать результирующий файл
     * @return сохраненный файл
     * @throws IOException
     */
    public static File saveResource(String outputDirectoryPath, String dstName, String nameRes, boolean replace) throws IOException {
        return saveResource(new File(outputDirectoryPath),dstName,nameRes,replace);
    }

    /**
     * Сохраняет файл из ресурсов jar в указанную директорию
     * @param outputDirectory директория для сохранения файла
     * @param dstName имя файла, которым будет назван сохраненный файл
     * @param nameRes путь к ресурсу
     * @param replace перезаписать результирующий файл
     * @return сохраненный файл
     * @throws IOException
     */
    public static File saveResource(File outputDirectory, String dstName, String nameRes, boolean replace) throws IOException {
        File out = new File(outputDirectory, dstName);
        if (!replace && out.exists()) return out;

        InputStream resource = ResourceUtil.class.getResourceAsStream(nameRes);
        if (resource == null) throw new FileNotFoundException(nameRes + " (resource not found)");

        try (InputStream in = resource; OutputStream writer = new BufferedOutputStream(new FileOutputStream(out))) {
            byte[] buffer = new byte[1024 * 4];
            int length;
            while ((length = in.read(buffer)) >= 0) {
                writer.write(buffer, 0, length);
            }
        }
        return out;
    }
}

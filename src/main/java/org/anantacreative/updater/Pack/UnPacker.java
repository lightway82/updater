package org.anantacreative.updater.Pack;

import org.anantacreative.updater.Pack.Exceptions.UnPackException;
import org.anantacreative.updater.Pack.Utils.ZipUtil;

import java.io.File;

/**
 * Created by anama on 01.06.17.
 */
public class UnPacker {
    /**
     * Раппаковывает архив в указанную директорию
     * @param arch архив
     * @param toDir  директория для распаковки
     * @throws UnPackException
     */
    public static void unPack(File arch, File toDir) throws UnPackException {
        ZipUtil.unZip(arch,toDir);
    }
}

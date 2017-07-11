package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Pack.Packer;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Упаковка файлов и директорий. Важно!!! Файлы и директории для каждого архива должны быть из одной директории. те не в разнобой.
 * Список на упаковку может содержать файлы  и директории в src. В dst путь к архиву в котором они будут размещены.
 * Допускается упаковка файлов в разные архивы, те dst может быть разным для разных  UpdateActionFileItem. Будет создано столько архивов сколько было разных dst
 * Упаковываются директории с содержимым! Для упаковки только содержимого директорий используйте ActionPackDirs
 */
public class ActionPackFiles extends AbstractAction {
    protected ActionPackFiles(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        packPreparedFiles(prepareFiles(files));
    }

    private Map<File, List<File>> prepareFiles(List<UpdateActionFileItem> files) throws UpdateActionException {
        Map<File, List<File>> preparedFiles = new HashMap<>();
        for (UpdateActionFileItem file : files) {
            try {
                if (preparedFiles.containsKey(file.getDstPath()))
                    preparedFiles.get(file.getDstPath()).add(file.getSrcPath());
                else {
                    preparedFiles.put(file.getDstPath(), new ArrayList<>());
                    preparedFiles.get(file.getDstPath()).add(file.getSrcPath());
                }
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(), file, e);
            }
        }
        return preparedFiles;
    }

    private void packPreparedFiles(Map<File, List<File>> preparedFiles) throws UpdateActionException {
        for (Map.Entry<File, List<File>> entry : preparedFiles.entrySet()) {
            List<File> files = entry.getValue();
            File toArchive = entry.getKey();
            try {
                Packer.packFiles(files, toArchive);
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(), UpdateActionFileItem.create().build(), e);
            }
        }

    }
}

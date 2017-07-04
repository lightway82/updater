package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Utilites.FilesUtil;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Перемещение файлов указанных в src в dst(путь к новому файлу или просто директории если не нужно переименовывать)
 * Возможно переименовать сразу файлы, указав другие имена в dst
 * Также перемещение закачаных файлов указанных в URL в dst, при этом src игнорируется
 */
public class ActionMove  extends AbstractAction {
    protected ActionMove(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            try {
                checkAndCreateDstDir(file.getDstPath());
                File src;
                if(file.getUrl()==null)  src=file.getSrcPath();
                else  src=file.getDownloadedFile();

                if(FilesUtil.isFile(file.getDstPath()))  Files.move(src.toPath(),file.getDstPath().toPath(), StandardCopyOption.REPLACE_EXISTING);
                else  Files.move(src.toPath(),new File(file.getDstPath(),file.getDownloadedFile().getName()).toPath(),StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
               throw new UpdateActionException(getActionType(),file,e);
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(),file,e);
            }
        }


    }
}

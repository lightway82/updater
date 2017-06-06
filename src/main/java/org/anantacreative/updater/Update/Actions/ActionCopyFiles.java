package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.FilesUtil;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 * Копирование файлов в директории или в файлы
 */
public class ActionCopyFiles  extends AbstractAction {
    protected ActionCopyFiles(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {

        for (UpdateActionFileItem file : files) {
            try {
                checkAndCreateDstDir(file.getDstPath());
                if(FilesUtil.isDirectory(file.getDstPath()))FilesUtil.copyFileToDir(file.getSrcPath(),file.getDstPath());
                else FilesUtil.copyFileToFile(file.getSrcPath(),file.getDstPath());

            }catch (Exception e){
                throw new UpdateActionException(getActionType(),file,e);
            }

        }

    }
}

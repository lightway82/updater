package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.FilesUtil;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 * Копировать директорию внутрь указанной директории
 */
public class ActionCopyDirs extends AbstractAction {
    public ActionCopyDirs(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            try {
                checkAndCreateDstDir(file.getDstPath());
                if(FilesUtil.isFile(file.getDstPath())) throw new Exception("Destination path must be directory");
                FilesUtil.copyDirToDir(file.getSrcPath(),file.getDstPath());
            }catch (Exception e){
                throw new UpdateActionException(getActionType(),file,e);
            }
        }
    }
}

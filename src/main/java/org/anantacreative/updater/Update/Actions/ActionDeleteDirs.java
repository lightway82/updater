package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.FilesUtil;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 * Удаление директории
 */
public class ActionDeleteDirs extends AbstractAction {
    protected ActionDeleteDirs(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> dirs)  throws UpdateActionException {
            for (UpdateActionFileItem dir : dirs) deleteDir(dir);
    }


private void deleteDir(UpdateActionFileItem dir) throws UpdateActionException {
    try {
        FilesUtil.recursiveDelete(dir.getSrcPath());
    } catch (Exception e) {
        throw new UpdateActionException(getActionType(),dir,e);
    }
}

}
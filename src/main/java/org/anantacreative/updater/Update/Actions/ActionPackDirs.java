package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Pack.Packer;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 *Упаковывает СОДЕРЖИМОЕ директорий src, каждую в свой архив dst.
 */
public class ActionPackDirs extends AbstractAction {
    protected ActionPackDirs(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for ( UpdateActionFileItem file : files) {
            try {
                Packer.packDir(file.getSrcPath(), file.getDstPath());
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(), UpdateActionFileItem.create().build(), e);
            }
        }
    }


}

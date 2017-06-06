package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Pack.Exceptions.UnPackException;
import org.anantacreative.updater.Pack.UnPacker;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 * Распаковка архивов из src в dst директории
 */
public class ActionUnpack  extends AbstractAction {
    public ActionUnpack(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            try {
                UnPacker.unPack(file.getSrcPath(),file.getDstPath());
            } catch (UnPackException e) {
                throw new UpdateActionException(getActionType(),file,"Unpack file error!",e);
            }
        }

    }
}

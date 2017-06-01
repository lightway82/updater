package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 *
 */
public class ActionPack extends AbstractAction{
    public ActionPack(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) {

    }
}

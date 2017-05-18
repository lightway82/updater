package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 *
 */
public class ActionRun  extends AbstractAction {
    public ActionRun(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) {

    }
}

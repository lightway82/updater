package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;

/**
 * Просто загрузка файла(url) в указанную директорию или просто в директорию загрузки если не указан dst
 */
public class ActionDownload extends AbstractAction {
    protected ActionDownload(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {

    }
}

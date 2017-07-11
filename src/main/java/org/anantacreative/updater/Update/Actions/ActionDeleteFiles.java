package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Utilites.FilesUtil;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.List;
import java.util.stream.Collectors;

/**
 *Удаление списка файлов
 */
public class ActionDeleteFiles  extends AbstractAction {
    protected ActionDeleteFiles(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        try {
            FilesUtil.deleteFiles(files.stream().map(file->file.getSrcPath()).collect(Collectors.toList()));
        } catch (Exception e) {
            throw new UpdateActionException(getActionType(), UpdateActionFileItem.create().build(),e);
        }

    }
}

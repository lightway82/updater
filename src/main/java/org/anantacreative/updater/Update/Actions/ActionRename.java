package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Переименование файла из src в dst. Оба параметра пути от корня программы.
 * При заддании другой директории будет перемещение файла с переименованием.
 *  Можно переименовывать директории
 */
public class ActionRename  extends AbstractAction {
    protected ActionRename(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            try {

                Files.move(file.getSrcPath().toPath(),file.getDstPath().toPath());
            } catch (IOException e) {
                throw new UpdateActionException(getActionType(),file,e);
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(),file,e);
            }
        }
    }
}

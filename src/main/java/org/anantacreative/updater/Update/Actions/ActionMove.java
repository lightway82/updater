package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/**
 * Перемещение файлов указанных в src в dst(путь к новому файлу, а не просто директории)
 * Возможно переименовать сразу файлы, указав другие имена в dst
 * Также перемещение закачаных файлов указанных в URL в dst, при этом src игнорируется
 */
public class ActionMove  extends AbstractAction {
    public ActionMove(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            try {
                checkAndCreateDstDir(file.getDstPath());
                if(file.getUrl()!=null){
                    Files.move(file.getSrcPath().toPath(),file.getDstPath().toPath());
                }else {
                    Files.move(file.getDownloadedFile().toPath(),file.getDstPath().toPath());
                }

            } catch (IOException e) {
               throw new UpdateActionException(getActionType(),file,e);
            } catch (Exception e) {
                throw new UpdateActionException(getActionType(),file,e);
            }
        }


    }
}

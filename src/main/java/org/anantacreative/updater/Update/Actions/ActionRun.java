package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Utilites.JarExecutor;
import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateActionException;
import org.anantacreative.updater.Update.UpdateActionFileItem;

import java.util.Collections;
import java.util.List;

/**
 * Запуск jar файлов закачаных по указанному URL или по пути src (url будет игнорироваться)
 */
public class ActionRun  extends AbstractAction {
    protected ActionRun(ActionType actionType) {
        super(actionType);
    }

    @Override
    public void execute(List<UpdateActionFileItem> files) throws UpdateActionException {
        for (UpdateActionFileItem file : files) {
            JarExecutor je;
            if(file.getSrcPath()!=null) je = new JarExecutor(file.getSrcPath().getAbsolutePath(), Collections.emptyList());
            else if(file.getDownloadedFile().exists()) je = new JarExecutor(file.getDownloadedFile().getAbsolutePath(), Collections.emptyList());
            else   throw new UpdateActionException(getActionType(),file,new Exception("Source file or url not found"));
            try {
                je.execute(true);
            } catch (JarExecutor.JarExecutorException e) {
                throw new UpdateActionException(getActionType(),file,e);
            }
        }

    }
}

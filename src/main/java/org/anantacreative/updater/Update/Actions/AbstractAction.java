package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.ActionType;
import org.anantacreative.updater.Update.UpdateAction;

import java.io.File;

/**
 * Базовый класс для Actions
 */
public abstract class AbstractAction implements UpdateAction {
    private ActionType actionType;

    public AbstractAction(ActionType actionType) {
        this.actionType = actionType;
    }

    public ActionType getActionType() {
        return actionType;
    }

    /**
     * Проверяет наличие директории для файла dstFile(файла, а не директория), если ее нет, то создаст
     * @param dstFile
     */
    public void checkAndCreateDstDir(File dstFile) throws Exception {
        File dstDir;

        if (dstFile.isDirectory()) dstDir = dstFile.getParentFile();
        else dstDir = dstFile;

        if (!dstDir.exists()) {
            if (dstDir.mkdirs() == false)
                throw new Exception("Directory created with error; " + dstDir.getAbsolutePath());
        }
    }
}

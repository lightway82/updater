package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.UpdateAction;

/**
 * Типы экшенов обновления
 */
public enum ActionType {

    MOVE("Move", ActionMove.class),
    RENAME("Rename", ActionRename.class),
    DELETE_DIRS("DeleteDirs", ActionDeleteDirs.class),
    DELETE_FILES("DeleteFiles", ActionDeleteFiles.class),
    UNPACK("Unpack", ActionUnpack.class),
    PACK_FILES("PackFiles",ActionPackFiles.class),
    PACK_DIRS("PackDirs",ActionPackDirs.class),
    RUN("Run",ActionRun.class),
    COPY_FILES("CopyFiles",ActionCopyFiles.class),
    COPY_DIRS("CopyDirs",ActionCopyDirs.class),
    DOWNLOAD("Download",ActionDownload.class),
    UNKNOWN("", null);

    private String typeName;
    private Class<? extends UpdateAction> updateActionClass;

    ActionType(String typeName, Class<? extends UpdateAction> updateActionClass) {
        this.typeName = typeName;
        this.updateActionClass = updateActionClass;
    }

    protected Class<? extends UpdateAction> getUpdateActionClass() {
        return updateActionClass;
    }

    public String getTypeName() {
        return typeName;
    }

    public static ActionType getType(String type) {

            for(ActionType at : ActionType.values()) if(at.getTypeName().equals(type)) return at;
            return UNKNOWN;


    }
}

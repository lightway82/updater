package org.anantacreative.updater.Update;

/**
 * Типы экшенов обновления
 */
public enum ActionType {

    MOVE("Move"),
    RENAME("Rename"),
    DELETE_DIRS("DeleteDirs"),
    DELETE_FILES("DeleteFiles"),
    UNPACK("Unpack"),
    PACK_FILES("PackFiles"),
    PACK_DIRS("PackDirs"),
    RUN("Run"),
    COPY_FILES("CopyFiles"),
    COPY_DIRS("CopyDirs"),
    UNKNOWN("");

    private String typeName;

    ActionType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }

    public static ActionType getType(String type) {

            for(ActionType at : ActionType.values()) if(at.getTypeName().equals(type)) return at;
            return UNKNOWN;


    }
}

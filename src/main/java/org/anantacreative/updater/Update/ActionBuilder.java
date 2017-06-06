package org.anantacreative.updater.Update;

import org.anantacreative.updater.Update.Actions.*;

/**
 *
 */
public class ActionBuilder {

    public static UpdateAction build(String type) throws UnknownActionError {
        return build(ActionType.getType(type));
    }

    public static UpdateAction build(ActionType type) throws UnknownActionError {
        if (type == ActionType.UNKNOWN) throw new UnknownActionError();
        UpdateAction action;
        switch (type) {
            case COPY_DIRS:
                action = new ActionCopyDirs(ActionType.COPY_DIRS);
                break;
            case COPY_FILES:
                action = new ActionCopyFiles(ActionType.COPY_FILES);
                break;
            case DELETE_DIRS:
                action = new ActionDeleteDirs(ActionType.DELETE_DIRS);
                break;
            case DELETE_FILES:
                action = new ActionDeleteFiles(ActionType.DELETE_FILES);
                break;
            case MOVE:
                action = new ActionMove(ActionType.MOVE);
                break;
            case PACK_FILES:
                action = new ActionPackFiles(ActionType.PACK_FILES);
                break;
            case PACK_DIRS:
                action = new ActionPackDirs(ActionType.PACK_DIRS);
                break;
            case RENAME:
                action = new ActionRename(ActionType.RENAME);
                break;
            case RUN:
                action = new ActionRun(ActionType.RUN);
                break;

            case UNPACK:
                action = new ActionUnpack(ActionType.UNPACK);
                break;
            default:
                throw new UnknownActionError();

        }
        return action;
    }


    /**
     * Неизвестный Action
     */
    public static class UnknownActionError extends Exception {
        public UnknownActionError() {
            super();
        }
    }
}

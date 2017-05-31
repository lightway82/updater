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
            case COPY_DIR:
                action = new ActionCopyDir(ActionType.COPY_DIR);
                break;
            case COPY_FILES:
                action = new ActionCopyFiles(ActionType.COPY_FILES);
                break;
            case DELETE_DIR:
                action = new ActionDeleteDir(ActionType.DELETE_DIR);
                break;
            case DELETE_FILES:
                action = new ActionDeleteFiles(ActionType.DELETE_FILES);
                break;
            case MOVE:
                action = new ActionMove(ActionType.MOVE);
                break;
            case PACK_DIR:
                action = new ActionPackDir(ActionType.PACK_DIR);
                break;
            case PACK_FILES:
                action = new ActionPackFiles(ActionType.PACK_FILES);
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

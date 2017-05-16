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
                action = new ActionCopyDir();
                break;
            case COPY_FILES:
                action = new ActionCopyFiles();
                break;
            case DELETE_DIR:
                action = new ActionDeleteDir();
                break;
            case DELETE_FILES:
                action = new ActionDeleteFiles();
                break;
            case MOVE:
                action = new ActionMove();
                break;
            case MOVE_IN:
                action = new ActionMoveIn();
                break;
            case PACK_DIR:
                action = new ActionPackDir();
                break;
            case PACK_FILES:
                action = new ActionPackFiles();
                break;
            case RENAME:
                action = new ActionRename();
                break;
            case RUN:
                action = new ActionRun();
                break;
            case RUN_IN:
                action = new ActionRunIn();
                break;
            case UNPACK:
                action = new ActionUnpack();
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

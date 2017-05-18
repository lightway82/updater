package org.anantacreative.updater.Update;

/**
 * Ошибка операции обновления
 */
public class UpdateActionException extends Exception {

    private ActionType actionType;
    private String fileItem;

    public UpdateActionException(ActionType actionType, UpdateActionFileItem item, String message, Throwable cause) {
        super(message, cause);
        this.actionType = actionType;
        fileItem=item.toString();
    }

    public UpdateActionException(ActionType actionType,UpdateActionFileItem item, Throwable cause) {
        super(cause);
        this.actionType = actionType;
        fileItem=item.toString();
    }

    @Override
    public void printStackTrace() {
        System.err.println("Action Type = "+actionType);
        System.err.println("File item = "+fileItem);
        super.printStackTrace();
    }

}

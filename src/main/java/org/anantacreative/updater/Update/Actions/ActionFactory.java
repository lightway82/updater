package org.anantacreative.updater.Update.Actions;

import org.anantacreative.updater.Update.UpdateAction;

import java.lang.reflect.Constructor;

/**
 *
 */
public class ActionFactory {

    public static UpdateAction build(String type) throws UnknownActionError {
        return build(ActionType.getType(type));
    }

    public static UpdateAction build(ActionType type) throws UnknownActionError {
        if (type == ActionType.UNKNOWN) throw new UnknownActionError();
        UpdateAction action;

        try {
            Constructor<? extends UpdateAction> constructor = type.getUpdateActionClass().getDeclaredConstructor(ActionType.class);
            action =  constructor.newInstance(type);

        } catch (Exception e) {
        throw new RuntimeException(e);
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

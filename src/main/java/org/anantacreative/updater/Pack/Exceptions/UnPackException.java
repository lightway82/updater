package org.anantacreative.updater.Pack.Exceptions;

/**
 * Created by anama on 01.06.17.
 */
public class UnPackException extends Exception {
    public UnPackException(String message) {
        super(message);
    }

    public UnPackException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnPackException(Throwable cause) {
        super(cause);
    }
}

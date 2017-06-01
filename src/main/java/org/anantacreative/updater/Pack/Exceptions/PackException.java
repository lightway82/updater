package org.anantacreative.updater.Pack.Exceptions;

/**
 * Created by anama on 01.06.17.
 */
public class PackException extends Exception {
    public PackException(String message) {
        super(message);
    }

    public PackException(String message, Throwable cause) {
        super(message, cause);
    }

    public PackException(Throwable cause) {
        super(cause);
    }
}

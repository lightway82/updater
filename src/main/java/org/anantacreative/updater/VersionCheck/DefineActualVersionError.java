package org.anantacreative.updater.VersionCheck;

public class DefineActualVersionError extends Exception {
    public DefineActualVersionError(String message) {
        super(message);
    }

    public DefineActualVersionError(String message, Throwable cause) {
        super(message, cause);
    }
}
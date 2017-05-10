package org.anantacreative.updater;

/**
 * Абстрактный класс проверки соответствия версии программы и версии для обновления
 * В подклассах следует реализовать  getVersionForUpdate() согласно своей стратегии.
 * Библиотека содержит несколько готовых реализаций
 */
public abstract class AbstractVersionChecker {

    private final Version currentVersion;
    private Version actualVersion;

    public AbstractVersionChecker(Version currentVersion) {
        this.currentVersion = currentVersion;
    }

    public Version getActualVersion() {
        return actualVersion;
    }

    public boolean checkNeedUpdate() throws DefineActualVersionError {
        actualVersion = getVersionForUpdate();
        if (actualVersion == null) throw new DefineActualVersionError("");
        return currentVersion.lessThen(actualVersion);
    }

    abstract Version getVersionForUpdate() throws DefineActualVersionError;


    public static class DefineActualVersionError extends Exception {
        public DefineActualVersionError(String message) {
            super(message);
        }

        public DefineActualVersionError(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

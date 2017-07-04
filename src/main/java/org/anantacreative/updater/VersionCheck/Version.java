package org.anantacreative.updater.VersionCheck;

/**
 * Хранит версию
 */
public class Version implements Comparable<Version> {
    private int major;
    private int minor;
    private int fix;

    private String vString;
    private static final String delimiter = ".";

    public Version(int major, int minor, int fix) {
        this.major = major;
        this.minor = minor;
        this.fix = fix;
        StringBuilder strb = new StringBuilder()
                .append(major).append(delimiter)
                .append(minor).append(delimiter)
                .append(fix);
        vString=strb.toString();
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getFix() {
        return fix;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (major != version.major) return false;
        if (minor != version.minor) return false;
        return fix == version.fix;
    }

    @Override
    public int hashCode() {
        int result = major;
        result = 31 * result + minor;
        result = 31 * result + fix;
        return result;
    }

    @Override
    public String toString() {
        return vString;
    }

    @Override
    public int compareTo(Version o) {
        if (equals(o)) return 0;

        if (major < o.getMajor()) return -1;
        else if (major > o.getMajor()) return 1;

        if (minor < o.getMinor()) return -1;
        else if (minor > o.getMinor()) return 1;

        if (fix < o.getFix()) return -1;
        else if (fix > o.getFix()) return 1;

        return 0;
    }

    public boolean greaterOrEqualThen(Version o) {
        return compareTo(o) >= 0;
    }

    public boolean lessThen(Version o) {
        return compareTo(o) < 0;
    }
}

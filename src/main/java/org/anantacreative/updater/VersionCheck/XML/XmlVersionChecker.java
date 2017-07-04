package org.anantacreative.updater.VersionCheck.XML;

import org.anantacreative.updater.VersionCheck.Version;
import org.anantacreative.updater.VersionCheck.AbstractVersionChecker;
import org.anantacreative.updater.VersionCheck.DefineActualVersionError;
import org.xml.sax.SAXException;

import java.net.URL;

/**
 * Реализует проверку версии на основе парсинга удаленного xml файла
 */
public class XmlVersionChecker extends AbstractVersionChecker {
    private final URL versionFile;

    public XmlVersionChecker(Version currentVersion, URL versionFile) {
        super(currentVersion);
        this.versionFile = versionFile;
    }

    @Override
    protected Version getVersionForUpdate() throws DefineActualVersionError {

        try {
            XmlVersionParser versionParser = new XmlVersionParser(versionFile);
            return versionParser.parse();

        } catch (SAXException e) {
            throw new DefineActualVersionError("Version check error", e);
        }

    }


}

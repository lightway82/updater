package org.anantacreative.updater;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler for {@link XmlVersionParser}
 */
public class XmlVersionParserHandler extends DefaultHandler {
    private Version version;

    @Override
    public void startElement(String uri, String name, String qName, Attributes attrs) throws XmlVersionParser.FileTypeMissMatch {

        if (!qName.equals("Version")) throw new XmlVersionParser.FileTypeMissMatch("File don`t has 'Version' tag.");
        else if (attrs.getLength() == 0) throw new XmlVersionParser.FileTypeMissMatch("Attributes is absent");

        String major = attrs.getValue("major");
        String minor = attrs.getValue("minor");
        String fix = attrs.getValue("fix");
        if (major == null || minor == null || fix == null)
            throw new XmlVersionParser.FileTypeMissMatch("One or more attributes is absent");

        major = major.replaceAll("[^0-9]", "");
        minor = minor.replaceAll("[^0-9]", "");
        fix = fix.replaceAll("[^0-9]", "");

        if (major.isEmpty() || minor.isEmpty() || fix.isEmpty())
            throw new XmlVersionParser.FileTypeMissMatch("Attributes not correct");

        version = new Version(Integer.parseInt(major), Integer.parseInt(minor), Integer.parseInt(fix));


    }

    public Version getVersion() {
        return version;
    }

}

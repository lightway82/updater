package org.anantacreative.updater;

import org.xml.sax.SAXException;

/**
 * Тип XML не соответствует парсеру
 */
public class FileTypeMissMatch extends SAXException {
    public FileTypeMissMatch() {
    }

    public FileTypeMissMatch(String message) {
        super(message);
    }

    public FileTypeMissMatch(Exception e) {
        super(e);
    }

    public FileTypeMissMatch(String message, Exception e) {
        super(message, e);
    }
}


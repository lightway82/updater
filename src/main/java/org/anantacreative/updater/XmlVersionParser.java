package org.anantacreative.updater;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Парсит файл xml с данными о версии
 * Корневой тег Version c атрибутами major, minor, fix
 */
public class XmlVersionParser {

    private URL xmlURL;
    private File xmlFile;
    private XMLReader reader;
    private XmlVersionParserHandler handler;

    public XmlVersionParser(URL xmlURL) throws SAXException {
        super();
        this.xmlURL = xmlURL;
        reader = XMLReaderFactory.createXMLReader();
        handler = new XmlVersionParserHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
    }


    public XmlVersionParser(File xmlFile) throws SAXException {
        super();
        this.xmlFile = xmlFile;
        reader = XMLReaderFactory.createXMLReader();
        handler = new XmlVersionParserHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
    }

    public Version parse() throws FileTypeMissMatch {
        try {
            if (xmlFile != null) {
                reader.parse(new InputSource(new FileReader(xmlFile)));
                return handler.getVersion();
            } else {

                URLConnection conn = xmlURL.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(30000);
                InputStream in = conn.getInputStream();
                reader.parse(new InputSource(in));
                return handler.getVersion();
            }

        } catch (FileNotFoundException e) {
            throw new FileTypeMissMatch("File not found!", e);
        } catch (IOException e) {
            throw new FileTypeMissMatch("Connection error!", e);
        } catch (SAXException e) {
            throw new FileTypeMissMatch("File parse error!", e);
        }
    }


    /**
     * Тип XML не соответствует парсеру
     */
    public static class FileTypeMissMatch extends SAXException {
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
}
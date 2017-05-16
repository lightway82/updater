package org.anantacreative.updater.Update.XML;

import org.anantacreative.updater.FileTypeMissMatch;
import org.anantacreative.updater.Update.UpdateTask;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Парсер xml файла со списком файлов на скачивание
 */
public class XmlUpdateFileParser {

    private URL xmlURL;
    private File rootDirApp;
    private File xmlFile;
    private XMLReader reader;
    private XmlUpdateFileHandler handler;

    public XmlUpdateFileParser(URL xmlURL, File rootDirApp) throws SAXException {
        super();
        this.xmlURL = xmlURL;
        this.rootDirApp = rootDirApp;
        initParser();
    }


    public XmlUpdateFileParser(File xmlFile, File rootDirApp) throws SAXException {
        super();
        this.xmlFile = xmlFile;
        this.rootDirApp = rootDirApp;
        initParser();
    }

    private void initParser() throws SAXException {
        reader = XMLReaderFactory.createXMLReader();
        handler = new XmlUpdateFileHandler(rootDirApp);
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);
    }


    public UpdateTask parse() throws FileTypeMissMatch {
        try {
            if (xmlFile != null) {
                reader.parse(new InputSource(new FileReader(xmlFile)));
                return handler.getTask();
            } else {

                URLConnection conn = xmlURL.openConnection();
                conn.setConnectTimeout(20000);
                conn.setReadTimeout(30000);
                InputStream in = conn.getInputStream();
                reader.parse(new InputSource(in));
                return handler.getTask();
            }

        } catch (FileNotFoundException e) {
            throw new FileTypeMissMatch("File not found!", e);
        } catch (IOException e) {
            throw new FileTypeMissMatch("Connection error!", e);
        } catch (SAXException e) {
            throw new FileTypeMissMatch("File parse error!", e);
        }
    }


}

package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.Update.XML.XmlUpdateFileParser;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.fail;

/**
 *
 */
public class XmlUpdaterParserTest {
    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }
    @Test
    public void test(){
        try {
            XmlUpdateFileParser parser = new XmlUpdateFileParser(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/update_parser_check.xml"),
                    new File("./"));

            UpdateTask task = parser.parse();

        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }

    }
}

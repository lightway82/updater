package org.anantacreative.updater.tests;

import org.anantacreative.updater.Version;
import org.anantacreative.updater.VersionCheck.XML.XmlVersionParser;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.SAXException;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

/**
 *
 */

public class XmlVersionParserTest {
    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }

    @Test(groups = {"common"})
    public void versionCheck(){
        Version v1=new Version(1,2,3);
        Version v2=new Version(1,2,3);
        assertTrue("Version must be equals",v1.equals(v2));

        Version v3=new Version(1,2,5);
        assertTrue("Version must be not equals",!v1.equals(v3));
    }


    @Test(dependsOnMethods = "versionCheck",groups = {"common"})
    public void parseCheck(){
        try {

            XmlVersionParser parser=new XmlVersionParser(new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"));
            Version version = parser.parse();
            Version vTest=new Version(1,2,3);
            assertTrue("Parsing version.xml is error",vTest.equals(version));

        } catch (SAXException e) {
            e.printStackTrace();
            fail();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        }

    }
}

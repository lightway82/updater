package org.anantacreative.updater.tests;

import org.anantacreative.updater.Version;
import org.anantacreative.updater.VersionCheck.DefineActualVersionError;
import org.anantacreative.updater.VersionCheck.XML.XmlVersionChecker;
import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.testng.AssertJUnit.assertTrue;
import static org.testng.AssertJUnit.fail;

/**
 *
 */
@Test(groups = {"common"})
public class XmlVersionCheсkerTest {
    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }



    public void checkTest(){
        try {
            Version version = new Version(1, 2, 3);
            XmlVersionChecker checker=new XmlVersionChecker(version,new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"));
            assertTrue("Не совпадают версии. Ожидается совпадение",!checker.checkNeedUpdate());

            version = new Version(1, 2, 1);
             checker=new XmlVersionChecker(version,new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"));
            assertTrue("Cовпадают версии. Ожидается несовпадение. Ожидается Необходимость обновления",checker.checkNeedUpdate());

            version = new Version(1, 1, 3);
            checker=new XmlVersionChecker(version,new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"));
            assertTrue("Cовпадают версии. Ожидается несовпадение. Ожидается Необходимость обновления",checker.checkNeedUpdate());

            version = new Version(1, 3, 5);
            checker=new XmlVersionChecker(version,new URL("http://localhost:"+TestingUpdateServer.getPort()+"/version.xml"));
            assertTrue("Cовпадают версии. Ожидается несовпадение. Ожидается отсутствие необъодимости обновления. тк версия выше, чем на сервере", !checker.checkNeedUpdate());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail();
        } catch (DefineActualVersionError e) {
            e.printStackTrace();
            fail();
        }
    }
}

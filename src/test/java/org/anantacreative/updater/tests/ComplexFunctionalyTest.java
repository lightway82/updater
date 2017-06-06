package org.anantacreative.updater.tests;

import org.anantacreative.updater.tests.server.TestingUpdateServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 */
@Test(dependsOnGroups = {"common","updates"})
public class ComplexFunctionalyTest {
    @BeforeClass
    public void init(){
        TestingUpdateServer.startServer();
    }

    public void test(){}

}

package org.anantacreative.updater.tests;


import org.anantacreative.updater.ResourceUtil;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.AssertJUnit.assertTrue;

public class ResourceUtilTest {


    @Test
    public void testSaveResource() throws Exception {
        File dir =   TestUtil.initTestDir("./tmp");
        File file = ResourceUtil.saveResource(dir, "res.zip","/test.zip",false);
        assertTrue(file.exists());
        assertTrue(file.getParentFile().getAbsolutePath().equals(dir.getAbsolutePath()));

    }

    @Test
    public void testSaveResource1() throws Exception {
        File dir =   TestUtil.initTestDir("./tmp");
        File file = ResourceUtil.saveResource(dir.getPath(), "res.zip","/test.zip",true);
        assertTrue(file.exists());
        assertTrue(file.getParentFile().getAbsolutePath().equals(dir.getAbsolutePath()));
    }



}
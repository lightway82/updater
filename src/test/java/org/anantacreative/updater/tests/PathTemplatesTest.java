package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.PathTemplates;
import org.junit.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertTrue;

/**
 *
 */

public class PathTemplatesTest {
    @Test
  public void replaceTest(){
      PathTemplates.setVar(PathTemplates.DOWNLOAD_DIR,"/tmp/download/ ");
      String path =" ${DOWNLOAD_DIR}/otherPath/";
      String correctResultPath ="/tmp/download/otherPath/";
      assertEquals(correctResultPath,PathTemplates.replaceVarsInPath(path));
  }

    @Test
  public void varsTest(){
        PathTemplates.setVar(PathTemplates.DOWNLOAD_DIR,"/tmp/download/ ");
        PathTemplates.setVar("var1","1");
        PathTemplates.setVar("var2","2");

        assertTrue(PathTemplates.getAllVars().size()==3);



  }
}

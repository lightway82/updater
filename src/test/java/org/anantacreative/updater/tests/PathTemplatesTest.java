package org.anantacreative.updater.tests;

import org.anantacreative.updater.Update.PathTemplates;
import org.junit.Test;

import static org.testng.Assert.assertEquals;

/**
 *
 */

public class PathTemplatesTest {
    @Test
  public void test(){
      PathTemplates.setVar(PathTemplates.DOWNLOAD_DIR,"/tmp/download/ ");
      String path =" ${DOWNLOAD_DIR}/otherPath/";
      String correctResultPath ="/tmp/download/otherPath/";
      assertEquals(correctResultPath,PathTemplates.replaceVarsInPath(path));
  }
}

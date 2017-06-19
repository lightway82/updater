package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtil;

import java.util.Arrays;

/**
 *
 */
public class DownloadDirVarTest extends BaseActionTest{


    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        TestUtil.hasFilesInDirExectly(getTestDir(), Arrays.asList("file1.txt"),false);
    }

    @Override
    protected void beforeTest() throws Exception {

    }

    @Override
    protected String getUpdateXmlName() {
        return "download_path_var.xml";
    }
}

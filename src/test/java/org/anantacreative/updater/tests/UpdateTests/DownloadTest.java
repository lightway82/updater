package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateActionFileItem;
import org.anantacreative.updater.Update.UpdateTask;

import java.util.List;

import static org.testng.AssertJUnit.assertTrue;


public class DownloadTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        List<UpdateActionFileItem> files = ut.getDownloadingFilesItem();
        assertTrue(files.size()==1);

        assertTrue(files.get(0).getDownloadedFile().exists());
        assertTrue(files.get(0).getDownloadedFile().getName().equals("file1.txt"));


    }

    @Override
    protected void beforeTest() {

    }

    @Override
    protected String getUpdateXmlName() {
        return "download.xml";
    }
}

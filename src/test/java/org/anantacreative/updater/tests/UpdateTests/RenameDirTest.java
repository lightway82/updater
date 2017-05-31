package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.TestUtils;

import java.io.File;
import java.util.Collections;

import static org.testng.AssertJUnit.fail;


public class RenameDirTest extends BaseActionTest {

    private static final String SRC_DIR_NAME="tmp2";
    private static final String DST_DIR_NAME="tmp3";

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {


        File dir=new File(getTestDir(),DST_DIR_NAME);
        if(!dir.exists())  fail("Отсутствует конечная директория "+dir.getPath());

    }

    @Override
    protected void beforeTest() throws Exception {

        TestUtils.initDirectory(new File(getTestDir(),SRC_DIR_NAME), Collections.emptyList());


    }

    @Override
    protected String getUpdateXmlName() {
        return "rename_dir.xml";
    }
}

package org.anantacreative.updater.tests.UpdateTests;

import org.anantacreative.updater.ResourceUtil;
import org.anantacreative.updater.Update.UpdateTask;
import org.anantacreative.updater.tests.PackUnpackTest;

import java.io.File;


public class UnPackTest extends BaseActionTest {

    @Override
    protected void testLogic(UpdateTask ut) throws Exception {
        PackUnpackTest.checkUnPackTestArch(new File(getTestDir(),"unpacked_files1"));
        PackUnpackTest.checkUnPackTestArch(new File(getTestDir(),"unpacked_files2"));
    }

    @Override
    protected void beforeTest() throws Exception {
        ResourceUtil.saveResource(getTestDir(),"test.zip","/test.zip",true);
    }

    @Override
    protected String getUpdateXmlName() {
        return "unpack.xml";
    }
}

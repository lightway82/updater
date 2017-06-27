package org.anantacreative.updater.tests;

import org.anantacreative.updater.CommonUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@Test(groups = {"common"})
public class CommonUtilsTest {

    public void calculatePersent(){
        assertEquals(0,CommonUtils.calculatePersent(0,11));
        assertEquals(100,CommonUtils.calculatePersent(11,11));
        assertEquals(91,CommonUtils.calculatePersent(10,11));
        assertEquals(10,CommonUtils.calculatePersent(1,11));
    }
}

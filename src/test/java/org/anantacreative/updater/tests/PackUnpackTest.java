package org.anantacreative.updater.tests;


import org.testng.annotations.Test;

public class PackUnpackTest {

    @Test
    public void packFiles(){
        initPackFiles();
    }

    @Test
    public void packDirs(){
        initPackDirs();

    }

    @Test(dependsOnMethods ={"packFiles", "packDirs"})
    public void unPack(){
        initUnPack();
    }


    private void initPackFiles(){

    }

    private void initPackDirs(){

    }


    private void initUnPack(){

    }
}

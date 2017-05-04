/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.anantacreative.updater;

import java.io.File;
import java.net.URL;

/**
 *
 * @author Anama
 */
public interface ServicesInterface 
{
   
    public void stopService();
    public void startService();
    public void init(int maxConnections, File baseDir,URL checkConnectionURL);
    public String getName();
}

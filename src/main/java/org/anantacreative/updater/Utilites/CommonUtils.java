package org.anantacreative.updater.Utilites;


public class CommonUtils
{
    /**
     * Считает  целочисленный округленный процент value от total
     * @param value
     * @param total
     * @return
     */
    public static int calculatePersent(int value, int total){
        return (int)Math.ceil( ((double)(value)/(double)total)*100.0);
    }
}

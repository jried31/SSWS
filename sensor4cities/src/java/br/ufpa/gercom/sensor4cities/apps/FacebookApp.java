/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.apps;

/**
 *
 * @author leokassio
 */
public class FacebookApp implements ISensor4CitiesApp {

    private static FacebookApp instance;
    public static String APP_ID = "367944629914777";
    public static String APP_SECRET = "5cf3f647376a8b4a6a2299dabb36df6f";
    
    private FacebookApp() {
        
    }
    
    public static FacebookApp getInstance() {
        if(instance == null)
            instance = new FacebookApp();
        return instance;
    }
    
    public void finishApp() {
        
    }

    public void startApp() {
        
    }
    
}

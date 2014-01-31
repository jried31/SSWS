/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.apps.SimulationApp;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import java.io.Serializable;

/**
 *
 * @author leokassio
 */

public class AppsBean implements Serializable {
    
    private TwitterApp twitterApp;
    private SimulationApp simulationApp;
    
    public void startTwitterApp() {
        twitterApp = TwitterApp.getInstance();
        twitterApp.startApp();
    }

    public void finishTwitterApp() {
        twitterApp.finishApp();
    }
    
    public void startSimulationApp() {
        simulationApp = SimulationApp.getInstance();
        simulationApp.startApp();
        
    }
    
    public void finishSimulationApp() {
        simulationApp.finishApp();
    }

    public SimulationApp getSimulationApp() {
        return simulationApp;
    }

    public TwitterApp getTwitterApp() {
        return twitterApp;
    }
    
}

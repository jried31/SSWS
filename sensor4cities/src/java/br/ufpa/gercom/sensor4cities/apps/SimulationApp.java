/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.apps;

import br.ufpa.gercom.sensor4cities.apps.simulation.SimulationThread;

/**
 *
 * @author leokassio
 */
public class SimulationApp implements ISensor4CitiesApp {
    
    private static SimulationApp instance;
    private SimulationThread simulationThread;
    
    private SimulationApp() { }
    
    public static SimulationApp getInstance() {
        if(instance == null) {
            instance = new SimulationApp();
        }
        return instance;
    }

    public void finishApp() {
        simulationThread.setFinish(true);
        instance = null;
    }

    public void startApp() {
        SimulationThread st = new SimulationThread();
        st.start();
    }
}

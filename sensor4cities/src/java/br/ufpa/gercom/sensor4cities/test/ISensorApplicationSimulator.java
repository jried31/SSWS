/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.test;

import br.ufpa.gercom.sensor4cities.wsn.ISensorApplication;
import br.ufpa.gercom.sensor4cities.wsn.messages.datareport.DataQueryMessage;

/**
 *
 * @author leokassio
 */
public class ISensorApplicationSimulator extends ISensorApplication {

    public void initConnection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public boolean sendDataQueryMessage(DataQueryMessage dataQueryMessage) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}

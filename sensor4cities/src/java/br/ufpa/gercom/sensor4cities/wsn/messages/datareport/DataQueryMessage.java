/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.wsn.messages.datareport;

import br.ufpa.gercom.sensor4cities.wsn.messages.Sensor4CitiesMessage;

/**
 *
 * @author leokassio
 */
public class DataQueryMessage extends Sensor4CitiesMessage {

    private int sourceQuery;
    
    public DataQueryMessage(int idTypeMessage, int idPacket, int sourceQuery) {
        super(idTypeMessage, idPacket);
        this.sourceQuery = sourceQuery;
    }

    public int getSourceQuery() {
        return sourceQuery;
    }

    public void setSourceQuery(int sourceQuery) {
        this.sourceQuery = sourceQuery;
    }
    
}

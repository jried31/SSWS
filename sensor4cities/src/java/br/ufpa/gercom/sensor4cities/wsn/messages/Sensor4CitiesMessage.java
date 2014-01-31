/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.wsn.messages;

/**
 *
 * @author leokassio
 */
public abstract class Sensor4CitiesMessage {

    public static final String MESSAGE_FIELD_SEPARATOR = "/";
    public static final String APP_FIELD_SEPARATOR = ";";
    public static final String BROADCAST = "0000:0000:0000:0000";
    public static final String BROADCAST_MEMSIC = "65535";
    public static final int DATAQUERYMESSAGE = 1;
    public static final int DATAREPLYMESSAGE = 2;
    public static final int HELLOMESSAGE = 3;
    public static final int LOCALIZATIONMESSAGE = 4;
    public static final int SLEEPACKMESSAGE = 5;
    public static final int SLEEPQUERYMESSAGE = 6;
    public static final int STATUSQUERYMESSAGE = 7;
    public static final int STATUSREPLYMESSAGE = 8;
    public static final int TIMESTAMPMESSAGE = 9;
    public static final int WEB_QUERY = 1;
    public static final int TWITTER_QUERY = 2;
    
    private int idTypeMessage;
    private int idPacket;

    public Sensor4CitiesMessage() {
    }

    public Sensor4CitiesMessage(int idTypeMessage, int idPacket) {
        this.idTypeMessage = idTypeMessage;
        this.idPacket = idPacket;
    }

    public int getIdTypeMessage() {
        return idTypeMessage;
    }

    public void setIdTypeMessage(int idTypeMessage) {
        this.idTypeMessage = idTypeMessage;
    }

    public int getIdPacket() {
        return idPacket;
    }

    public void setIdPacket(int idPacket) {
        this.idPacket = idPacket;
    }
    
}

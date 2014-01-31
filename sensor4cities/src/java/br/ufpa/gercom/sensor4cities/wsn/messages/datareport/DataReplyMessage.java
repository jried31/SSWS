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
public class DataReplyMessage extends Sensor4CitiesMessage {

    private double temperature;
    private double humidity;
    private double luminance;
    private double co2;
    private double watterPollution;
    private double noisePollution;
    private int sourceQuery;

    public DataReplyMessage() {
    }

    public DataReplyMessage(double temperature, double humidity, double luminance, double co2, double watterPollution, double noisePollution, int sourceQuery, int idTypeMessage, int idPacket) {
        super(idTypeMessage, idPacket);
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminance = luminance;
        this.co2 = co2;
        this.watterPollution = watterPollution;
        this.noisePollution = noisePollution;
        this.sourceQuery = sourceQuery;
    }

    public double getCo2() {
        return co2;
    }

    public void setCo2(double co2) {
        this.co2 = co2;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLuminance() {
        return luminance;
    }

    public void setLuminance(double luminance) {
        this.luminance = luminance;
    }

    public double getNoisePollution() {
        return noisePollution;
    }

    public void setNoisePollution(double noisePollution) {
        this.noisePollution = noisePollution;
    }

    public int getSourceQuery() {
        return sourceQuery;
    }

    public void setSourceQuery(int sourceQuery) {
        this.sourceQuery = sourceQuery;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWatterPollution() {
        return watterPollution;
    }

    public void setWatterPollution(double watterPollution) {
        this.watterPollution = watterPollution;
    }
       
}

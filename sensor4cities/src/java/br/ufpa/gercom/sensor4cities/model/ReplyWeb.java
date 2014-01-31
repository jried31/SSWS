/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class ReplyWeb implements Serializable {

    @Id
    @GeneratedValue
    private Long idReplyWeb;
    @Column(nullable=false)
    private Double temperature;
    @Column(nullable=false)
    private Double humidity;
    @Column(nullable=false)
    private Double luminance;
    @Column(nullable=false)
    private Double co2;
    @Column(nullable=false)
    private Double watterPollution;
    @Column(nullable=false)
    private Double noisePollution;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable=false)
    private Date dateReply;
    @ManyToMany(cascade= CascadeType.ALL)
    private List<QueryWeb> queriesWeb;
    @ManyToOne
    private Sensor sensor;

    public ReplyWeb() {
    }

    public ReplyWeb(Long idReplyWeb, Double temperature, Double humidity, Double luminance, Double co2, Double watterPollution, Double noisePollution, Date dateReply, List<QueryWeb> queriesWeb, Sensor sensor) {
        this.idReplyWeb = idReplyWeb;
        this.temperature = temperature;
        this.humidity = humidity;
        this.luminance = luminance;
        this.co2 = co2;
        this.watterPollution = watterPollution;
        this.noisePollution = noisePollution;
        this.dateReply = dateReply;
        this.queriesWeb = queriesWeb;
        this.sensor = sensor;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public Long getIdReplyWeb() {
        return idReplyWeb;
    }

    public void setIdReplyWeb(Long idReplyWeb) {
        this.idReplyWeb = idReplyWeb;
    }

    public List<QueryWeb> getQueriesWeb() {
        return queriesWeb;
    }

    public void setQueriesWeb(List<QueryWeb> queriesWeb) {
        this.queriesWeb = queriesWeb;
    }

    public Double getCo2() {
        return co2;
    }

    public void setCo2(Double co2) {
        this.co2 = co2;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }

    public Double getLuminance() {
        return luminance;
    }

    public void setLuminance(Double luminance) {
        this.luminance = luminance;
    }

    public Double getNoisePollution() {
        return noisePollution;
    }

    public void setNoisePollution(Double noisePollution) {
        this.noisePollution = noisePollution;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Double getWatterPollution() {
        return watterPollution;
    }

    public void setWatterPollution(Double watterPollution) {
        this.watterPollution = watterPollution;
    }

    public Date getDateReply() {
        return dateReply;
    }

    public void setDateReply(Date dateReply) {
        this.dateReply = dateReply;
    }
    
    public Double getPhenomena(int phenomenaId) {
        switch(phenomenaId) {
            case SensorConstants.TEMPERATURE:
                return temperature;
            case SensorConstants.HUMIDITY:
                return humidity;
            case SensorConstants.LUMINANCE:
                return luminance;
            case SensorConstants.CO2:
                return co2;
            case SensorConstants.WATTER_POLLUTION:
                return watterPollution;
            case SensorConstants.NOISE_POLLUTION:        
                return noisePollution;
            default:
                return 0D;
        }
    }
  
}

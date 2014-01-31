/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import javax.persistence.Column;

/**
 *
 * @author leokassio
 */
@Entity
public class Sensor implements Serializable{

    @Id
    @GeneratedValue
    private Long idSensor;
    @Column(nullable=false)
    private String name;
    private String description;
    @Column(nullable=false)
    private String model;
    @Column(nullable=false)
    private Double latitude;
    @Column(nullable=false)
    private Double longitude;
    @Column(nullable=false)
    private Boolean online;
    @Column(nullable=false)
    private String address;
    @Column(nullable=false)
    private Boolean basestation;
    
    public Sensor() {
    }

    public Sensor(String name, String description, String model, Double latitude, Double longitude, String address, Boolean online, Boolean basestation) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.latitude = latitude;
        this.longitude = longitude;
        this.online = online;
        this.address = address;
        this.basestation = basestation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(Long idSensor) {
        this.idSensor = idSensor;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getIcon() {
        if(model.equals(SensorConstants.SUNSPOT))
            return SensorConstants.ICON_SUNSPOT;
        else if(model.equals(SensorConstants.MEMSIC))
            return SensorConstants.ICON_MEMSIC;
        else if(model.equals(SensorConstants.XBEE))
            return SensorConstants.ICON_XBEE;
        return null;
    }

    public Boolean getBasestation() {
        return basestation;
    }

    public void setBasestation(Boolean basestation) {
        this.basestation = basestation;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author leokassio
 */
@Entity
public class SensorStatus implements Serializable {
    
    @Id
    @GeneratedValue
    private Long idSensorStatus;
    @ManyToOne
    private Sensor sensor;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSensorStatus;
    private Integer battery;
    private Double transmitPower;
    private Integer panId;
    private Integer channel;
    
    
    
}

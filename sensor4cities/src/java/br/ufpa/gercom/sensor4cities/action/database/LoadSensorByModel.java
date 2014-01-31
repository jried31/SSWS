/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class LoadSensorByModel implements IAction {

    private String modelOfMote;
    private Session session;
    private List<Sensor> sensors;

    public LoadSensorByModel() {
    }

    public LoadSensorByModel(String kindOfMote, Session session) {
        this.modelOfMote = kindOfMote;
        this.session = session;
    }
    
    public String execute() {
        Criteria crtr = session.createCriteria(Sensor.class);
        crtr = crtr.add(Restrictions.eq("model", modelOfMote));
        crtr = crtr.addOrder(Order.asc("name"));
        sensors = crtr.list();
        return "ok";
    }

    public void setModelOfMote(String modelOfMote) {
        this.modelOfMote = modelOfMote;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
    
    
    
}

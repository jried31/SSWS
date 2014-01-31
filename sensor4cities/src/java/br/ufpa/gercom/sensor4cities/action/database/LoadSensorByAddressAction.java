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
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class LoadSensorByAddressAction implements IAction {

    private Session session;
    private String address;
    private Sensor sensor;
    
    public String execute() {
        Criteria c = session.createCriteria(Sensor.class);
        c = c.add(Restrictions.eq("address", address));
        c = c.setMaxResults(1);
        List<Sensor> l = c.list();
        if(!l.isEmpty())
            sensor = l.get(0);
        return "ok";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Sensor getSensor() {
        return sensor;
    }
    
}

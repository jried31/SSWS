/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Action desenvolvida para recuperar os sensores persistidos ao banco, pode ser
 * parametrizada conforme um booleano para sensores online ou offline.
 * @author leokassio
 */
public class LoadSensorAction implements IAction {
    
    private Boolean online;
    private List<Sensor> sensors;
    private Session session;

    public LoadSensorAction() {
    }

    public LoadSensorAction(Boolean online, Session session) {
        this.online = online;
        this.session = session;
    }

    public String execute() {
        if(online)
            sensors = getOnlineSensors();
        else
            sensors = getAllSensors();
        return "ok";
    }
    
    private List<Sensor> getAllSensors() {
        Criteria criteria = session.createCriteria(Sensor.class);
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }
    
    private List<Sensor> getOnlineSensors() {
        Criteria criteria = session.createCriteria(Sensor.class);
        criteria.add(Restrictions.eq("online", true));
        criteria.addOrder(Order.asc("name"));
        return criteria.list();
    }

    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

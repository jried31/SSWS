/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import org.hibernate.Session;

/**
 * Action desenvolvida para adição de um novo sensor ao banco de dados.
 * Os sensores adicionados por esta action estarão disponíveis para uso
 * do sistema.
 * @author leokassio
 */
public class AddSensorAction implements IAction{
    
    private Sensor sensor;
    private Session session;

    public AddSensorAction() {
    }

    public AddSensorAction(Sensor sensor, Session session) {
        this.sensor = sensor;
        this.session = session;
    }

    public String execute() {
        Dao<Sensor> dao = new Dao(Sensor.class, session);
        dao.save(sensor);
        return "ok";
    }
    
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

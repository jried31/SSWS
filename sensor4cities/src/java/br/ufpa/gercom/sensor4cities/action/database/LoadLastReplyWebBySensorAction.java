/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * Action desenvolvida para recuperar o Replies mais recentes persistidos no banco
 * @author leokassio
 */
public class LoadLastReplyWebBySensorAction implements IAction {

    private List<Sensor> sensors;
    private Session session;
    private HashMap<Sensor, ReplyWeb> replies;
    private boolean lastMinutes;

    public LoadLastReplyWebBySensorAction() {
        replies = new HashMap<Sensor, ReplyWeb>();
        lastMinutes = false;
    }

    public LoadLastReplyWebBySensorAction(Session session) {
        this.session = session;
    }

    public String execute() {
        if(sensors==null)
            loadAllSensors();
        else
            loadBySensors();
        return "ok";
    }

    private void loadAllSensors() {
        LoadSensorAction lsa = new LoadSensorAction();
        lsa.setSession(session);
        lsa.setOnline(true);
        lsa.execute();
        sensors = lsa.getSensors();
        loadBySensors();
    }

    private void loadBySensors() {
        for (Sensor sensor : sensors) {
            Criteria crtr = session.createCriteria(ReplyWeb.class);
            crtr = crtr.add(Restrictions.eq("sensor", sensor));
            crtr = crtr.addOrder(Order.desc("dateReply"));
            crtr = crtr.setMaxResults(1);
            if(lastMinutes) {
                Date date = new Date(System.currentTimeMillis());
                GregorianCalendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.setTime(date);
                gregorianCalendar.add(GregorianCalendar.MINUTE, -HibernateUtil.LAST_MINUTES);
                date.setTime(gregorianCalendar.getTime().getTime());
                crtr = crtr.add(Restrictions.ge("dateReply", date));
            }
            Transaction t = session.beginTransaction();
            List<ReplyWeb> l = crtr.list();
            t.commit();
            if(!l.isEmpty())
                replies.put(sensor, l.get(0));
            else
                System.err.println("Não há replyweb para o sensor "+sensor.getModel()+" "+sensor.getAddress()+" "+sensor.getName());
        }
    }

    public HashMap<Sensor, ReplyWeb> getReplies() {
        return replies;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setLastMinutes(boolean lastMinutes) {
        this.lastMinutes = lastMinutes;
    }

}

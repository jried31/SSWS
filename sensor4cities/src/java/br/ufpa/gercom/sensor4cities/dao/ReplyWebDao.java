/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.dao;

import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class ReplyWebDao extends Dao<ReplyWeb> {

    public ReplyWebDao(Session session) {
        super(ReplyWeb.class, session);
    }
    
    public List<ReplyWeb> getLastUpdate() {
        Criteria criteria = getSession().createCriteria(ReplyWeb.class);
        criteria.addOrder(Order.desc("dateReply"));
        criteria.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        return criteria.list();
    }
    
    public List<ReplyWeb> getLastReplies() {
        Criteria criteria = getSession().createCriteria(ReplyWeb.class);
        criteria.addOrder(Order.desc("dateReply"));
        criteria.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(GregorianCalendar.MINUTE, -HibernateUtil.LAST_MINUTES);
        date.setTime(gregorianCalendar.getTime().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        criteria.add(Restrictions.ge("dateQueryWeb", calendar));
        return criteria.list();
    }
    
    public ReplyWeb getLastReplyBySensor(Sensor sensor, boolean lastHour) {
        Criteria criteria = getSession().createCriteria(ReplyWeb.class);
        criteria.addOrder(Order.desc("dateReply"));
        criteria.setMaxResults(1);
        criteria.add(Restrictions.eq("sensor", sensor));
        if(lastHour) {
            Date date = new Date(System.currentTimeMillis());
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            gregorianCalendar.add(GregorianCalendar.MINUTE, -HibernateUtil.LAST_MINUTES);
            date.setTime(gregorianCalendar.getTime().getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            criteria.add(Restrictions.ge("dateReply", calendar));
        }
        Iterator<ReplyWeb> l = criteria.list().iterator();
        if(l.hasNext())
            return l.next();
        else 
            return null;
    }
    
}

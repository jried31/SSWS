/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Action desenvolvida para recuperar as Queries da última hora persistidas 
 * junto ao banco conforme
 * o tipo de mote parametrizado.
 * Os tipos de motes são definidos conforme a classe Constants.
 * @author leokassio
 */
public class LoadQueryWebPendingAction implements IAction {

    private List<QueryWeb> queryWebs;
    private int modelOfMote;
    private Session session;

    public LoadQueryWebPendingAction(int modelOfMote, Session session) {
        this.modelOfMote = modelOfMote;
        this.session = session;
    }

    public LoadQueryWebPendingAction() {
    }
    
    public String execute() {
        if(modelOfMote == SensorConstants.ID_SUNSPOT)
            queryWebs = getQuerySunspot();
        else if(modelOfMote == SensorConstants.ID_MEMSIC)
            queryWebs = getQueryMemsic();
        else if(modelOfMote == SensorConstants.ID_XBEE)
            queryWebs = getQueryXbee();
        
        return "ok";
    }

    public void setModelOfMote(int modelOfMote) {
        this.modelOfMote = modelOfMote;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<QueryWeb> getQueryWebs() {
        return queryWebs;
    }   
    /**
     * Metodo retorna apenas queries pendentes da ultima hora
     * @return 
     */
    private List<QueryWeb> getQueryMemsic() {
        Criteria criteria = session.createCriteria(QueryWeb.class);
        criteria.add(Restrictions.eq("memsic", true));
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(GregorianCalendar.HOUR, -HibernateUtil.LAST_HOUR);
        date.setTime(gregorianCalendar.getTime().getTime());
        criteria.add(Restrictions.ge("dateQueryWeb", date));
        criteria.add(Restrictions.eq("attentedMemsic", false));
        Transaction t = session.beginTransaction();
        List<QueryWeb> l = criteria.list();
        t.commit();
        return l;
    }
    
    private List<QueryWeb> getQuerySunspot() {
        Criteria criteria = session.createCriteria(QueryWeb.class);
        criteria.add(Restrictions.eq("sunspot", true));
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(GregorianCalendar.HOUR, -HibernateUtil.LAST_HOUR);
        date.setTime(gregorianCalendar.getTime().getTime());
        criteria.add(Restrictions.ge("dateQueryWeb", date));
        criteria.add(Restrictions.eq("attentedSunspot", false));
        Transaction t = session.beginTransaction();
        List<QueryWeb> l = criteria.list();
        t.commit();
        return l;
    }
    
    private List<QueryWeb> getQueryXbee() {
        Criteria criteria = session.createCriteria(QueryWeb.class);
        criteria.add(Restrictions.eq("xbee", true));
        Date date = new Date(System.currentTimeMillis());
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.add(GregorianCalendar.HOUR, -HibernateUtil.LAST_HOUR);
        date.setTime(gregorianCalendar.getTime().getTime());
        criteria.add(Restrictions.ge("dateQueryWeb", date));
        criteria.add(Restrictions.eq("attentedXbee", false));
        Transaction t = session.beginTransaction();
        List<QueryWeb> l = criteria.list();
        t.commit();
        return l;
    }
}

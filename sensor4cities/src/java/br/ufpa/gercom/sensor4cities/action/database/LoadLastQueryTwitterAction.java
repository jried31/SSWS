/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author leokassio
 */
public class LoadLastQueryTwitterAction implements IAction {
    
    private Session session;
    private List<QueryTwitter> queryTwitter;

    public String execute() {
        Criteria c = session.createCriteria(QueryTwitter.class);
        c = c.addOrder(Order.desc("dateQuery"));
        c = c.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        queryTwitter = c.list();
        return "ok";
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<QueryTwitter> getQueryTwitter() {
        return queryTwitter;
    }
    
}

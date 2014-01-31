/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * Action desenvolvida para recuperar as Queries persistidas no banco.
 * Retorna uma lista das 'n' Queries mais recentes.
 * Valor de 'n' definido na classe Constants do pacote 
 * br.ufpa.sensorslab.managedbean.
 * @author leokassio
 */
public class LoadLastQueryWebAction implements IAction {

    private List<QueryWeb> queriesweb;
    private Session session;

    public LoadLastQueryWebAction() {
    }

    public LoadLastQueryWebAction(Session session) {
        this.session = session;
    }
    
    public String execute() {
        Criteria criteria = session.createCriteria(QueryWeb.class);
        criteria = criteria.addOrder(Order.desc("dateQueryWeb"));
        criteria = criteria.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        queriesweb = criteria.list();        
        return "ok";
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<QueryWeb> getQueriesweb() {
        return queriesweb;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 * Action utilizada para resgatar Queries advindas do Twitter ainda pendentes
 * de serem atendidas.
 * @author leokassio
 */
public class LoadQueryTwitterPendingAction implements IAction {

    private Session session;
    private List<QueryTwitter> queriesTwitter;

    public LoadQueryTwitterPendingAction() {
    }

    public LoadQueryTwitterPendingAction(Session session) {
        this.session = session;
    }
    
    public String execute() {
        Criteria c = session.createCriteria(QueryTwitter.class);
        c = c.add(Restrictions.eq("attended", false));
        Transaction t = session.beginTransaction();
        queriesTwitter = c.list();
        t.commit();
        return "ok";
    }

    public List<QueryTwitter> getQueriesTwitter() {
        return queriesTwitter;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

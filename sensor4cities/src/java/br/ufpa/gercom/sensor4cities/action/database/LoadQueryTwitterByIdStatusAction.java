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
import org.hibernate.criterion.Restrictions;

/**
 * Action utilizada para carregar e confirmar a existencia de determinada QueryTwitter.
 * @author leokassio
 */
public class LoadQueryTwitterByIdStatusAction implements IAction {

    private Session session;
    private Long idStatus;
    private QueryTwitter queryTwitter;
    private boolean exists;

    public LoadQueryTwitterByIdStatusAction() {
    }

    public LoadQueryTwitterByIdStatusAction(Session session, Long idStatus) {
        this.session = session;
        this.idStatus = idStatus;
    }
    
    public String execute() {
        Criteria c = session.createCriteria(QueryTwitter.class);
        c = c.add(Restrictions.eq("idTweet", idStatus));
        List<QueryTwitter> l = c.list();
        exists = !l.isEmpty();
        for (QueryTwitter q : l) {
            queryTwitter = q;
        }
        return "ok";
    }

    public void setIdStatus(Long idStatus) {
        this.idStatus = idStatus;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public boolean isExists() {
        return exists;
    }

    public QueryTwitter getQueryTwitter() {
        return queryTwitter;
    } 
    
}

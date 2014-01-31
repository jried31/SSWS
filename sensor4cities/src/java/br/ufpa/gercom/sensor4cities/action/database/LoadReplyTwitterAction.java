/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class LoadReplyTwitterAction implements IAction{
    
    private Session session;
    private QueryTwitter queryTwitter;
    private List<ReplyTwitter> replyTwitter;

    public String execute() {
        Criteria c = session.createCriteria(ReplyTwitter.class);
        c = c.add(Restrictions.eq("queryTwitter_idQueryTwitter", queryTwitter.getIdQueryTwitter()));
        Transaction t = session.beginTransaction();
        replyTwitter = c.list();
        t.commit();
        return "ok";
    }

    public void setQueryTwitter(QueryTwitter queryTwitter) {
        this.queryTwitter = queryTwitter;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<ReplyTwitter> getReplyTwitter() {
        return replyTwitter;
    }
    
}

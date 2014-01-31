/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 * Action utilizada para analisar as QueriesTwitter pendentes de publicação
 * @author leokassio
 */

public class LoadReplyTwitterPendingAction implements IAction {

    private Session session;
    private List<ReplyTwitter> repliesTwitter;

    public LoadReplyTwitterPendingAction() {
    }

    public LoadReplyTwitterPendingAction(Session session) {
        this.session = session;
    }
    
    public String execute() {
        Criteria c = session.createCriteria(ReplyTwitter.class);
        c = c.add(Restrictions.eq("published", false));
        repliesTwitter = c.list();
        return "ok";
    }

    public List<ReplyTwitter> getRepliesTwitter() {
        return repliesTwitter;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
    
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author leokassio
 */
public class LoadLastReplyTwitterAction implements IAction {

    private Session session;
    private List<ReplyTwitter> replyTwitter;
    
    public String execute() {
        Criteria c = session.createCriteria(ReplyTwitter.class);
        c = c.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        c = c.addOrder(Order.desc("dateReply"));
        replyTwitter = c.list();
        return "ok";
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<ReplyTwitter> getReplyTwitter() {
        return replyTwitter;
    }
    
}

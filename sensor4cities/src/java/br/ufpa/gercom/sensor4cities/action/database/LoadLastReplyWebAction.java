/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * Action para carregamento dos últimos Reply persistidos
 * @author leokassio
 */
public class LoadLastReplyWebAction implements IAction {

    private Session session;
    private List<ReplyWeb> replyWeb;

    public LoadLastReplyWebAction(Session session) {
        this.session = session;
    }

    public LoadLastReplyWebAction() {
    }
    
    public String execute() {
        Criteria c = session.createCriteria(ReplyWeb.class);
        c = c.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        c = c.addOrder(Order.desc("dateReply"));
        replyWeb = c.list();
        return "ok";
    }

    public List<ReplyWeb> getReplyWeb() {
        return replyWeb;
    }

    public void setSession(Session session) {
        this.session = session;
    }    
}

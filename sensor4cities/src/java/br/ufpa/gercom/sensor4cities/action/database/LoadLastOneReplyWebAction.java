/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 * Action para carregamento dp último Reply persistido de UM UNICO sensor
 * @author leokassio
 */
public class LoadLastOneReplyWebAction implements IAction {

    private Session session;
    private ReplyWeb replyWeb;

    public LoadLastOneReplyWebAction(Session session) {
        this.session = session;
    }

    public LoadLastOneReplyWebAction() {
    }
    
    public String execute() {
        Criteria c = session.createCriteria(ReplyWeb.class);
        c = c.setMaxResults(1);
        c = c.addOrder(Order.desc("dateReply"));
        List<ReplyWeb> l = c.list();
        if(!l.isEmpty())
            replyWeb = l.iterator().next();
        return "ok";
    }

    public ReplyWeb getReplyWeb() {
        return replyWeb;
    }

    public void setSession(Session session) {
        this.session = session;
    }    
}

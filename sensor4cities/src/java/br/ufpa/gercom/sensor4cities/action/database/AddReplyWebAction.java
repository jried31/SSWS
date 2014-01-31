/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.dao.ReplyWebDao;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class AddReplyWebAction implements IAction{
    
    private Session session;
    private ReplyWeb replyWeb;

    public void setReplyWeb(ReplyWeb replyWeb) {
        this.replyWeb = replyWeb;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public String execute() {
        ReplyWebDao replyWebDao = new ReplyWebDao(session);
        replyWebDao.save(replyWeb);
        return "ok";
    }
    
    
}

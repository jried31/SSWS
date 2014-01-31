/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Action destinada a persistir as mensagens de resposta (replies) dos sensores
 * a consultas advindas do twitter.
 * @author leokassio
 */
public class AddReplyTwitterAction implements IAction {

    private Session session;
    private List<QueryTwitter> queries;
    private String tweet;

    public AddReplyTwitterAction() {
    }

    public AddReplyTwitterAction(Session session, List<QueryTwitter> queries, String tweet) {
        this.session = session;
        this.queries = queries;
        this.tweet = tweet;
    }
    
    public String execute() {
        Iterator<QueryTwitter> it = queries.iterator();
        while(it.hasNext()) {
            QueryTwitter q = it.next();
            ReplyTwitter r = new ReplyTwitter();
            Date date = new Date(System.currentTimeMillis());
            r.setDateReply(date);
            r.setQueryTwitter(q);
            r.setTweet(q.getUserQuery()+" "+tweet);
            q.setAttended(true);
            Transaction t = session.beginTransaction();
            session.save(q);
            session.save(r);
            t.commit();
        }
        return "ok";
    }

    public void setQueries(List<QueryTwitter> queries) {
        this.queries = queries;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }    
}

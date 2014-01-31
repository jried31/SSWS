/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.TwitterConfig;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author leokassio
 */
public class LoadTwitterConfigAction implements IAction {

    private Session session;
    private String twitterUser;
    private TwitterConfig twitterConfig;
    
    public String execute() {
        Criteria c = session.createCriteria(TwitterConfig.class);
        c = c.add(Restrictions.and(Restrictions.eq("twitterUser", twitterUser), Restrictions.eq("valid", true)));
        c = c.addOrder(Order.desc("dateTwitterConfig"));
        List<TwitterConfig> l = c.list();
        if(!l.isEmpty()){
            twitterConfig = l.get(0);
        }
        return "ok";
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setTwitterUser(String userTwitter) {
        this.twitterUser = userTwitter;
    }

    public TwitterConfig getTwitterConfig() {
        return twitterConfig;
    }
}

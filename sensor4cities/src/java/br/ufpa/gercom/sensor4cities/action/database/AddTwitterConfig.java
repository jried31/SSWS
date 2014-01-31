/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
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
public class AddTwitterConfig implements IAction {

    private Session session;
    private TwitterConfig twitterConfig;
    
    public String execute() {
        Criteria c = session.createCriteria(TwitterConfig.class);
        c = c.add(Restrictions.eq("twitterUser", twitterConfig.getTwitterUser()));
        c = c.addOrder(Order.desc("dateTwitterConfig"));
        List<TwitterConfig> l = c.list();
        Dao<TwitterConfig> dao = new Dao<TwitterConfig>(TwitterConfig.class, session);
        for (TwitterConfig t : l) {
            t.setValid(false);
            dao.save(t);
        }
        dao.save(twitterConfig);
        return "ok";
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void setTwitterConfig(TwitterConfig twitterConfig) {
        this.twitterConfig = twitterConfig;
    }
        
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.LoggerException;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

/**
 *
 * @author leokassio
 */
public class LoadLastLoggerExceptionsAction implements IAction {

    private Session session;
    private List<LoggerException> loggerException;
    
    public String execute() {
        Criteria c = session.createCriteria(LoggerException.class);
        c = c.addOrder(Order.desc("dateException"));
        c = c.setMaxResults(HibernateUtil.CRITERIA_MAX_RESULTS);
        loggerException = c.list();
        return "ok";
    }

    public List<LoggerException> getLoggerException() {
        return loggerException;
    }

    public void setSession(Session session) {
        this.session = session;
    }    
}

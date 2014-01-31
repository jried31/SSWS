/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.model.LoggerException;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class AddLoggerExceptionAction implements IAction {

    private Session session;
    private LoggerException loggerException;
    
    public String execute() {
        Dao<LoggerException> dao = new Dao<LoggerException>(LoggerException.class, session);
        dao.save(loggerException);
        return "ok";
    }

    public void setLoggerException(LoggerException loggerException) {
        this.loggerException = loggerException;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

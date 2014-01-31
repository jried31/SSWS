/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import org.hibernate.Session;

/**
 * Action desenvolvida para adição de uma nova Query ao banco de dados.
 * OBS: Apenas queries advindas do website.
 * @author leokassio
 */
public class AddQueryWebAction implements IAction {

    private QueryWeb queryWeb;
    private Session session;

    public AddQueryWebAction(QueryWeb queryWeb, Session session) {
        this.queryWeb = queryWeb;
        this.session = session;
    }

    public AddQueryWebAction() {
    }
    
    public String execute() {
        Dao dao = new Dao(QueryWeb.class, session);
        dao.save(queryWeb);
        return "ok";
    }

    public void setQueryWeb(QueryWeb queryWeb) {
        this.queryWeb = queryWeb;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Action responsável pela adição de Queries oriundas do Twitter, persiste
 * a requisição no banco de dados
 * @author leokassio
 */
public class AddQueryTwitterAction implements IAction{

    private Session session;
    private QueryTwitter queryTwitter;

    public AddQueryTwitterAction(Session session, QueryTwitter queryTwitter) {
        this.session = session;
        this.queryTwitter = queryTwitter;
    }

    public AddQueryTwitterAction() {
    }

    public String execute() {
        Transaction transaction = session.beginTransaction();
        session.save(queryTwitter);
        transaction.commit();
        return "ok";
    }

    public void setQueryTwitter(QueryTwitter queryTwitter) {
        this.queryTwitter = queryTwitter;
    }

    public void setSession(Session session) {
        this.session = session;
    }    
    
}

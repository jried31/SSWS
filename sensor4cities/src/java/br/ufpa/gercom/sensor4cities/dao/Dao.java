/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.dao;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author leokassio
 */
public class Dao<T> {
    
    private Class persistentClass;
    private Session session;

    public Dao(Class persistentClass, Session session) {
        this.persistentClass = persistentClass;
        this.session = session;
    }

    public Serializable save(T o) throws HibernateException {
        Transaction transaction = session.beginTransaction();
        Serializable serializable =  session.save(o);
        transaction.commit();
        return serializable;
    }

    public void delete(T o) throws HibernateException {
        Transaction t = session.beginTransaction();
        session.delete(o);
        t.commit();
    }
    
    protected Session getSession() {
        return session;
    }
    
    protected void closeSession() {
        session.close();
    }
    
    
}

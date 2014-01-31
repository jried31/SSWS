/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.configuration;

import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class ConfigurationEmpty extends Database {
    
    public static void main(String agrgs[]) {
        Session session = HibernateUtil.openSession();
        createDataBase();
        addAdminMember(session);
        session.close();
    }
    
}

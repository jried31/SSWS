/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.configuration;


import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.model.AdminMember;
import br.ufpa.gercom.sensor4cities.apps.twitter.TwitterConnector;
import br.ufpa.gercom.sensor4cities.apps.twitter.TwitterQueryThread;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import twitter4j.Twitter;


/**
 *
 * @author leokassio
 */
public abstract class Database {
    
    protected static void createDataBase() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        SchemaExport schemaExport = new SchemaExport(configuration);
        schemaExport.create(true, true);
    }
    
    protected static void addQueryTwitter() throws InterruptedException {
        TwitterConnector twitterConnector = new TwitterConnector();
        twitterConnector.connect();
        Twitter twitter = twitterConnector.getTwitter();
        /*  o booleano indica que o banco esta sendo iniciado e portanto todas as quries encontradas
            devem ser consideradas como atendidas */
        TwitterQueryThread queryThread = new TwitterQueryThread(twitter, true);
        queryThread.start();
    }
    
    protected static void addAdminMember(Session session) {
        Dao<AdminMember> dao = new Dao<AdminMember>(AdminMember.class, session);
        dao.save(new AdminMember("admin", "adminadmin"));
    }
        
}

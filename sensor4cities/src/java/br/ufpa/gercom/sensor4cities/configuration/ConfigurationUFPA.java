/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.configuration;

import br.ufpa.gercom.sensor4cities.action.database.AddTwitterConfig;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.AdminMember;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import br.ufpa.gercom.sensor4cities.model.TwitterConfig;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class ConfigurationUFPA extends Database {

    public static void main(String args[]) throws InterruptedException {
        Session session = HibernateUtil.openSession();
        addSensors(session);
        addAdminMember(session);
        addTwitterConfig(session);
        addQueryTwitter();
        session.close();
    }
    
    private static void addSensors(Session session) {
        Dao sensorDao = new Dao(Sensor.class, session);
 
        //sensorDao.save(new Sensor("Nodo 01", "Basestation v6 SunSPOT Tradicional", SensorConstants.SUNSPOT, -1.4753948798828609, -48.454266712966955, "0014.4F01.0000.79E7", true, true));
        sensorDao.save(new Sensor("Nodo 02", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.4753948798828609, -48.454266712966955, "0014.4F01.0000.78D2", true, false));
        sensorDao.save(new Sensor("Nodo 03", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.4738718897190017, -48.45482461244205, "0014.4F01.0000.7890", true, false));
        sensorDao.save(new Sensor("Nodo 04", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.4742365494305842, -48.45517866403202, "0014.4F01.0000.780F", true, false));
        sensorDao.save(new Sensor("Nodo 05", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.474440329831634, -48.45472805291752, "0014.4F01.0000.7A0C", true, false));
        sensorDao.save(new Sensor("Nodo 06", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.47448323096631, -48.45468513757328, "0014.4F01.0000.781B", true, false));
        sensorDao.save(new Sensor("Nodo 07", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.4746977366273368, -48.455028460327185, "0014.4F01.0000.818C", true, false));
        sensorDao.save(new Sensor("Nodo 08", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.474998044518035, -48.45448128968815, "0014.4F01.0000.7A29", true, false));
        sensorDao.save(new Sensor("Nodo 09", "SunSPOT Nodo v6", SensorConstants.SUNSPOT, -1.4731854712763832, -48.45467440873722, "0014.4F01.0000.7C62", true, false));
        //sensorDao.save(new Sensor("Nodo 10", "Basestation v6 SunSPOT Adaptada Com Bateria Adicional e Sem SensorBoad", SensorConstants.SUNSPOT, -1.47526617652906, -48.454770968261755, "0014.4F01.0000.77E7", true, false));
        sensorDao.save(new Sensor("Nodo 11", "Basestation v6 SunSPOT Adaptada Com Bateria Adicional e Sem SensorBoad", SensorConstants.SUNSPOT, -1.4728315366842737, -48.4551035621796, "0014.4F01.0000.77BE", true, false));
        sensorDao.save(new Sensor("Nodo 12", "Basestation v6 SunSPOT Adaptada Com Bateria Adicional e Sem SensorBoad", SensorConstants.SUNSPOT, -1.4735715816763875, -48.45520012170414, "0014.4F01.0000.7CBD", true, false));
        sensorDao.save(new Sensor("Nodo 13", "MEMSIC Iris Mote", SensorConstants.MEMSIC, -1.4766819130113276, -48.454052136245764, "1", true, false));
        sensorDao.save(new Sensor("Nodo 14", "MEMSIC Iris Mote", SensorConstants.MEMSIC, -1.4763494295245085, -48.45339767724613, "2", true, false));
        sensorDao.save(new Sensor("Nodo 15", "MEMSIC Iris Mote", SensorConstants.MEMSIC, -1.4763494295245085, -48.45422379762272, "3", true, false));
        sensorDao.save(new Sensor("Nodo 16", "MEMSIC Iris Mote", SensorConstants.MEMSIC, -1.475995495435512, -48.453751728836096, "4", true, false));
        sensorDao.save(new Sensor("Nodo 17", "MEMSIC Iris Mote", SensorConstants.MEMSIC, -1.4760920229199188, -48.45434181481937, "5", true, false));
        sensorDao.save(new Sensor("Nodo 18", "MEMSIC TelosB Mote", SensorConstants.MEMSIC, -1.4758560668395453, -48.45390193254093, "6", true, false));
        sensorDao.save(new Sensor("Nodo 19", "MEMSIC TelosB Mote", SensorConstants.MEMSIC, -1.4756308360121768, -48.45407359391788, "7", true, false));
        sensorDao.save(new Sensor("Nodo 20", "MEMSIC TelosB Mote", SensorConstants.MEMSIC, -1.4756201107341216, -48.45468513757328, "8", true, false));
    }
    
    protected static void addAdminMember(Session session) {
        Dao<AdminMember> dao = new Dao<AdminMember>(AdminMember.class, session);
        dao.save(new AdminMember("leokassio", "gercom2012"));
        dao.save(new AdminMember("dlrosario", "gercom2012"));
        dao.save(new AdminMember("ecerqueira", "gercom2012"));
        dao.save(new AdminMember("pterroso", "gercom2012"));
        dao.save(new AdminMember("tmousinho", "gercom2012"));
        dao.save(new AdminMember("rcosta", "gercom2012"));
        dao.save(new AdminMember("csilva", "gercom2012"));
    }
    
    private static void addTwitterConfig(Session session) {
        TwitterConfig t = new TwitterConfig();
        t.setConsumerKey(TwitterApp.CONSUMER_KEY);
        t.setConsumerSecret(TwitterApp.CONSUMER_SECRET);
        t.setTwitterUser(TwitterApp.TWITTER_USER);
        t.setToken("540955426-kzM7uEkie92cbLQiBnSxpnbPqiDoHrMTBlMpzoxt");
        t.setTokenSecret("akjaWF4ruX7JIZsFUgtvQr1AE399Id9hL5rvHFRXBi0");
        t.setValid(true);
        t.setDateTwitterConfig(new Date(System.currentTimeMillis()));
        AddTwitterConfig addTwitterConfig = new AddTwitterConfig();
        addTwitterConfig.setSession(session);
        addTwitterConfig.setTwitterConfig(t);
        addTwitterConfig.execute();
    }
    
}

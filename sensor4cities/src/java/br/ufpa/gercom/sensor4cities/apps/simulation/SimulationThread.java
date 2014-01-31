package br.ufpa.gercom.sensor4cities.apps.simulation;

import br.ufpa.gercom.sensor4cities.action.database.AddDataReplyAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadQueryTwitterPendingAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadQueryWebPendingAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadSensorByModel;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.managedbean.ConstantsBean;
import br.ufpa.gercom.sensor4cities.wsn.messages.Sensor4CitiesMessage;
import br.ufpa.gercom.sensor4cities.wsn.messages.datareport.DataReplyMessage;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.hibernate.Session;
import org.hibernate.Transaction;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author leokassio
 */
public class SimulationThread extends Thread {

    private Session session;
    private SimpleDateFormat simpleDateFormat;
    private Date date;
    private boolean finish;

    public SimulationThread() {
        
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        date = new Date();
    }
    
    
    @Override
    public void run() {
        super.run();
        while(true) {
            try {
                session = HibernateUtil.openSession();
                date.setTime(System.currentTimeMillis());
                webSimulator();
                twitterSimulator();
                session.close();
                if(finish)
                    break;
                Thread.sleep(10000);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    public void webSimulator() {
        Random r = new Random(System.currentTimeMillis());
        
        LoadSensorByModel lsbm = new LoadSensorByModel(SensorConstants.SUNSPOT, session);
        lsbm.execute();
        List<Sensor> sensors = lsbm.getSensors();
        
        LoadQueryWebPendingAction lqwpa = new LoadQueryWebPendingAction();
        lqwpa.setSession(session);
        lqwpa.setModelOfMote(SensorConstants.ID_SUNSPOT);
        lqwpa.execute();
        List<QueryWeb> l = lqwpa.getQueryWebs();
        if(!l.isEmpty()) {
            System.err.println("[GERCOM] [Simulation] "+l.size()+" Pending Queries for SunSPOT Motes "+simpleDateFormat.format(date));
            for (Sensor sensor : sensors) {
                System.out.println("[GERCOM] [Simulation] Saving ReplyWeb from "+sensor.getName()+" "+simpleDateFormat.format(date));
                AddDataReplyAction frwa = new AddDataReplyAction();
                frwa.setSession(session);
                frwa.setQueryWebs(l);
                frwa.setDataReplyMessage(new DataReplyMessage(27+r.nextInt(2), 87+r.nextInt(3), r.nextInt(750), 0, 0, 0, 0, Sensor4CitiesMessage.DATAREPLYMESSAGE, Sensor4CitiesMessage.WEB_QUERY));
                frwa.setSensor(sensor);
                frwa.execute();
            }
        } else
            System.err.println("[GERCOM] [Simulation] No Pending Queries for SunSPOT Motes "+simpleDateFormat.format(date));            
        
        
        
        lsbm = new LoadSensorByModel(SensorConstants.MEMSIC, session);
        lsbm.execute();
        sensors = lsbm.getSensors();
        
        lqwpa = new LoadQueryWebPendingAction();
        lqwpa.setSession(session);
        lqwpa.setModelOfMote(SensorConstants.ID_MEMSIC);
        lqwpa.execute();
        l = lqwpa.getQueryWebs();
        if(!l.isEmpty()) {
            System.err.println("[GERCOM] [Simulation] "+l.size()+" Pending Queries for MEMSIC Motes "+simpleDateFormat.format(date));
            for (Sensor sensor : sensors) {
                System.out.println("[GERCOM] [Simulation] Saving ReplyWeb from "+sensor.getName()+" "+simpleDateFormat.format(date));
                AddDataReplyAction frwa = new AddDataReplyAction();
                frwa.setSession(session);
                frwa.setQueryWebs(l);
                frwa.setDataReplyMessage(new DataReplyMessage(27+r.nextInt(2), 87+r.nextInt(3), r.nextInt(750), 0, 0, 0, 0, Sensor4CitiesMessage.DATAREPLYMESSAGE, Sensor4CitiesMessage.TWITTER_QUERY));
                frwa.setSensor(sensor);
                frwa.execute();
            }
        } else {
            System.err.println("[GERCOM] [Simulation] No Pending Queries for MEMSIC Motes "+simpleDateFormat.format(date));
            return;
        }
            
        
    }
    
    public void twitterSimulator() {
        Random r = new Random(System.currentTimeMillis());
        LoadQueryTwitterPendingAction lqtpa = new LoadQueryTwitterPendingAction();
        lqtpa.setSession(session);
        lqtpa.execute();
        List<QueryTwitter> list = lqtpa.getQueriesTwitter();
        for (QueryTwitter queryTwitter : list) {
            Transaction t = session.beginTransaction();
            ReplyTwitter replyTwitter = new ReplyTwitter();
            replyTwitter.setDateReply(new Date(System.currentTimeMillis()));
            replyTwitter.setPublished(false);
            replyTwitter.setQueryTwitter(queryTwitter);
            int temperature = r.nextInt(3)+27;
            replyTwitter.setTweet("@"+queryTwitter.getUserQuery()+" "+TwitterApp.CITY+" "+temperature+"°C, with possibility of rain");
            //replyTwitter.setTweet("@"+queryTwitter.getUserQuery()+" "+TwitterApp.CITY+" "+temperature+"°C, com previsão de chuva");
            session.save(replyTwitter);
            queryTwitter.setAttended(true);
            session.save(queryTwitter);
            t.commit();
        }
        
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
    
}

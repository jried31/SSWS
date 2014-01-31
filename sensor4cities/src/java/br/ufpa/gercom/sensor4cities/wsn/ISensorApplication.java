package br.ufpa.gercom.sensor4cities.wsn;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import br.ufpa.gercom.sensor4cities.action.database.AddLoggerExceptionAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadQueryTwitterPendingAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadQueryWebPendingAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadSensorByAddressAction;
import br.ufpa.gercom.sensor4cities.model.*;
import br.ufpa.gercom.sensor4cities.wsn.messages.Sensor4CitiesMessage;
import br.ufpa.gercom.sensor4cities.wsn.messages.datareport.DataQueryMessage;
import br.ufpa.gercom.sensor4cities.wsn.messages.datareport.DataReplyMessage;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author leokassio
 */
public abstract class ISensorApplication {
    
    public List<QueryWeb> loadQueryWebPending(Session session, int kindOfMote) {
        LoadQueryWebPendingAction lqwpa = new LoadQueryWebPendingAction();
        lqwpa.setSession(session);
        lqwpa.setModelOfMote(kindOfMote);
        lqwpa.execute();
        return lqwpa.getQueryWebs();
    }
    
    public List<QueryTwitter> loadQueryTwitterPending(Session session) {
        LoadQueryTwitterPendingAction lqtpa = new LoadQueryTwitterPendingAction();
        lqtpa.setSession(session);
        lqtpa.execute();
        return lqtpa.getQueriesTwitter();
    }
    
    public abstract void initConnection();

    public abstract boolean sendDataQueryMessage(DataQueryMessage dataQueryMessage);
    
    public boolean saveDataReplyMessage(Session session, DataReplyMessage dataReplyMessage, String sourceNode) {
        if(dataReplyMessage.getSourceQuery() == Sensor4CitiesMessage.TWITTER_QUERY) {
            QueryTwitter qt = (QueryTwitter) session.load(QueryTwitter.class, new Long(dataReplyMessage.getIdPacket()));
            if(qt != null) {
                ReplyTwitter rt = new ReplyTwitter();
                rt.setDateReply(new Date(System.currentTimeMillis()));
                rt.setPublished(false);
                rt.setQueryTwitter(qt);
                
                //TODO Make the natural language response
                rt.setTweet("@"+qt.getUserQuery()+" The weather is cloudy with possibility of rain. Temperature: "+dataReplyMessage.getTemperature() + "°C and Luminance: "+dataReplyMessage.getLuminance()+"lx");
                //rt.setTweet("@"+qt.getUserQuery()+" "+dataReplyMessage.getTemperature() + "°C com previsão de chuva, umidade "+dataReplyMessage.getHumidity()+"% e Luminancia "+dataReplyMessage.getLuminance()+"lx");
                qt.setAttended(true);
                List<ReplyTwitter> l = qt.getReplyTwitter();
                if(!l.isEmpty())
                    rt.setPublished(true);
                Transaction t = session.beginTransaction();
                session.save(rt);
                session.save(qt);
                t.commit();
            } else {
                System.out.println("[GERCOM] [Error] No QueryTwitter with id "+dataReplyMessage.getIdPacket());
                AddLoggerExceptionAction alea = new AddLoggerExceptionAction();
                alea.setSession(session);
                alea.setLoggerException(new LoggerException(new Date(System.currentTimeMillis()), "Twitter DataReply Recebido para uma Query inexistente", "Não se aplica", "ISensorApplication", "Não se aplica"));
            }
        } else if(dataReplyMessage.getSourceQuery() == Sensor4CitiesMessage.WEB_QUERY) {
            QueryWeb queryWeb = (QueryWeb) session.load(QueryWeb.class, new Long(dataReplyMessage.getIdPacket()));
            if(queryWeb != null) {
                LoadSensorByAddressAction lsbaa = new LoadSensorByAddressAction();
                lsbaa.setSession(session);
                lsbaa.setAddress(sourceNode);
                lsbaa.execute();
                Sensor s = lsbaa.getSensor();
                if(s == null) {
                    System.out.println("[GERCOM] [Error] Node Sensor "+sourceNode+" is not registred in Sensor4Cities");
                    AddLoggerExceptionAction alea = new AddLoggerExceptionAction();
                    alea.setSession(session);
                    alea.setLoggerException(new LoggerException(new Date(System.currentTimeMillis()), "Sensor "+sourceNode+" não cadastrado", "Não se aplica", "ISensorApplication", "Não se aplica"));
                    return false;
                }
                ReplyWeb rw = new ReplyWeb();
                rw.setCo2(dataReplyMessage.getCo2());
                rw.setDateReply(new Date(System.currentTimeMillis()));
                rw.setHumidity(dataReplyMessage.getHumidity());
                rw.setLuminance(dataReplyMessage.getLuminance());
                rw.setNoisePollution(dataReplyMessage.getNoisePollution());
                rw.setWatterPollution(dataReplyMessage.getWatterPollution());
                rw.setTemperature(dataReplyMessage.getTemperature());
                rw.setSensor(s);
                List<QueryWeb> l = new ArrayList<QueryWeb>();
                l.add(queryWeb);
                if(s.getModel().equalsIgnoreCase(SensorConstants.MEMSIC))
                    queryWeb.setAttentedMemsic(true);
                else if(s.getModel().equalsIgnoreCase(SensorConstants.SUNSPOT))
                    queryWeb.setAttentedSunspot(true);
                else if(s.getModel().equalsIgnoreCase(SensorConstants.XBEE))
                    queryWeb.setAttentedXbee(true);
                rw.setQueriesWeb(l);
                Transaction t = session.beginTransaction();
                session.save(rw);
                t.commit();
            }
        } else {
            System.out.println("[GERCOM] [Error] No QueryWeb with id "+dataReplyMessage.getIdPacket());
            AddLoggerExceptionAction alea = new AddLoggerExceptionAction();
            alea.setSession(session);
            alea.setLoggerException(new LoggerException(new Date(System.currentTimeMillis()), "Web DataReply Recebido para uma Query inexistente", "Não se aplica", "ISensorApplication", "Não se aplica"));
        }
        return true;
    }
    
}

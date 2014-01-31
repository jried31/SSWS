/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.test;

import br.ufpa.gercom.sensor4cities.action.database.AddQueryWebAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import java.util.Date;
import org.hibernate.Session;

/**
 *
 * @author leokassio
 */
public class ISensorApplicationTest {
    
    public static void main(String args[]) {
        
        ISensorApplicationSimulator isas = new ISensorApplicationSimulator();
        
        Session session = HibernateUtil.openSession();
        
        AddQueryWebAction addQueryWebAction = new AddQueryWebAction();
        addQueryWebAction.setSession(session);
        for(int x=0;x<5;x++) {
            QueryWeb queryWeb = new QueryWeb();
            queryWeb.setAttentedMemsic(false);
            queryWeb.setAttentedSunspot(false);
            queryWeb.setAttentedXbee(false);
            queryWeb.setMemsic(true);
            queryWeb.setSunspot(true);
            queryWeb.setXbee(true);
            queryWeb.setDateQueryWeb(new Date(System.currentTimeMillis()));
            addQueryWebAction.setQueryWeb(queryWeb);
            addQueryWebAction.execute();
        }
        
        
        
        
        
//        LoadQueryWebPendingAction lqwpa = new LoadQueryWebPendingAction();
//        lqwpa.setModelOfMote(Constants.ID_SUNSPOT);
//        lqwpa.setSession(session);
//        lqwpa.execute();
//        List<QueryWeb> l = lqwpa.getQueryWebs();
//        for(QueryWeb q : l) {
//            for(int x=0;x<5;x++) {
//                DataReplyMessage dataReplyMessage = new DataReplyMessage();
//                dataReplyMessage.setCo2(100);
//                dataReplyMessage.setHumidity(100);
//                int id = Integer.parseInt(q.getIdQueryWeb()+"");
//                dataReplyMessage.setIdPacket(id);
//                dataReplyMessage.setLuminance(100);
//                dataReplyMessage.setNoisePollution(100);
//                dataReplyMessage.setSourceQuery(Sensor4CitiesMessage.WEB_QUERY);
//                dataReplyMessage.setTemperature(100);
//                dataReplyMessage.setWatterPollution(100);
//                isas.saveDataReplyMessage(session, dataReplyMessage, "0014.4F01.0000.78D2");
//            }
//        }
        
        
    }
}

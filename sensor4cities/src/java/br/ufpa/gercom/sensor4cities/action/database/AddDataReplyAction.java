/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.database;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.dao.Dao;
import br.ufpa.gercom.sensor4cities.wsn.messages.datareport.DataReplyMessage;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;

/**
 * Action desenvolvida para encaminhamento das mensagens Replies recebidas,
 * esta action persiste junto ao banco uma lista de Replies que correspondem
 * a uma lista de Queries. Action utilizada para facilitar a implementação
 * nas basestations. (nome anterior ForwardReplyWebAction)
 * @author leokassio
 */
public class AddDataReplyAction implements IAction{
    
    private List<QueryWeb> queryWebs;
    private DataReplyMessage dataReplyMessage;
    private Sensor sensor;
    private Session session;

    public AddDataReplyAction() {
    }

    public AddDataReplyAction(List<QueryWeb> queryWebs, DataReplyMessage dataReplyMessage, Sensor sensor, Session session) {
        this.queryWebs = queryWebs;
        this.dataReplyMessage = dataReplyMessage;
        this.sensor = sensor;
        this.session = session;
    }
    
    public String execute() {
        Dao rdao = new Dao(ReplyWeb.class, session);
        Dao qdao = new Dao(QueryWeb.class, session);
        Date date = new Date(System.currentTimeMillis());
        ReplyWeb replyWeb = new ReplyWeb(null, (double) dataReplyMessage.getTemperature() , (double) dataReplyMessage.getHumidity(), (double) dataReplyMessage.getLuminance(), (double) dataReplyMessage.getCo2(), (double) dataReplyMessage.getWatterPollution(), (double) dataReplyMessage.getNoisePollution(), date, queryWebs, sensor);
        rdao.save(replyWeb);
        for(QueryWeb q : queryWebs) {
            if(sensor.getModel().equals(SensorConstants.SUNSPOT))
                q.setAttentedSunspot(true);
            else if(sensor.getModel().equals(SensorConstants.MEMSIC))
                q.setAttentedMemsic(true);
            else if(sensor.getModel().equals(SensorConstants.XBEE))
                q.setAttentedXbee(true);
            qdao.save(q);
        }
        return "ok";
    }

    public void setDataReplyMessage(DataReplyMessage dataReplyMessage) {
        this.dataReplyMessage = dataReplyMessage;
    }

    public void setQueryWebs(List<QueryWeb> queryWebs) {
        this.queryWebs = queryWebs;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

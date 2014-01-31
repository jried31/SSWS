/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.action.view;

import br.ufpa.gercom.sensor4cities.action.IAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadSensorAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastReplyWebBySensorAction;
import br.ufpa.gercom.sensor4cities.managedbean.ConstantsBean;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import br.ufpa.gercom.sensor4cities.wsn.SensorConstants;
import java.util.Collection;
import java.util.HashMap;
import org.hibernate.Session;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * Action desenvolvida para tratamento dos dados da última requisição, é
 * responsável pelo formatação dos dados para a plotagem do gráfico de barras.
 * @author leokassio
 */
public class PlotLastReplyWebAction implements IAction {

    private int phenomena;
    private Session session;
    private CartesianChartModel cartesianChartModel;

    public PlotLastReplyWebAction() {
    }

    public PlotLastReplyWebAction(int phenomena, Session session) {
        this.phenomena = phenomena;
        this.session = session;
        cartesianChartModel = new CartesianChartModel();
    }
    
    public String execute() {
        LoadSensorAction loadSensorAction = new LoadSensorAction();
        loadSensorAction.setOnline(true);
        loadSensorAction.setSession(session);
        loadSensorAction.execute();
        
        LoadLastReplyWebBySensorAction llrwbsa = new LoadLastReplyWebBySensorAction();
        llrwbsa.setSession(session);
        llrwbsa.setSensors(loadSensorAction.getSensors());
        llrwbsa.execute();
        HashMap<Sensor, ReplyWeb> replies = llrwbsa.getReplies();
        Collection<Sensor> keys = replies.keySet();
        ChartSeries cs = new ChartSeries();
        
        switch(phenomena) {
                case SensorConstants.TEMPERATURE: 
                    cs.setLabel("Temperatura");
                    break;
                case SensorConstants.LUMINANCE:
                    cs.setLabel("Luminância");
                    break;
                case SensorConstants.HUMIDITY:
                    cs.setLabel("Umidade");
                    break;
                case SensorConstants.CO2:
                    cs.setLabel("CO2");
                    break;
                case SensorConstants.WATTER_POLLUTION:
                    cs.setLabel("Poluição Aquática");
                    break;
                case SensorConstants.NOISE_POLLUTION:
                    cs.setLabel("Poluição Sonora");
                    break;
            }
        
        Double dataSensed = null;
        
        for (Sensor s : keys) {
            switch(phenomena) {
                case SensorConstants.TEMPERATURE: 
                    dataSensed = replies.get(s).getTemperature();
                    break;
                case SensorConstants.LUMINANCE:
                    dataSensed = replies.get(s).getLuminance();
                    break;
                case SensorConstants.HUMIDITY:
                    dataSensed = replies.get(s).getHumidity();
                    break;
                case SensorConstants.CO2:
                    dataSensed = replies.get(s).getCo2();
                    break;
                case SensorConstants.WATTER_POLLUTION:
                    dataSensed = replies.get(s).getWatterPollution();
                    break;
                case SensorConstants.NOISE_POLLUTION:
                    dataSensed = replies.get(s).getNoisePollution();
                    break;                 
            }
            cs.set(s.getName(), dataSensed);
        }
        cartesianChartModel.addSeries(cs);
        return "ok";
    }

    public CartesianChartModel getCartesianChartModel() {
        return cartesianChartModel;
    }

    public void setPhenomena(int phenomena) {
        this.phenomena = phenomena;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    
}

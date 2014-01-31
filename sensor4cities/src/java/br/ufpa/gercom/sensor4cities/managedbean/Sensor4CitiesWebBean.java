/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.action.database.AddQueryWebAction;
import br.ufpa.gercom.sensor4cities.action.view.PlotLastReplyWebAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastReplyWebBySensorAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadSensorAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.map.Circle;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author leokassio
 */
public class Sensor4CitiesWebBean implements Serializable {

    private List<Sensor> sensors; //Lista de sensores persistido no banco.
    private QueryWeb queryWeb; //Query que será persistida ao banco.
    private MapModel mapModel; //Modelo de mapa que será carregado e exibido no GoogleMpas
    private Marker marker; //Marcação do mapa selecionada pelo usuário para exibir informações do ponto.
    private int phenomenaSelectedId; //Fenomeno selecionado para exibicao dos replies. Utiliza a classe Constants
    public Double defaultLongitude;
    public Double defaultLatitude;
    public Integer defaultZoom;
    private Date date;
    private SimpleDateFormat dateFormat;
    private Boolean renderedMap; //flag utilizada para aliviar a carga no processamento da app mobile
    private Boolean advancedUser;
    
    private CartesianChartModel cartesianChartModel;
    
    public Sensor4CitiesWebBean() {
        sensors = new ArrayList<Sensor>();
        mapModel = new DefaultMapModel();
        queryWeb = new QueryWeb();
        defaultLongitude = -1.474258; //TODO Desenvolver uma tabela para persitência destes dados
        defaultLatitude = -48.453741;
        defaultZoom = 17;
        dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        cartesianChartModel = new CartesianChartModel();
        renderedMap = false;
        advancedUser = false;
    }

    public void loadSensors() {
        renderedMap = true;
        mapModel = new DefaultMapModel();
        Session session = HibernateUtil.openSession();
        
        LoadSensorAction loadSensorAction = new LoadSensorAction();
        loadSensorAction.setOnline(true);        
        loadSensorAction.setSession(session);
        loadSensorAction.execute();
        sensors = loadSensorAction.getSensors();
        
        LoadLastReplyWebBySensorAction llrwbsa = new LoadLastReplyWebBySensorAction();
        llrwbsa.setSession(session);
        llrwbsa.setSensors(sensors);
        llrwbsa.setLastMinutes(true);
        llrwbsa.execute();
        HashMap<Sensor, ReplyWeb> hm = llrwbsa.getReplies(); //o hashmap contem apenas sensores que tiveram respostas
        for (Sensor sensor : sensors) { //percorre todos os sensores online
            if(hm.containsKey(sensor)) {
                ReplyWeb rw = hm.get(sensor);
                LatLng latLng = new LatLng(sensor.getLatitude(), sensor.getLongitude());
                date = rw.getDateReply();
                //antes da temperatura: "ID " + rw.getIdReplyWeb() + 
                Marker mkr = new Marker(latLng, dateFormat.format(date), "The weather is cloudy and with possibility of rain. Temp. "+rw.getTemperature()+"C, Humid. "+rw.getHumidity()+"%, Lumi. "+rw.getLuminance()+"lx", "images/ok.png", sensor.getIcon());
                //Marker mkr = new Marker(latLng, sensor.getModel()+" "+sensor.getName() + " | " +dateFormat.format(date), " Temp. "+rw.getTemperature()+"C, Humid. "+rw.getHumidity()+"%, Lumi. "+rw.getLuminance()+"lx", sensor.getIcon());
                Circle c = new Circle(latLng, 30);
                c.setFillColor("#00ff00");
                c.setStrokeColor("00ff00");
                c.setFillOpacity(0.2);
                c.setFillOpacity(0.2);
                mapModel.addOverlay(mkr);
                mapModel.addOverlay(c);
            } else {
                LatLng latLng = new LatLng(sensor.getLatitude(), sensor.getLongitude());
                date = new Date(System.currentTimeMillis());
                Marker mkr = new Marker(latLng, sensor.getModel()+" | " +dateFormat.format(date), "There is no recent data from this node", sensor.getIcon());
                //Marker mkr = new Marker(latLng, sensor.getModel()+" "+sensor.getName() + " | " +dateFormat.format(date), "There is no recent data from this node", sensor.getIcon());
                Circle c = new Circle(latLng, 30);
                c.setFillColor("#ff0000");
                c.setStrokeColor("ff0000");
                c.setFillOpacity(0.2);
                c.setFillOpacity(0.2);
                mapModel.addOverlay(mkr);
                mapModel.addOverlay(c);
            }
                
        }
        List<Marker> list = getServerMarkers();
        for (Marker marker1 : list) {
            mapModel.addOverlay(marker1);            
        }
        session.close();
    }
    
    public void loadCommonData() {
        renderedMap = true;
        mapModel = new DefaultMapModel();
        Session session = HibernateUtil.openSession();
        
        LoadSensorAction loadSensorAction = new LoadSensorAction();
        loadSensorAction.setOnline(true);        
        loadSensorAction.setSession(session);
        loadSensorAction.execute();
        sensors = loadSensorAction.getSensors();
        
        LoadLastReplyWebBySensorAction llrwbsa = new LoadLastReplyWebBySensorAction();
        llrwbsa.setSession(session);
        llrwbsa.setSensors(sensors);
        llrwbsa.setLastMinutes(true);
        llrwbsa.execute();
        HashMap<Sensor, ReplyWeb> hm = llrwbsa.getReplies(); //o hashmap contem apenas sensores que tiveram respostas
        for (Sensor sensor : sensors) { //percorre todos os sensores online
            if(hm.containsKey(sensor)) {
                ReplyWeb rw = hm.get(sensor);
                LatLng latLng = new LatLng(sensor.getLatitude(), sensor.getLongitude());
                date = rw.getDateReply();
                Marker mkr = new Marker(latLng, dateFormat.format(date), "The weather is cloudy and with possibility of rain. Temp. "+rw.getTemperature()+"C, Humid. "+rw.getHumidity()+"%, Lumi. "+rw.getLuminance()+"lx", "images/ok.png");
                Circle c = new Circle(latLng, 30);
                c.setFillColor("#00ff00");
                c.setStrokeColor("00ff00");
                c.setFillOpacity(0.2);
                c.setFillOpacity(0.2);
                mapModel.addOverlay(mkr);
                mapModel.addOverlay(c);
            } else {
                LatLng latLng = new LatLng(sensor.getLatitude(), sensor.getLongitude());
                date = new Date(System.currentTimeMillis());
                //não há respostas desta localidade
                Marker mkr = new Marker(latLng, dateFormat.format(date), "There is no recent data", "images/error.png");
                Circle c = new Circle(latLng, 30);
                c.setFillColor("#ff0000");
                c.setStrokeColor("ff0000");
                c.setFillOpacity(0.2);
                c.setFillOpacity(0.2);
                mapModel.addOverlay(mkr);
                mapModel.addOverlay(c);
            }
                
        }
        session.close();
    }
    
    public void sendBlankQueryWeb() {
        //TODO add a config segundo a pagina xhtml (painel basico)
        queryWeb.setMemsic(true);
        queryWeb.setSunspot(true);
        Date d = new Date(System.currentTimeMillis());
        queryWeb.setDateQueryWeb(d);
        AddQueryWebAction addQueryWebAction = new AddQueryWebAction();
        addQueryWebAction.setQueryWeb(queryWeb);
        Session session = HibernateUtil.openSession();
        addQueryWebAction.setSession(session);
        addQueryWebAction.execute();
        session.close();
        queryWeb = new QueryWeb();
        //Requisição registrada com suceso!
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request successfully registered!", "Sensor4Cities"));
    }
    
    public void sendQueryWeb() {
        //TODO add a config segundo a pagina xhtml (painel avançado)
        if(queryWeb.getSunspot() == false && queryWeb.getMemsic() == false && queryWeb.getXbee() == false) {
            //Escolha um tipo de mote!
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Choose at least one node type!", "Sensor4Cities Erro"));
            return;
        }
        Date d = new Date(System.currentTimeMillis());
        queryWeb.setDateQueryWeb(d);
        AddQueryWebAction addQueryWebAction = new AddQueryWebAction();
        addQueryWebAction.setQueryWeb(queryWeb);
        Session session = HibernateUtil.openSession();
        addQueryWebAction.setSession(session);
        addQueryWebAction.execute();
        session.close();
        queryWeb = new QueryWeb();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Request successfully registered!", "Sensor4Cities"));
    }
    
    public void sendMobileQueryWeb() {
        queryWeb = new QueryWeb();
        queryWeb.setMemsic(true);
        queryWeb.setSunspot(true);
        queryWeb.setXbee(true);
        Date d = new Date(System.currentTimeMillis());
        queryWeb.setDateQueryWeb(d);
        AddQueryWebAction addQueryWebAction = new AddQueryWebAction();
        addQueryWebAction.setQueryWeb(queryWeb);
        Session session = HibernateUtil.openSession();
        addQueryWebAction.setSession(session);
        addQueryWebAction.execute();
        session.close();
        queryWeb = new QueryWeb();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Requisição registrada com suceso!", "Sensor4Cities"));
    }
    
    public List<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(List<Sensor> sensors) {
        this.sensors = sensors;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = (Marker) marker;
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
        try {
            marker = (Marker) event.getOverlay();
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public QueryWeb getQueryWeb() {
        return queryWeb;
    }

    public void setQueryWeb(QueryWeb queryWeb) {
        this.queryWeb = queryWeb;
    }

    public CartesianChartModel getCartesianChartModel() {
        return cartesianChartModel;
    }

    public void setCartesianChartModel(CartesianChartModel cartesianChartModel) {
        this.cartesianChartModel = cartesianChartModel;
    }

    public void showChart() {
        Session session = HibernateUtil.openSession();
        PlotLastReplyWebAction plot = new PlotLastReplyWebAction(phenomenaSelectedId, session);
        plot.execute();
        cartesianChartModel = plot.getCartesianChartModel();
        session.close();
    }

    public int getPhenomenaSelectedId() {
        return phenomenaSelectedId;
    }

    public void setPhenomenaSelectedId(int phenomenaSelectedId) {
        this.phenomenaSelectedId = phenomenaSelectedId;
    }

    public Double getDefaultLatitude() {
        return defaultLatitude;
    }

    public void setDefaultLatitude(Double defaultLatitude) {
        this.defaultLatitude = defaultLatitude;
    }

    public Double getDefaultLongitude() {
        return defaultLongitude;
    }

    public void setDefaultLongitude(Double defaultLongitude) {
        this.defaultLongitude = defaultLongitude;
    }

    public Integer getDefaultZoom() {
        return defaultZoom;
    }

    public void setDefaultZoom(Integer defaultZoom) {
        this.defaultZoom = defaultZoom;
    }

    public Boolean getRenderedMap() {
        return renderedMap;
    }

    public void setRenderedMap(Boolean renderedMap) {
        this.renderedMap = renderedMap;
    }
    
    public static List<Marker> getServerMarkers() { //TODO Criar uma tabela para persistência de infra estrutura de rede
        LatLng latLng1 = new LatLng(-1.474371,-48.456182);
        LatLng latLng2 = new LatLng(-1.473996,-48.452083);
        LatLng latLng3 = new LatLng(-1.466689,-48.446891);
        //Marker marker1 = new Marker(latLng1, "GERCOM - Instituto de Ciências Naturais", "Universidade Federal do Pará - Belém", "images/server.png");
        //Marker marker2 = new Marker(latLng2, "GERCOM - Faculdade de Engenharia de Computação", "Universidade Federal do Pará - Belém", "images/server.png");
        //Marker marker3 = new Marker(latLng3, "GERCOM - Centro de Eficiência Energética da Amazônia", "Universidade Federal do Pará - Belém", "images/server.png");
        ArrayList<Marker> l = new ArrayList<Marker>();
        //l.add(marker1);
        //l.add(marker2);
        //l.add(marker3);
        return l;
    }
}

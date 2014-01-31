/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.managedbean;

import br.ufpa.gercom.sensor4cities.action.database.AddSensorAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastLoggerExceptionsAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastQueryTwitterAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastQueryWebAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastReplyTwitterAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadLastReplyWebAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadTwitterConfigAction;
import br.ufpa.gercom.sensor4cities.apps.SimulationApp;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import br.ufpa.gercom.sensor4cities.model.QueryWeb;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import br.ufpa.gercom.sensor4cities.model.ReplyWeb;
import br.ufpa.gercom.sensor4cities.model.Sensor;
import br.ufpa.gercom.sensor4cities.model.TwitterConfig;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import br.ufpa.gercom.sensor4cities.model.LoggerException;
import java.io.Serializable;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.primefaces.event.map.PointSelectEvent;
import org.primefaces.model.map.LatLng;

/**
 *
 * @author leokassio
 */
public class ManagerBean  implements Serializable {
    private Sensor sensor;
    private Double defaultLongitude;
    private Double defaultLatitude;
    private int defaultZoom;
    private List<QueryWeb> queryWeb;
    private List<QueryTwitter> queryTwitter;
    private List<ReplyWeb> replyWeb;
    private List<ReplyTwitter> replyTwitter;
    private List<LoggerException> loggerException;
    private TwitterConfig twitterConfig;
    private TwitterApp twitterApp;
    private SimulationApp simulationApp;

    public ManagerBean() {
        sensor = new Sensor();
        defaultLongitude = -1.474258;
        defaultLatitude = -48.453741;
        defaultZoom = 17;
        refreshQueryWeb();
        refreshQueryTwitter();
        refreshReplyTwitter();
        refreshReplyWeb();
        loadTwitterConfig();
    }
        
    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    
    public void addSensor() {
        try {
            AddSensorAction addSensorAction = new AddSensorAction();
            sensor.setOnline(true);
            addSensorAction.setSensor(sensor);
            Session session = HibernateUtil.openSession();
            addSensorAction.setSession(session);
            addSensorAction.execute();
            session.close();
            sensor = new Sensor();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrado com Sucesso, obrigado!", "Sensor4Cities"));
        } catch(Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ops!", "Ocorreu um erro!"));
        }
        
    }
    
    public void onPointSelect(PointSelectEvent event) {
        LatLng latlng = event.getLatLng();
        sensor.setLatitude(latlng.getLat());
        sensor.setLongitude(latlng.getLng());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Geolocalização Selecionada", "Lat:" + latlng.getLat() + ", Lng:" + latlng.getLng()));
    }
    
    public void refreshQueryWeb() {
        Session session = HibernateUtil.openSession();
        LoadLastQueryWebAction llqwa = new LoadLastQueryWebAction(session);
        llqwa.execute();
        queryWeb = llqwa.getQueriesweb();
        session.close();
    }
    
    public void refreshQueryTwitter() {
        Session session = HibernateUtil.openSession();
        LoadLastQueryTwitterAction llqta = new LoadLastQueryTwitterAction();
        llqta.setSession(session);
        llqta.execute();
        queryTwitter = llqta.getQueryTwitter();
        session.close();
    }

    private void loadTwitterConfig() {
        Session session = HibernateUtil.openSession();
        LoadTwitterConfigAction ltca = new LoadTwitterConfigAction();
        ltca.setSession(session);
        ltca.setTwitterUser(TwitterApp.TWITTER_USER);
        ltca.execute();
        twitterConfig = ltca.getTwitterConfig();
        if(twitterConfig == null)
            twitterConfig = new TwitterConfig();
        session.close();
    }
    
    public void refreshReplyWeb() {
        Session session = HibernateUtil.openSession();
        LoadLastReplyWebAction llrwa = new LoadLastReplyWebAction();
        llrwa.setSession(session);
        llrwa.execute();
        replyWeb = llrwa.getReplyWeb();
        session.close();
    }
    
    public void refreshReplyTwitter() {
        Session session = HibernateUtil.openSession();
        LoadLastReplyTwitterAction llrta = new LoadLastReplyTwitterAction();
        llrta.setSession(session);
        llrta.execute();
        replyTwitter = llrta.getReplyTwitter();
        session.close();
    }
    
    public void refreshException() {
        Session session = HibernateUtil.openSession();
        LoadLastLoggerExceptionsAction lllea = new LoadLastLoggerExceptionsAction();
        lllea.setSession(session);
        lllea.execute();
        loggerException = lllea.getLoggerException();
        session.close();
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

    public int getDefaultZoom() {
        return defaultZoom;
    }

    public void setDefaultZoom(int defaultZoom) {
        this.defaultZoom = defaultZoom;
    }

    public List<QueryTwitter> getQueryTwitter() {
        return queryTwitter;
    }

    public void setQueryTwitter(List<QueryTwitter> queryTwitter) {
        this.queryTwitter = queryTwitter;
    }

    public List<QueryWeb> getQueryWeb() {
        return queryWeb;
    }

    public void setQueryWeb(List<QueryWeb> queryWeb) {
        this.queryWeb = queryWeb;
    }

    public TwitterConfig getTwitterConfig() {
        return twitterConfig;
    }

    public void setTwitterConfig(TwitterConfig twitterConfig) {
        this.twitterConfig = twitterConfig;
    }

    public String getHashtag() {
        return TwitterApp.HASHTAG;
    }

    public void setHashtag(String hashtag) {    }

    public List<ReplyTwitter> getReplyTwitter() {
        return replyTwitter;
    }

    public void setReplyTwitter(List<ReplyTwitter> replyTwitter) {
        this.replyTwitter = replyTwitter;
    }

    public List<ReplyWeb> getReplyWeb() {
        return replyWeb;
    }

    public void setReplyWeb(List<ReplyWeb> replyWeb) {
        this.replyWeb = replyWeb;
    }

    public List<LoggerException> getLoggerException() {
        return loggerException;
    }

    public void setLoggerException(List<LoggerException> loggerException) {
        this.loggerException = loggerException;
    }
    
    public void garbageCollector() {
        System.gc();
    }
    
}

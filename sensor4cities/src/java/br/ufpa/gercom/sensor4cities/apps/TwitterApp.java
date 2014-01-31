/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.apps;

import br.ufpa.gercom.sensor4cities.apps.twitter.TwitterConnector;
import br.ufpa.gercom.sensor4cities.apps.twitter.TwitterQueryThread;
import br.ufpa.gercom.sensor4cities.apps.twitter.TwitterReplyThread;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Twitter;

/**
 *
 * @author leokassio
 */
public class TwitterApp implements ISensor4CitiesApp {
    
    public static final String HASHTAG = "#sensor"; //WARNING WARNING WARNING WARNING
    public static final String CONSUMER_KEY = "oGdlDqWyawu1g4CPQbWMSg"; //WARNING WARNING WARNING WARNING
    public static final String CONSUMER_SECRET = "V2r3lyU2BmSt4i591m9hOy5jCzcvs0XPo3DrZ8A"; //WARNING WARNING
    public static final String TWITTER_USER = "sensor4cities"; //WARNING WARNING WARNING WARNING WARNING WARNING
    public static final String CITY = "Belém"; //WARNING WARNING WARNING WARNING WARNING WARNING WARNING WARNING
    
    private static TwitterApp instance;
    private TwitterQueryThread queryThread;
    private TwitterReplyThread replyThread;

    private TwitterApp() {    }
    
    public static TwitterApp getInstance() {
        if(instance == null) {
            instance = new TwitterApp();
        }
        return instance;
    }
    
    public void finishApp() {
        queryThread.setFinish(true);
        replyThread.setFinish(true);
        instance = null;
    }
    
    public void startApp() {
        try {
            TwitterConnector twitterConnector = new TwitterConnector();
            twitterConnector.connect();
            Twitter twitter = twitterConnector.getTwitter();
            queryThread = new TwitterQueryThread(twitter, false);
            replyThread = new TwitterReplyThread();
            queryThread.start();
            replyThread.start();
        } catch (InterruptedException ex) {
            Logger.getLogger(TwitterApp.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }
}

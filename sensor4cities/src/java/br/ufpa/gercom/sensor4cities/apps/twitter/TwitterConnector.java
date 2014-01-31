/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpa.gercom.sensor4cities.apps.twitter;

import br.ufpa.gercom.sensor4cities.action.database.AddTwitterConfig;
import br.ufpa.gercom.sensor4cities.action.database.LoadTwitterConfigAction;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.TwitterConfig;
import java.awt.Desktop;
import java.net.URI;
import java.util.Date;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.http.AccessToken;
import twitter4j.http.RequestToken;

/**
 *
 * @author leokassio
 */
public class TwitterConnector {

    private Twitter twitter;

    public boolean connect() throws InterruptedException {
        twitter = new TwitterFactory().getInstance();
        twitter.setOAuthConsumer(TwitterApp.CONSUMER_KEY, TwitterApp.CONSUMER_SECRET);
        Session session = HibernateUtil.openSession();
        while(true) {
            try {
                RequestToken requestToken = twitter.getOAuthRequestToken();
                AccessToken accessToken = getAccessToken(session,  TwitterApp.TWITTER_USER);
                while (null == accessToken) {
                    Desktop.getDesktop().browse(new URI(requestToken.getAuthorizationURL()));
                    String pin = JOptionPane.showInputDialog(null, "Insert OAuth Twitter PIN", "Twitter", JOptionPane.ERROR_MESSAGE);
                    accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                    if(accessToken!=null)
                        saveAccessToken(session, accessToken.getToken(), accessToken.getTokenSecret());
                }
                
                twitter = new TwitterFactory().getOAuthAuthorizedInstance(TwitterApp.CONSUMER_KEY, TwitterApp.CONSUMER_SECRET, accessToken);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                session.close();
                Thread.sleep(60000);
                return false;
            }
            session.close();
            return true;
        }
    }

    private AccessToken getAccessToken(Session session, String user) throws InterruptedException {
        AccessToken accessToken = null;
        try {
            LoadTwitterConfigAction ltc = new LoadTwitterConfigAction();
            ltc.setSession(session);
            ltc.setTwitterUser(user);
            ltc.execute();
            TwitterConfig tc = ltc.getTwitterConfig();
            if(tc != null) {
                accessToken = new AccessToken(tc.getToken(), tc.getTokenSecret());
            } else {
                System.err.println("[GERCOM] ATENCAO - Nao há token e tokensecret para o usuário registrado - ATENCAO");
            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Thread.sleep(60000);
            return null;
        }
        return accessToken;
    }

    private boolean saveAccessToken(Session session, String token, String tokenSecret) {
        AddTwitterConfig addTwitterConfig = new AddTwitterConfig();
        addTwitterConfig.setSession(session);
        TwitterConfig tc = new TwitterConfig();
        tc.setConsumerKey(TwitterApp.CONSUMER_KEY);
        tc.setConsumerSecret(TwitterApp.CONSUMER_SECRET);
        tc.setTwitterUser(TwitterApp.TWITTER_USER);
        tc.setToken(token);
        tc.setTokenSecret(tokenSecret);
        tc.setValid(true);
        tc.setDateTwitterConfig(new Date(System.currentTimeMillis()));
        addTwitterConfig.setTwitterConfig(tc);
        addTwitterConfig.execute();
        return true;
    }

    public Twitter getTwitter() {
        return twitter;
    }
}

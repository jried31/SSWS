/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpa.gercom.sensor4cities.apps.twitter;

import br.ufpa.gercom.sensor4cities.action.database.LoadReplyTwitterPendingAction;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.ReplyTwitter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 *
 * @author leokassio
 */
public class TwitterReplyThread extends Thread {

    private String[] hashtags;
    private Random random;
    private Session session;
    public boolean finish;
    
    public TwitterReplyThread() {
        super("TwitterReplyThread");
        hashtags = new String[7];
        hashtags[0] = "#iot";
        hashtags[1] = "#rssf";
        hashtags[2] = "#wsn";
        hashtags[3] = "#belem";
        hashtags[4] = "#ufpa";
        hashtags[5] = "#clima";
        hashtags[6] = "#tempo";
        random = new Random(System.currentTimeMillis());
        finish = false;
    }

    @Override
    public void run() {
        kill: while(true) {
            try {
                TwitterConnector twitterConnector = new TwitterConnector();
                twitterConnector.connect();
                Twitter twitter = twitterConnector.getTwitter();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
                
                while(true) {
                    Date date = new Date(System.currentTimeMillis());
                    session = HibernateUtil.openSession();
                    LoadReplyTwitterPendingAction lrtpa = new LoadReplyTwitterPendingAction();
                    lrtpa.setSession(session);
                    lrtpa.execute();
                    Iterator<ReplyTwitter> itr = lrtpa.getRepliesTwitter().iterator();
                    
                    while(itr.hasNext()) {
                        ReplyTwitter r = itr.next();
                        try {
                            twitter.updateStatus(r.getTweet()); //PRIMEIRA TENTATIVA
                            System.err.println("[GERCOM] [Twitter] Tweetting @"+r.getTweet()+" at "+dateFormat.format(date));
                        } catch(TwitterException e) {
                            String hash = hashtags[random.nextInt(6)];
                            r.setTweet(r.getTweet()+" "+hash);
                            try {
                                twitter.updateStatus(r.getTweet()); //SEGUNDA TENTATIVA
                                System.err.println("[GERCOM] [Twitter] Tweetting @"+r.getTweet()+" at "+dateFormat.format(date));
                            } catch(TwitterException ex) {
                                hash = hashtags[random.nextInt(6)];
                                r.setTweet(r.getTweet()+" "+hash);
                                twitter.updateStatus(r.getTweet()); //TERCEIRA TENTATIVA
                                System.err.println("[GERCOM] [Twitter] Tweetting @"+r.getTweet()+" at "+dateFormat.format(date));
                            }
                        }
                        finally {
                            r.setPublished(true);
                            Transaction t = session.beginTransaction();
                            session.save(r);
                            t.commit();
                            Thread.sleep(10000);
                        }
                        
                    }
                    
                    //WARNING WARNING WARNING WARNING WARNING WARNING WARNING
                    session.close(); //WARNING WARNING WARNING WARNING WARNING
                    //WARNING WARNING WARNING WARNING WARNING WARNING WARNING
                    if(finish)
                        break kill;
                    Thread.sleep(10000);
                }
            }catch(Exception e) {
                e.printStackTrace();
                session.close();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(TwitterReplyThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(finish)
                break kill;
        }
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufpa.gercom.sensor4cities.apps.twitter;

import br.ufpa.gercom.sensor4cities.action.database.AddQueryTwitterAction;
import br.ufpa.gercom.sensor4cities.action.database.LoadQueryTwitterByIdStatusAction;
import br.ufpa.gercom.sensor4cities.apps.TwitterApp;
import br.ufpa.gercom.sensor4cities.dao.HibernateUtil;
import br.ufpa.gercom.sensor4cities.model.QueryTwitter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;

/**
 *
 * @author leokassio
 */
public class TwitterQueryThread extends Thread {

    private Twitter twitter;
    private Session session;
    private SimpleDateFormat simpleDateFormat;
    private boolean creatingDatabase;
    private boolean finish;
    
    public TwitterQueryThread(Twitter twitter) {
        super("TwitterQueryThread");
        this.twitter = twitter;
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        finish = false;
    }
    
    public TwitterQueryThread(Twitter twitter, boolean creatingDatabase) {
        super("TwitterQueryThread");
        this.twitter = twitter;
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        this.creatingDatabase = creatingDatabase;
        finish = false;
    }

    @Override
    public void run() {
        kill: while(true) {
            try {
                while(true) {
                    try {
                        all: for(int x=1;;x++) {
                            session = HibernateUtil.openSession();
                            Date date = new Date(System.currentTimeMillis());
                            System.err.println("[GERCOM] [Twitter] Searching for Queries on @"+TwitterApp.TWITTER_USER+" "+ simpleDateFormat.format(date));
                            //just storing the date
                            Paging paging = new Paging(x, 10);
                            List<Status> list = twitter.getMentions(paging);
                            //load stored tweet with pagination
                            if(list.isEmpty()) {
                                System.err.println("[GERCOM] [Twitter] No tweets " + simpleDateFormat.format(date));
                                break all;
                            } //no more tweets
                            //loading the queries by idtweet
                            LoadQueryTwitterByIdStatusAction byStatusIdAction = new LoadQueryTwitterByIdStatusAction();
                            byStatusIdAction.setSession(session);
                            //preparing action for add query on twitter
                            AddQueryTwitterAction aqta = new AddQueryTwitterAction();
                            aqta.setSession(session);
                            //reading the twetts mentions
                            for (Status status : list) {
                                if(!status.getText().contains(TwitterApp.HASHTAG)){
                                    continue;
                                }
                                byStatusIdAction.setIdStatus(status.getId());
                                byStatusIdAction.execute();
                                //if already exists the id status in database. if yes, means the other tweet of paging was stored already in db.
                                if(byStatusIdAction.isExists()) {
                                    if(creatingDatabase) {
                                        System.out.println("[GERCOM] [Twitter] Finishing the database creation proccess " + simpleDateFormat.format(date));
                                        break kill;
                                    }
                                    System.err.println("[GERCOM] [Twitter] No Queries Founded " + simpleDateFormat.format(date));
                                    break all;                                    
                                }
                                else {
                                    QueryTwitter qt = new QueryTwitter();
                                    qt.setDateQuery(status.getCreatedAt());
                                    qt.setAttended(creatingDatabase);
                                    qt.setIdTweet(status.getId());
                                    qt.setTweet(status.getText());
                                    qt.setUserQuery(status.getUser().getScreenName());
                                    aqta.setQueryTwitter(qt);
                                    aqta.execute();
                                    System.err.println("[GERCOM] [Twitter] Adding New Query from @"+qt.getUserQuery()+": "+qt.getTweet() +" "  + simpleDateFormat.format(date));
                                }
                            }
                            session.close();
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        break;
                    }
                    if(finish)
                        break kill;
                    Thread.sleep(60000);
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }
            if(finish)
                break kill;
            try {
                Thread.sleep(60000);
            } catch (Exception ex) {}
        }
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }   

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.queries;

import java.util.List;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.conf.Configuration;/*
import twitter4j.media.ImageUpload;
import twitter4j.media.ImageUploadFactory;
import twitter4j.media.MediaProvider;*/
 
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import twitter4j.Query;
import twitter4j.QueryResult;

//todo: 
//integrate query into create tmp page, respond to tweet

/**
 *
 * @author Victor
 * Function List and Description:
 * private static void populateDupCheck(BufferedReader file)
        runs once, populates dupCheck with data in the file SensiteTwitterQueries.txt
 * private static void writeDupCheck(PrintWriter file)
        runs every time the Bot creates a tweet. rewrites SensiteTwitterQueries.txt 
        based on the hash map
 * private static void handleMessage(Twitter twitter)
        searches twitter for the query, in this case "@sensor4cities", 
        calls checkTweet to get the important components, and calls 
        respondToTweet for every tweet found. Also updates the hash 
        map with relevant values
 * private static void respondToTweet(Twitter twitter, String [] tweetComponents,String respondtoUser)
        makes a new twitter status for each unresponded to tweet. 
        After creating a status, writes to SensiteTwitterQueries.txt
 * private static String[] checkTweet(String tweet)
        regex based function that checks a string and returns all of the components of the 
        tweet: phenomenon, latitude, longitude, and time
 * private static int hashFunc(String name, Date date, String text)
        hash function for the hash map
 * public static void main(String[] args)
 */
public class SensiteTwitterPost {
    private static final int HASH_SIZE = 5000;
    private static String [][] dupCheck =  new String[HASH_SIZE][4];
        //hash map, indexed by hashFunc, that contains the following in the following order: if the tweet was answered (true or false), tweeter, tweet time (of query), tweet text
    private static BufferedReader br; //reader to read SensiteTwitterQueries.txt
    private static PrintWriter pw; //writer that writes to SensiteTwitterQueries.txt
    private static Date lastResponded = new Date();
    
    private static void manageTmpPages(){
        File folder = new File("web/tmp/");
        File[] fileList = folder.listFiles();
        if (fileList != null)
        for( File file : fileList){
            if (file.isFile()){
                Date date = new Date();
                if(date.getTime() - file.lastModified() > 21600000){
                    file.delete();
                }
                
                //System.out.println(file.getName()+" "+file.lastModified() + " "+date.getTime()); 
            }
        }
    }
    
    private static String createTmpPage(int hash, String JSONcontents) {
        PrintWriter tempPage = null;
        String path = "web/tmp/";
        String fileName = path+hash+".html";
        File f = new File(path);
        File fi = new File(fileName);
        f.mkdirs();
        try {
            fi.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }//file path and file created
        try {
            tempPage = new PrintWriter(fileName, "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }

        //tempPage.write("hello temp string here\n"); //PLACEHOLDER, PUT GOTTEN INFO HERE
        tempPage.write("Twitter user: " + dupCheck[hash][1] + "\n<br>Tweet Date: " + dupCheck[hash][2] + "\n<br>Tweet Contents: " + 
                        dupCheck[hash][3] + "\n\n<br><br>Response:\n<br>" + JSONcontents);
        tempPage.close();
        return fileName;
    }
    
    private static void populateDupCheck(BufferedReader file){ 
        int i;
        for(i=0; i<HASH_SIZE; i++){
            String curLine = null;
            try {
                curLine = file.readLine();
            } catch (IOException ex) {
                Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            dupCheck[i][0] = curLine.substring(0,curLine.indexOf(',')); //true false check
            curLine = curLine.substring(curLine.indexOf(',')+1, curLine.length());
            dupCheck[i][1] = curLine.substring(0,curLine.indexOf(',')); //twitter name
            curLine = curLine.substring(curLine.indexOf(',')+1, curLine.length());
            dupCheck[i][2] = curLine.substring(0,curLine.indexOf(',')); //twitter date
            curLine = curLine.substring(curLine.indexOf(',')+1, curLine.length());
            dupCheck[i][3] = curLine; //twitter message

        }
        System.out.println("Successfully populated hash map.");
        try {
            file.close();
        } catch (IOException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void writeDupCheck(PrintWriter file){ 
        try {
            file = new PrintWriter("SensiteTwitterQueries.txt", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(int i=0; i<HASH_SIZE; i++){
            file.println(dupCheck[i][0] + ',' + dupCheck[i][1] + ',' + dupCheck[i][2] + ',' + dupCheck[i][3]);
        }
        System.out.println("Successfully wrote hash map.");
        file.close();
    }

    private static void handleMessage(Twitter twitter) throws TwitterException{ //searches twitter for the query, in this case "@sensor4cities", 
                                                                                  //calls checkTweet to get the important components, and calls 
                                                                                  //respondToTweet for every tweet found. Also updates the hash 
                                                                                  //map with relevant values
        Query query = new Query("@sensor4cities");
        List<String> tweetList = null;
        QueryResult result;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) {                 
                String tweetText = tweet.getText();     
                //check tweet is valid and get the components
                String[] tweetComponents = QueryController.DoParsing(tweetText);//checkTweet(tweetText);
                if(tweetComponents != null){
                    int hash = hashFunc(tweet.getUser().getScreenName(), tweet.getCreatedAt().toString(), tweet.getText());
                    if(dupCheck[hash][0].contains("false")){
                        if(tweet.getCreatedAt().compareTo(lastResponded)>0){
                            lastResponded = tweet.getCreatedAt();
                            dupCheck[hash][0] = "true";
                            dupCheck[hash][1] = tweet.getUser().getScreenName();
                            dupCheck[hash][2] = tweet.getCreatedAt().toString();
                            dupCheck[hash][3] = tweet.getText();
                            //QueryController.ParseJson(QueryController.SendQuery(tweetComponents))
                            respondToTweet(twitter, tweetComponents, tweet.getUser().getScreenName(),
                                    "", hash);
                        }
                    }
                }
            }
        } while ((query = result.nextQuery()) != null);
    }
    
    private static void respondToTweet(Twitter twitter, String [] tweetComponents     
                                        ,String respondtoUser, String JSONcontents, int hash) throws TwitterException{ 
        String internalURL = createTmpPage(hash, JSONcontents);
        Status status = twitter.updateStatus("@"+respondtoUser+" Here is your link to data: " + internalURL + "... phenom: "+tweetComponents[0]+", long:"
                                                +tweetComponents[1]+", lat:"+tweetComponents[2]+", time:"+tweetComponents[3]);
        //System.out.println("@"+respondtoUser+" Here is your link to data: " + internalURL + "... phenom: "+tweetComponents[0]+", long:"
        //                                        +tweetComponents[1]+", lat:"+tweetComponents[2]+", time:"+tweetComponents[3]);
        writeDupCheck(pw);
    }

    private static int hashFunc(String name, String date, String text){ 
        int hash = 1;
        hash = 29 + name.hashCode();
        hash = 59 * hash + 17 * date.hashCode();
        hash = 71 * hash + 43 * text.hashCode();
        if (hash<0)
            hash = -hash;
        return hash % HASH_SIZE;
    }
    
    public static void startBot() { //mostly copied from twitter4j examples
        //int tmptest = 21347862;
        //System.out.println(createTmpPage(tmptest));
        //manageTmpPages();
/*        for (String tmp : QueryController.DoParsing("#ssphen$11,12$time")){
            System.out.println(tmp);
        }
        return;
  */      
        String testStatus="Hello from twitter4j, post 3";
        try {
            br = new BufferedReader(new FileReader("SensiteTwitterQueries.txt"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        
 
        ConfigurationBuilder cb = new ConfigurationBuilder();
         
         
        //the following is set without accesstoken- desktop client
        cb.setDebugEnabled(true)
      .setOAuthConsumerKey("ob9uqrxySZ54Apf9zdVafQ")
      .setOAuthConsumerSecret("n6JrxwY7ngML2Ey0776fTUBVjgrDywvg0mNII5O6Eg");
   
        try {
            TwitterFactory tf = new TwitterFactory(cb.build());
            Twitter twitter = tf.getInstance();
             
            try {
                System.out.println("-----");
 
                // get request token.
                // this will throw IllegalStateException if access token is already available
                // this is oob, desktop client version
                RequestToken requestToken = twitter.getOAuthRequestToken(); 
 
                System.out.println("Got request token.");
                System.out.println("Request token: " + requestToken.getToken());
                System.out.println("Request token secret: " + requestToken.getTokenSecret());
 
                System.out.println("|-----");
 
                AccessToken accessToken = null;
 
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                 
                while (null == accessToken) {
                    System.out.println("Open the following URL and grant access to your account:");
                    System.out.println(requestToken.getAuthorizationURL());
                    System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
                    String pin = br.readLine();
                
                    try {
                        if (pin.length() > 0) {
                            accessToken = twitter.getOAuthAccessToken(requestToken, pin);
                        } else {
                            accessToken = twitter.getOAuthAccessToken(requestToken);
                        }
                    } catch (TwitterException te) {
                        if (401 == te.getStatusCode()) {
                            System.out.println("Unable to get the access token.");
                        } else {
                            te.printStackTrace();
                        }
                    }
                }
                System.out.println("Got access token.");
                System.out.println("Access token: " + accessToken.getToken());
                System.out.println("Access token secret: " + accessToken.getTokenSecret());
                 
            } catch (IllegalStateException ie) {
                // access token is already available, or consumer key/secret is not set.
                if (!twitter.getAuthorization().isEnabled()) {
                    System.out.println("OAuth consumer key/secret is not set.");
                    System.exit(-1);
                }
            }
             
           /*Status status = twitter.updateStatus(testStatus);
 
           System.out.println("Successfully updated the status to [" + status.getText() + "].");
 
           System.out.println("ready exit");*/
           //Twitter twitter = TwitterFactory.getSingleton();
           
            //TIMELINE STUFF
            /*List<Status> statuses = twitter.getHomeTimeline();
           System.out.println("Showing home timeline.");
           for (Status status : statuses) {
                System.out.println(status.getUser().getName() + ":" +
                                                    status.getText());
           }*/
                
            //Arrays.fill(dupCheck,false);
            
            populateDupCheck(br);
            
            try{
                while(true){
                    manageTmpPages();
                    handleMessage(twitter);
                    Thread.sleep(10*1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            System.exit(0);
        } catch (TwitterException te) {
            te.printStackTrace();
            System.out.println("Failed to get timeline: " + te.getMessage());
            System.exit(-1);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Failed to read the system input.");
            System.exit(-1);
        }
    }
}
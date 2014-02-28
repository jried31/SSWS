/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import twitter4j.Query;
import twitter4j.QueryResult;
/**
 *
 * @author Victor
 */
public class SensiteTwitterPost {
    private static final int HASH_SIZE = 5000;
    private static String [][] dupCheck =  new String[HASH_SIZE][4];
    private static BufferedReader br;
    private static PrintWriter pw;
        
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

            //System.out.println("index " + i + " , " + dupCheck[i][0] + " , " + dupCheck[i][1] + " , " + dupCheck[i][2] + " , " + dupCheck[i][3]);
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

    private static void handleMessage(Twitter twitter) throws TwitterException{
        //boolean [] dupCheck = new boolean[HASH_SIZE];
        //Arrays.fill(dupCheck,false);
        
        Query query = new Query("@sensor4cities");
        List<String> tweetList = null;
        QueryResult result;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) { 
                //System.out.println(tweet.getUser().getScreenName() + " $sensite$ " + tweet.getCreatedAt() + " $sensite$ " + tweet.getText());
                
                String tweetText = tweet.getText();
                
                //check tweet is valid
                String[] tweetComponents = checkTweet(tweetText);
                if(tweetComponents != null){
                    int hash = hashFunc(tweet.getUser().getScreenName(), tweet.getCreatedAt(), tweet.getText());
                    //System.out.println("doing dup check..." + dupCheck[hash][0]);
                    if(dupCheck[hash][0].contains("false")){
                        dupCheck[hash][0] = "true";
                        dupCheck[hash][1] = tweet.getUser().getScreenName();
                        dupCheck[hash][2] = tweet.getCreatedAt().toString();
                        dupCheck[hash][3] = tweet.getText();
                        //query database
                        //System.out.println("dup check false");
                        respondToTweet(twitter, tweetComponents, tweet.getUser().getScreenName());
                    }
                }
            }
        } while ((query = result.nextQuery()) != null);
    }
    
    private static void respondToTweet(Twitter twitter, String [] tweetComponents
                                        ,String respondtoUser) throws TwitterException{
        Status status = twitter.updateStatus("@"+respondtoUser+" Here is your link to data...tmp not working... phenom: "+tweetComponents[0]+", long:"
                                                +tweetComponents[1]+", lat:"+tweetComponents[2]+", time:"+tweetComponents[3]);
        System.out.println("@"+respondtoUser+" Here is your link to data...tmp not working... phenom: "+tweetComponents[0]+", long:"
                                                +tweetComponents[1]+", lat:"+tweetComponents[2]+", time:"+tweetComponents[3]);
        writeDupCheck(pw);
    }
    
    private static String[] checkTweet(String tweet){
        //tweetStruct retVal = null;
        String [] retVal = new String[4];
        String text = tweet;
        
        //System.out.println("tweet text: "+text);
        
        text.toLowerCase();
        //if(text.contains("#sensor")){;
            String regexmatcher = "(.*)[^\\s]+\\$[0-9.]+,[0-9]+\\$[^\\s]+(.*)";
            if(text.matches(regexmatcher)){ // may need to add 1 level of \
                //messy code because java sucks at regex...
                String [] tmptxt = text.split("[^\\s]+\\$[0-9.]+,[0-9]+\\$[^\\s]+"); // regex doesn't match correctly for date
                int tmplength = 0;
                
                //System.out.println("tweet 0: "+tmptxt[0] + "length: " + tmptxt[0].length());
                if(tmptxt.length == 1){
                    tmplength = 0;
                } else{
                    tmplength = tmptxt[1].length();
                }
                //System.out.println("tweet 1: "+tmptxt[1] + "length: " + tmptxt[1].length());
                
                //if(tmptxt[3] == null){ // if 3rd string, then doesn't match query requirements... can add multiple query logic later
                    String importantInfo;
                    importantInfo = text.substring(tmptxt[0].length(), text.length()-tmplength); // should give substring of regex match
                    //System.out.println(importantInfo);
                    String [] tmparray = importantInfo.split("\\$");
                    //System.out.println(tmparray[0]);
                    //System.out.println(tmparray[1]);
                    //System.out.println(tmparray[2]);
                    //System.out.println("before setphenomenon " + tmparray[0]);
                    //retVal.setPhenomenon(tmparray[0]);
                    //System.out.println("before latlongsplit");
                    
                    String [] latlong = tmparray[1].split("\\,");
                    //System.out.println(latlong[0]);
                    //System.out.println(latlong[1]);
                    
                    retVal[0] = tmparray[0];
                    retVal[1] = latlong[0];
                    retVal[2] = latlong[1];
                    retVal[3] = tmparray[2];
                    //System.out.println("things: ");
                    //System.out.println(retVal[0]);
                    //System.out.println(retVal[1]);
                    //System.out.println(retVal[2]);
                    //System.out.println(retVal[3]);

                    return retVal;
                //}
            }
        //}
        return null;
    }
    
    
    
    private static int hashFunc(String name, Date date, String text){
        int hash = 1;
        hash = 29 + name.hashCode();
        hash = 59 * hash + 17 * date.toString().hashCode();
        hash = 71 * hash + 43 * text.hashCode();
        if (hash<0)
            hash = -hash;
        return hash % HASH_SIZE;
    }
    
    public static void main(String[] args) {
        
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
                    handleMessage(twitter);
                    Thread.sleep(30*1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //handleMessage(twitter); // TO REPLACE WITH INITIALIZE BOT
            
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

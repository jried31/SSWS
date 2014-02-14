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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import twitter4j.Query;
import twitter4j.QueryResult;
/**
 *
 * @author Victor
 */
public class SensiteTwitterPost {
    private static final int HASH_SIZE = 5000;

    private static void handleMessage(Twitter twitter) throws TwitterException{
        boolean [] dupCheck = new boolean[HASH_SIZE];
        Arrays.fill(dupCheck,false);
        
        Query query = new Query("@sensor4cities");
        List<String> tweetList = null;
        QueryResult result;
        do {
            result = twitter.search(query);
            List<Status> tweets = result.getTweets();
            for (Status tweet : tweets) { 
                //System.out.println(tweet.getUser().getScreenName() + " $sensite$ " + tweet.getCreatedAt() + " $sensite$ " + tweet.getText());
                
                //check tweet is valid
                tweetStruct tweetComponents = checkTweet(tweet);
                if(tweetComponents != null){
                    int hash = hashFunc(tweet.getUser().getScreenName(), tweet.getCreatedAt(), tweet.getText());
                    if(dupCheck[hash] == false){
                        dupCheck[hash] = true;
                        //query database
                        respondToTweet(twitter);
                    }
                }
            }
        } while ((query = result.nextQuery()) != null);
    }
    
    private static void respondToTweet(Twitter twitter) throws TwitterException{
        Status status = twitter.updateStatus("Here is your link to data...");
    }
    
    private static tweetStruct checkTweet(Status tweet){
        tweetStruct retVal = null;
        String text = tweet.getText();
        //---------------------------------------------------------
        System.out.println(text);
        System.out.println("DONE GRABBING RAW TWEET DATA...");
        //---------------------------------------------------------
        text.toLowerCase();
        if(text.contains("#sensor")){
            System.out.println("DONE GRABBING #SENSOR");
            //System.out.println(text);
            String regexmatcher = "(.*)[^\\s]+\\$[0-9.]+,[0-9]+\\$[^\\s]+(.*)";
            //System.out.println(regexmatcher);
            if(text.matches(regexmatcher)){ // may need to add 1 level of \
                //messy code because java sucks at regex...
                //----------------------------------------------------
                System.out.println("text did match regex");
                //----------------------------------------------------
                String [] tmptxt = new String[10];
                tmptxt = text.split("(.*)[^\\s]+\\$[0-9.]+,[0-9]+\\$[^\\s]+(.*)"); //regex doesn't match correctly for date
//BROKENASFK------------------------------------------------------------------------------------------------------------------------
                System.out.println("text did match regex x2");
                System.out.println(tmptxt[0]);
                System.out.println(tmptxt[1]);
                System.out.println(tmptxt[2]);
                //if(tmptxt[3] == null){ // if 3rd string, then doesn't match query requirements... can add multiple query logic later
                    String importantInfo;
                    importantInfo = text.substring(tmptxt[0].length(), text.length()-tmptxt[1].length()); // should give substring of regex match
                    String [] tmparray = new String[10];
                    tmparray = importantInfo.split("$");
                    retVal.phenomenon = tmparray[0];
                    String[] latlong = new String[10];
                    latlong = tmparray[1].split(",");
                    retVal.latitude = latlong[0];
                    retVal.longitude = latlong[1];
                    retVal.time = tmparray[2];
                    return retVal;
                //}
            }
        }
        return null;
    }
    
    private class tweetStruct{
        public String phenomenon;
        public String latitude;
        public String longitude;
        public String time;
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
                
            handleMessage(twitter); // TO REPLACE WITH INITIALIZE BOT
            
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

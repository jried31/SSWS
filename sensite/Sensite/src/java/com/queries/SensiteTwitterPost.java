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
        File folder = new File("/root/appDev/apache-tomcat-7.0.52/webapps/tmp/");
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
    
    private static String createTmpPage(int hash, String JSONcontents, String lat, String lon) {
        System.out.println("JSONcontents: " + JSONcontents);
        PrintWriter tempPage = null;
        String path = "/root/appDev/apache-tomcat-7.0.52/webapps/tmp/";
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
        tempPage.write(part1 + lat + part2 + lon + part3 + JSONcontents + part4);
       /* tempPage.write("Twitter user: " + dupCheck[hash][1] + "\n<br>Tweet Date: " + dupCheck[hash][2] + "\n<br>Tweet Contents: " + 
                        dupCheck[hash][3] + "\n\n<br><br>Response:\n<br>");
        tempPage.println(JSONcontents); */
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
                    //tweetComponents[3].
                    //if(dupCheck[hash][0].contains("false")){
                        if(tweet.getCreatedAt().compareTo(lastResponded)>0){
                            lastResponded = tweet.getCreatedAt();
                            dupCheck[hash][0] = "true";
                            dupCheck[hash][1] = tweet.getUser().getScreenName();
                            dupCheck[hash][2] = tweet.getCreatedAt().toString();
                            dupCheck[hash][3] = tweet.getText();
                            //QueryController.ParseJson(QueryController.SendQuery(tweetComponents))
                            respondToTweet(twitter, tweetComponents, tweet.getUser().getScreenName(),
                                    hash);
                            }
                }
            }
        } while ((query = result.nextQuery()) != null);
    }
    
    private static void respondToTweet(Twitter twitter, String [] tweetComponents     
                                        ,String respondtoUser, int hash) throws TwitterException{ 
        String JSONcontents = "If you see this message, then query failed.";
        try {
            JSONcontents = QueryController.ParseJson(QueryController.SendQuery(tweetComponents));
        } catch (IOException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        String internalURL = createTmpPage(hash, JSONcontents, tweetComponents[2], tweetComponents[1]);
        String Str = internalURL.substring(4);
        Status status = twitter.updateStatus("@"+respondtoUser+" Here is your link to data: 108.168.239.92:8080/tmp" + hash + ".html ... phenom: "+tweetComponents[0]+", long:"
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
    
    public static void main(String[] args) { //mostly copied from twitter4j examples
        //int tmptest = 21347862;
        //System.out.println(createTmpPage(tmptest));
        //manageTmpPages();
        //for (String tmp : QueryController.DoParsing("#ssphen$11,12$time")){
        //    System.out.println(tmp);
        //}
/*        String [] tmptxt = new String[4];
        tmptxt[0] = "rain";
        tmptxt[1] = "23";
        tmptxt[2] = "-34";
        tmptxt[3] = "2013-05-23_13%3A32%3A45";
        try {
            System.out.println(QueryController.ParseJson(QueryController.SendQuery(tmptxt)));
        } catch (IOException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(SensiteTwitterPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        return;*/
        
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
    
    private static String part1 = "<!DOCTYPE html>\n" +
"<html>\n" +
"<head>\n" +
"	<title>Sensite</title>\n" +
"	<meta http-equiv=\"content-type\" content=\"text/html;charset=UTF-8\" />\n" +
"	<meta charset=\"utf-8\" />\n" +
"	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\" />\n" +
"	<meta content=\"\" name=\"description\" />\n" +
"	<meta content=\"\" name=\"author\" />\n" +
"\n" +
"	<!--GOOGLE MAP HEAD-->\n" +
"	 <meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />\n" +
"    <style type=\"text/css\">\n" +
"      html { height: 100% }\n" +
"      body { height: 500px; margin: 0; padding: 0 }\n" +
"      #map-canvas { height: 100% }\n" +
"    </style>\n" +
"\n" +
"	<!-- NEED TO WORK ON -->\n" +
"\n" +
"	<link href=\"/Sensite2/assets/plugins/pace/pace-theme-flash.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>\n" +
"	<link href=\"/Sensite2/assets/plugins/jquery-slider/css/jquery.sidr.light.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>\n" +
"	<link href=\"/Sensite2/assets/plugins/boostrapv3/css/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"	<link href=\"/Sensite2/assets/plugins/boostrapv3/css/bootstrap-theme.min.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"	<link href=\"/Sensite2/assets/plugins/font-awesome/css/font-awesome.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"	<link href=\"/Sensite2/assets/css/animate.min.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"	\n" +
"	<link href=\"/Sensite2/assets/css/responsive.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"	<link href=\"/Sensite2/assets/css/custom-icon-set.css\" rel=\"stylesheet\" type=\"text/css\"/>\n" +
"\n" +
"	<!-- BEGIN CORE JS FRAMEWORK--> \n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-1.8.3.min.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/boostrapv3/js/bootstrap.min.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/breakpoints.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-unveil/jquery.unveil.min.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-block-ui/jqueryblockui.js\" type=\"text/javascript\"></script> \n" +
"	<!-- END CORE JS FRAMEWORK --> \n" +
"	<!-- BEGIN PAGE LEVEL JS --> 	\n" +
"\n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-slider/jquery.sidr.min.js\" type=\"text/javascript\"></script> 	\n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-slimscroll/jquery.slimscroll.min.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/plugins/pace/pace.min.js\" type=\"text/javascript\"></script>  \n" +
"	<script src=\"/Sensite2/assets/plugins/jquery-numberAnimate/jquery.animateNumbers.js\" type=\"text/javascript\"></script>\n" +
"	<!-- END PAGE LEVEL PLUGINS --> 	\n" +
"	\n" +
"	<!-- BEGIN CORE TEMPLATE JS --> \n" +
"	<script src=\"/Sensite2/assets/js/core.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/js/chat.js\" type=\"text/javascript\"></script> \n" +
"	<script src=\"/Sensite2/assets/js/demo.js\" type=\"text/javascript\"></script> \n" +
"\n" +
"	<script src=\"/Sensite2/assets/js/displayjson.js\" type=\"text/javascript\"></script>\n" +
"	<script src=\"/Sensite2/assets/js/jsontest.js\" type=\"text/javascript\"></script>\n" +
"    \n" +
"        <script src=\"/Sensite2/assets/plugins/DataTables-1.9.4/media/js/jquery.js\" type =\"text/javascript\"></script>\n" +
"        <script src=\"/Sensite2/assets/plugins/DataTables-1.9.4/media/js/jquery.dataTables.js\" type =\"text/javascript\"></script>\n" +
"        <script src=\"/Sensite2/assets/js/modernizr.js\" type =\"text/javascript\"></script>\n" +
"\n" +
"	<!-- GOOGLE MAPS JS -->\n" +
"	<script type=\"text/javascript\"\n" +
"      src=\"https://maps.googleapis.com/maps/api/js?key=AIzaSyDio-wxQQCPRMjJmQGJV3IsUesvkc_Anfo&sensor=true\">\n" +
"    </script>\n" +
"    <script type=\"text/javascript\">\n" +
"        \n" +
"    var lat ='";
    private static String part2 = "';\n" +
"    var lon ='";
    private static String part3 = "';\n" +
"    var json =";
    private static String part4 = ";    \n" +
"        var j;\n" +
"    var map;\n" +
"      function initialize() {\n" +
"	  $(\"#mapcontainer\").html(\"<div id='map-canvas' style='height:500px; width:1100px' align='center'></div>\");\n" +
"        var mapOptions = {\n" +
"          center: new google.maps.LatLng(lat, lon),\n" +
"          zoom: 15\n" +
"        };\n" +
"        map = new google.maps.Map(document.getElementById(\"map-canvas\"),\n" +
"            mapOptions);\n" +
"	\n" +
"        for( var key =0; key < json.Informations.length; key++)\n" +
"        {\n" +
"        	var myLatlng = new google.maps.LatLng(json.Informations[key].BaseData.location.lat,json.Informations[key].BaseData.location.lon);\n" +
"        	  var marker = new google.maps.Marker({\n" +
"    		  position: myLatlng,\n" +
"    		  map: map,\n" +
"  });\n" +
"        }\n" +
"        \n" +
"      \n" +
"    $(\"#tabledisplay\").append(\"<br><table id ='heytable' class='tftable' border='1'><tr><th>Expand</th><th>Sensor Type</th><th>Data Reading</th><th>Latitude</th><th>Longitude</th><th>Datetime</th></tr></table>\");\n" +
"    for (var key = 0; key < json.Informations.length; key++) \n" +
"    {   \n" +
"\n" +
"      j = JSON.stringify(json.Informations[key]);\n" +
"       $(\"#heytable\").append(\"<tr><td class = 'clickme'><center><h5><b>+</b></h5></center></td><td>\" +json.Informations[key].BaseQoI.DataSource.Sensor.classification.sensorType+\" </td><td>\"+json.Informations[key].BaseData.metric.QuantitativeMetric+\"</td><td>\"+json.Informations[key].BaseData.location.lat+\"</td><td>\"+json.Informations[key].BaseData.location.lon+\"</td><td>\"+json.Informations[key].BaseData.dateTime+\"</td></tr><tr class='hideme'><td colspan='6' style='padding:0px;'><div>\"+j+\"</div></td></tr>\");  \n" +
"  } \n" +
"      \n" +
"        \n" +
"      }\n" +
"      google.maps.event.addDomListener(window, 'load', initialize);\n" +
"      \n" +
"      \n" +
"    \n" +
"      \n" +
"$(window).load(function(){\n" +
"$('.hideme').find('div').hide();\n" +
"$('.clickme').click(function() {\n" +
"    $(this).parent().next('.hideme').find('div').slideToggle(400);\n" +
"    return false;        \n" +
"    });\n" +
"});\n" +
"    \n" +
"    </script>\n" +
"    \n" +
"    <script>\n" +
"        if (!Modernizr.inputtypes.date) {\n" +
"            $(function() {\n" +
"                $( \"#date\" ).datepicker({ dateFormat: 'yy-mm-dd' });\n" +
"                $(\"#time\").attr('placeholder','HH:MM (Military time)');\n" +
"            });\n" +
"        }\n" +
"    </script>\n" +
"	<!-- END CORE TEMPLATE JS --> \n" +
"\n" +
"	<!-- END NEED TO WORK ON -->\n" +
"<style type='text/css'>\n" +
"    .tftable {font-size:12px;color:#333333;width:100%;border-width: 1px;border-color: #729ea5;border-collapse: collapse;}\n" +
"    .tftable th {font-size:12px;background-color:#acc8cc;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;text-align:left;}\n" +
"    .tftable tr {background-color:#d4e3e5;}.tftable td {font-size:12px;border-width: 1px;padding: 8px;border-style: solid;border-color: #729ea5;}\n" +
"    .tftable tr:hover {background-color:#ffffff;}\n" +
"</style>\n" +
"</head>\n" +
"<body class=\"\">\n" +
"	<h1 align=\"center\" style=\"width:80%\"> Sensite</h1>\n" +
"<!-- BEGIN CONTENT -->\n" +
"\n" +
"	<div id=\"mapcontainer\" style=\"height:50%;width:80%; padding-bottom:500px;\" align=\"center\">\n" +
"	\n" +
"	</div>\n" +
"\n" +
"	<div id =\"tabledisplay\" style=\"padding-bottom:35px; width:80%; padding-top:35px\" align=\"center\" >\n" +
"	</div>\n" +
"			<!-- END PLACE PAGE CONTENT HERE -->\n" +
"		</div>\n" +
"	</div>\n" +
"	<!-- END PAGE CONTAINER -->\n" +
"</div>\n" +
"<!-- END CONTENT --> \n" +
"\n" +
"\n" +
"</body>\n" +
"</html>";
    
    
}
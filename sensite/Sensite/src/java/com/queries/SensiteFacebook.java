/**
 *
 * @author Sixiang
 * 
 */
package com.queries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.restfb.exception.FacebookException;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import org.json.JSONException;
            
public class SensiteFacebook {
    
        /*
        
        private static AccessToken GenerateAccessToken(){
            String app_id = "237493219771359";
            String app_secret = "6c15d69456051330d3ab5a943fb1d02d";
            AccessToken accessToken = null;
            try{
               accessToken = new DefaultFacebookClient().obtainAppAccessToken(app_id, app_secret); 
            }catch(FacebookException ex){
                System.out.println("Fail to get access token");
            } 
            return accessToken;
        }
        */
        private static String createTmpPage(int hash, String JSONcontents, String lat, String lon) {
            PrintWriter tempPage = null;
            String path = "web/tmp/";
            String fileName = path+hash+".html";
            File f = new File(path);
            File fi = new File(fileName);
            f.mkdirs();
            try {
                fi.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SensiteFacebook.class.getName()).log(Level.SEVERE, null, ex);
            }//file path and file created
            try {
                tempPage = new PrintWriter(fileName, "UTF-8");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SensiteFacebook.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SensiteFacebook.class.getName()).log(Level.SEVERE, null, ex);
            }

            //tempPage.write("hello temp string here\n"); //PLACEHOLDER, PUT GOTTEN INFO HERE
            tempPage.write(part1 + lat + part2 + lon + part3 + JSONcontents + part4);
            tempPage.close();
            return fileName;
        }
        
        private static String[] getPostMetaData(FacebookClient fb_client, String post_id){
            String[] metadata = new String[3];
            Post cur_post = fb_client.fetchObject(post_id, Post.class);
            metadata[0] = cur_post.getFrom().getName();
            metadata[1] = cur_post.getCreatedTime().toString();
            metadata[2] = cur_post.getMessage();
            return metadata;
        } 
        
        /*
        WriteLastTimeStamp will write time stamp of the
        lastest post to a cache file "FacebookTimeStamp.txt"
        */
        private static void WriteTimeStamp(String time_stamp){
            FileWriter fileWriter = null;
            try{
              File file = new File("FacebookTimeStamp.txt");
              if(!file.exists()){
                  file.createNewFile();
              }
              fileWriter = new FileWriter(file);
              fileWriter.write(time_stamp);
              fileWriter.close();
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    fileWriter.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
                
            }
        }
        
        /*
        ReadLastestTimeStamp will return the cached time stamp string
        */
        private static String ReadLastestTimeStamp(){
            String output = null;
            BufferedReader br = null;
            File f = new File("FacebookTimeStamp.txt");
            if(!f.exists()){
                return null;
             }
            try{
                String cur_line;
                br = new BufferedReader(new FileReader("FacebookTimeStamp.txt"));
                while((cur_line = br.readLine()) != null){
                    if(cur_line != null){
                        output = cur_line;
                    }
                }
            }catch(IOException e){
                e.printStackTrace();
            }finally{
                try{
                    br.close();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
            }
            return output;
        }
        
        /*
        Return all the facebook posts which are posted after given time stamp
        */
        private static Map<String, String> GetNewPosts(String prev_time_stamp, FacebookClient fb_client) throws ParseException{
            Map<String, String> posts_map = new HashMap<String, String>();
            Connection<Post> new_feeds;
            if(prev_time_stamp != null){
                String format = "EEE MMM dd HH:mm:ss z yyyy";
                long timeMills = new SimpleDateFormat(format)
                                 .parse(prev_time_stamp)
                                 .getTime();
                long unixtime = timeMills/1000L;
                new_feeds = fb_client.fetchConnection("496505640454178/feed",
                                                                   Post.class, Parameter.with("since", String.valueOf(unixtime)));
            }
            else{
                new_feeds = fb_client.fetchConnection("496505640454178/feed", Post.class);
            }
            for(Post feed : new_feeds.getData()){
                if(feed.getMessage() != null){
                    posts_map.put(feed.getId(), feed.getMessage());
                }         
            }
            return posts_map;
        }
        
        /*
        
        */
        private static String GetMostRecentUpdateTime(FacebookClient fb_client){
            Date mrts = null;
            Connection<Post> posts = fb_client.fetchConnection("496505640454178/feed", Post.class);
            for(Post feed : posts.getData()){
                if(feed.getMessage()!= null){
                    return feed.getUpdatedTime().toString();
                }              
            }
            return null;
        }
   
        public static void main(String[] args) throws ParseException{
            
            String long_term_access_token = "CAADXZC7MAQ98BAFyCiE9yE1GeFZCZB96eDQ2fGSivS3PMZCEeIrS88DOZC0CdllpynJAO2yf0aGJuxiFNnIUbq2O7v1iuNSxRI8zedxmGDNkl2GQROh5T3J1jgIegeFBwNcgBeN9mT45LhKrpZCKDcYQV8S4xzchXdHEEmtpkJl8SsMmMqhfOZC";
            FacebookClient facebookClient = new DefaultFacebookClient(long_term_access_token); 
            while(true){
                //Load previous time stamp
                String prev_time_stamp = ReadLastestTimeStamp();           
                //Load new posts from Facebook Wall
                Map<String, String> posts_map = GetNewPosts(prev_time_stamp, facebookClient);
                for(Map.Entry<String, String> entry : posts_map.entrySet()){
                   String cur_message = entry.getValue();
                   String cur_post_id = entry.getKey();
                   String send_back = "This is just a test";
                   if(cur_message != null){
                        System.out.println(cur_post_id);
                        System.out.println(cur_message);
;                        String[] parse_result = QueryController.DoParsing(cur_message);
                        
                       //We catch a new query
                       if(parse_result != null){
                           try{
                               String[] metadata = getPostMetaData(facebookClient, cur_post_id);
                               JSONObject json_obj = QueryController.SendQuery(parse_result);
                               String link = createTmpPage(cur_post_id.hashCode(),json_obj.toString(), parse_result[2], parse_result[1]);
                               String Str = link.substring(4);
                               send_back = "Here is the link to your query result: http://108.168.239.92:8080/" + Str;
                           }catch(IOException ioex){
                               ioex.printStackTrace();
                           }catch(JSONException jsonex){
                               jsonex.printStackTrace();
                           }
                       }                       
                       else{
                           send_back = "Please use the correct query format";
                       }
                       FacebookType post_comment = facebookClient.publish(cur_post_id + "/comments", FacebookType.class, 
                                                                               Parameter.with("message", send_back));
                    }
                 }
                 String mrut = GetMostRecentUpdateTime(facebookClient);
                 if(mrut != null){
                    WriteTimeStamp(mrut);   
                 }
                 try{
                   Thread.sleep(5000); 
                 }catch(InterruptedException iex){
                   iex.printStackTrace();
                 }               
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
"	<div id=\"mapcontainer\" style=\"height:50%;width:80%; padding-bottom:40px;\" align=\"center\">\n" +
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

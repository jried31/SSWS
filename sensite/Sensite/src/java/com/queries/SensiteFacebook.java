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
        private static String createTmpPage(int hash, String JSONcontents, String[] PostMetaData) {
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
            tempPage.write("Facebook user: " + PostMetaData[0] + "\n<br>Post Date: " + PostMetaData[1] + "\n<br>Post Contents: " + 
                            PostMetaData[2] + "\n\n<br><br>Response:\n<br>" + JSONcontents);
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
                        String[] parse_result = QueryController.DoParsing(cur_message);   
                       //We catch a new query
                       if(parse_result != null){
                           try{
                               String[] metadata = getPostMetaData(facebookClient, cur_post_id);
                               JSONObject json_obj = QueryController.SendQuery(parse_result);
                               String link = createTmpPage(cur_post_id.hashCode(),json_obj.toString(), metadata);
                               send_back = "Here is the link to your query result: " + link;
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
}


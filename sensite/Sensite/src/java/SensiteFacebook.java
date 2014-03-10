/**
 *
 * @author Sixiang
 */
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
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
        
        /*
        WriteLastTimeStamp will write time stamp of the
        lastest post to a cache file "FacebookTimeStamp.txt"
        */
        private static void WriteTimeStamp(String time_stamp){
            try{
              File file = new File("FacebookTimeStamp.txt");
              if(!file.exists()){
                  file.createNewFile();
              }
              FileOutputStream f = new FileOutputStream(file);
              ObjectOutputStream s = new ObjectOutputStream(f);
              s.writeObject(time_stamp);
              s.close();
            }catch(IOException e){
                e.printStackTrace();
            }        
        }
        
        /*
        ReadLastestTimeStamp will return the cached time stamp string
        */
        private static String ReadLastestTimeStamp(){
            String time_stamp = null;
            ObjectInputStream s = null;
            try{
                File file = new File("SensiteFacebookPostId.txt");
                FileInputStream f = new FileInputStream(file);
                s = new ObjectInputStream(f);
                time_stamp = (String)s.readObject();
            }catch(IOException ex){
                ex.printStackTrace();
            }catch(ClassNotFoundException class_ex){
                class_ex.printStackTrace();
            }finally{
               try{
                   s.close();
               }catch(IOException ioe){
                   ioe.printStackTrace();
               }             
            }
            return time_stamp;
        }
        
        /*
        Return all the facebook posts which are posted after given time stamp
        */
        private static Map<String, String> GetNewPosts(String prev_time_stamp, FacebookClient fb_client) throws ParseException{
            Map<String, String> posts_map = new HashMap<String, String>();
            String format = "yyyy-MM-dd'T'HH:mm:ss+SSSS";
            long timeMills = new SimpleDateFormat(format)
                                 .parse(prev_time_stamp)
                                 .getTime();
            long unixtime = timeMills/1000L;
            Connection<Post> new_feeds = fb_client.fetchConnection("496505640454178/feed",
                                                                   Post.class, Parameter.with("since", String.valueOf(unixtime)));
            for(Post feed : new_feeds.getData()){
                if(feed.getMessage() != null){
                    posts_map.put(feed.getId(), feed.getMessage());
                }         
            }
            return posts_map;
        }
        
        /*
        
        */
        private static Date GetMostRecentUpdateTime(FacebookClient fb_client){
            Date mrts = null;
            Connection<Post> posts = fb_client.fetchConnection("496505640454178/feed", Post.class);
            for(Post feed : posts.getData()){
                if(feed.getMessage()!= null)
                    mrts = feed.getUpdatedTime();
            }
            return mrts;
        }
   
        public static void main(String[] args) throws ParseException{
             //Load previous time stamp
             String prev_time_stamp = ReadLastestTimeStamp();
             //Generate Access Token
             //AccessToken accessToken = GenerateAccessToken();
             String long_term_access_token = "CAADXZC7MAQ98BAFyCiE9yE1GeFZCZB96eDQ2fGSivS3PMZCEeIrS88DOZC0CdllpynJAO2yf0aGJuxiFNnIUbq2O7v1iuNSxRI8zedxmGDNkl2GQROh5T3J1jgIegeFBwNcgBeN9mT45LhKrpZCKDcYQV8S4xzchXdHEEmtpkJl8SsMmMqhfOZC";
             FacebookClient facebookClient = new DefaultFacebookClient(long_term_access_token);
             //Load new posts from Facebook Wall
             Map<String, String> posts_map = GetNewPosts(prev_time_stamp, facebookClient);
             for(Map.Entry<String, String> entry : posts_map.entrySet()){
                String cur_message = entry.getValue();
                String cur_post_id = entry.getValue();
                String send_back = null;
                if(cur_message != null){
                    String[] parse_result = QueryController.DoParsing(cur_message);                    
                    //We catch a new query
                    if(parse_result != null){
                        try{
                            JSONObject json_obj = QueryController.SendQuery(parse_result);
                            send_back = QueryController.ParseJson(json_obj);
                        }catch(IOException ioex){
                            ioex.printStackTrace();
                        }catch(JSONException jsonex){
                            jsonex.printStackTrace();
                        }
                    }                       
                    //otherwise, use cached query 
                    else{
                        send_back = "Please use the correct query format";
                    }            
                    FacebookType post_comment = facebookClient.publish(cur_post_id + "/comments", FacebookType.class, 
                                                                            Parameter.with("message", send_back));
                 }
              }
              Date mrut = GetMostRecentUpdateTime(facebookClient);
              if(mrut != null){
                 WriteTimeStamp(mrut.toString());   
              }
              try{
                Thread.sleep(5000); 
              }catch(InterruptedException iex){
                iex.printStackTrace();
              }
        }
}


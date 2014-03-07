/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sixiang
 */
import java.util.Map;
import java.util.HashMap;
import java.io.File;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import com.restfb.exception.FacebookException;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.FacebookClient;
import com.restfb.types.*;
import org.json.JSONObject;
import org.json.JSONException;
            
public class SensiteFacebook {
    
        /*
        */    
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
        
        private static void WritePostsID(String FBPostID){
            try{
              File file = new File("SensiteFacebookPostId.txt");
              if(!file.exists()){
                  file.createNewFile();
              }
              BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
              bw.write(FBPostID);
              bw.newLine();
              bw.close();
            }catch(IOException e){
                e.printStackTrace();
            }           
        }
        
        private static List<String> ReadPostsID(){
            List<String> FBPostsID = new ArrayList<String>();
            BufferedReader br = null;
            try{
                String cur_line;
                br = new BufferedReader(new FileReader("SensiteFacebookPostId.txt"));
                while((cur_line = br.readLine()) != null){
                    FBPostsID.add(cur_line);
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }finally{
                try{
                    if(br != null) br.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
                
            }
            return FBPostsID;
        }
        
        /*
        CommentPost will take two arguments:the post id and content to comment
        It will use postid to locate specific facebook post and comment that post
        with String content
        */
        private static void CommentPost(String PostID, String Content){
            //TODO: Add implementation
        }
        
        
        public static void main(String[] args) {
                //Load cache file
                List<String> CachePosts = new ArrayList<String>();
                CachePosts = ReadPostsID();
                
                Map<String, String> PostsMap = new HashMap<String,String>();
                //Generate Access Token
                AccessToken accessToken = GenerateAccessToken();
                FacebookClient facebookClient = new DefaultFacebookClient(accessToken.getAccessToken());
                
                //Create a connection and fetch feeds
                Connection<Post> sensite_feeds = facebookClient.fetchConnection("496505640454178/feed", Post.class);
                for(Post feed : sensite_feeds.getData()){
                    String cur_message = feed.getMessage();
                    if(cur_message != null){
                        String[] parse_result = QueryController.DoParsing(cur_message);
                        //Check if current message is already in the cache
                        if((parse_result != null) && !CachePosts.contains(feed.getId())){
                            PostsMap.put(feed.getId(), cur_message);
                            JSONObject json_obj = null;
                            String result = null;
                            try{
                                json_obj = QueryController.SendQuery(parse_result);
                                result = QueryController.ParseJson(json_obj);
                                CommentPost(feed.getId(), result);
                            }catch(IOException ioex){
                                ioex.printStackTrace();
                            }catch(JSONException jsonex){
                                jsonex.printStackTrace();
                            }                           
                        }
                    }
                }
                
        }
}


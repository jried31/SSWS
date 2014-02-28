/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sixiang
 */
import java.util.Iterator;
import java.util.List;
import com.restfb.*;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.Facebook;
import com.restfb.FacebookClient;
import com.restfb.types.*;
            
public class SensiteFacebook {
        
        
        public static void main(String[] args) {
                String sensite_id = "496505640454178";
                String token = "CAACEdEose0cBALLSpIAJffdst4KIuUSSqEescRVIFiqHxXjPoKo3mdkdWMRCbs8X0c8D55vc2ugmqyvFgup2FChmP0DzrsB5ZADLYbehSvX4wlcY7YwMZBfeIYxeSom8ziY3KiZByVeTYvalv0omnxg6YZCYClOFgNO6ZABruIrM6cdE39LafJTKWU3lf5r4ZD";
                FacebookClient facebookClient = new DefaultFacebookClient(token);
		//args[0] is the token obtained from https://developers.facebook.com/tools/explorer

                Connection<Post> sensite_feeds = facebookClient.fetchConnection("496505640454178/feed", Post.class);
                System.out.println("Print all posts on Sensite Facebook Wall");
                System.out.println("...");
                for(Post feed : sensite_feeds.getData())
                    System.out.println(feed.getMessage() + " by " + feed.getFrom().getName());
                
        }
}


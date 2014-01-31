/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.test;

import br.ufpa.gercom.sensor4cities.apps.facebook.User;
import com.restfb.DefaultLegacyFacebookClient;
import com.restfb.LegacyFacebookClient;
import com.restfb.Parameter;
import java.util.List;


/**
 *
 * @author leokassio
 */
public class TestFacebook {

    static final String KEY = "367944629914777";
    static final String SECRET = "5cf3f647376a8b4a6a2299dabb36df6f";
    
    public static void main(String args[]) {
        System.out.println("Testing Facebook");
        LegacyFacebookClient facebookClient = new DefaultLegacyFacebookClient(KEY, SECRET);
        Long uid = facebookClient.execute("users.getLoggedInUser", Long.class);
        String query =
          "SELECT name, pic_big, affiliations FROM user " +
          "WHERE uid IN (SELECT uid2 FROM friend WHERE uid1=12345)";
        List<User> users =
          facebookClient.executeForList("fql.query", User.class,
            Parameter.with("query", query), Parameter.with("return_ssl_resources", "true"));
        
        for (User user : users)
            System.out.println(user + "\n");
        }
}

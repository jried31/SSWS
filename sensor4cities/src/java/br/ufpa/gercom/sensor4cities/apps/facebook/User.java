/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.apps.facebook;

import com.restfb.Facebook;
import java.util.List;

/**
 *
 * @author leokassio
 */
public class User {
    
    @Facebook
    private String name;
    
    @Facebook("pic_big")
    String pictureUrl;
    
    @Facebook
    List<Affiliation> affiliations;
    
    public String toString() {
        return String.format("Name: %s\nProfile Image URL: %s\nAffiliations: %s",
          name, pictureUrl, affiliations);
    }
}



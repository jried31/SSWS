/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.ufpa.gercom.sensor4cities.apps.facebook;

import com.restfb.Facebook;

/**
 *
 * @author leokassio
 */
public class Affiliation {
    
  @Facebook
  String name;

  @Facebook
  String type;

  public String toString() {
    return String.format("%s (%s)", name, type);
  }
    
}

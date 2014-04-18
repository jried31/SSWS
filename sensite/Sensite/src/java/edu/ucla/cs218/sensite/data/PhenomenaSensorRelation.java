/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.sensite.data;

/**
 *
 * @author jried31
 */
public class PhenomenaSensorRelation {
    private String phenomena=null,sensor=null;
    public PhenomenaSensorRelation(){}
    public PhenomenaSensorRelation(String phenomena,String sensor){this.phenomena=phenomena;this.sensor=sensor;}
    public String getPhenomenena(){return this.phenomena;}
    public String getSensor(){return this.sensor;}
    public void setPhenomena(String p){
        this.phenomena=p;
    }
    public void setSensor(String s){
        this.sensor=s;
    }
}


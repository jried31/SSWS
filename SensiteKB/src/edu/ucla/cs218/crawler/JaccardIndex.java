/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author jried31
 */
public class JaccardIndex {
	public static double jaccardSimilarity(HashMap<String,Integer> referenceShingle, HashMap<String,Integer> sentenceShingle){

            Set<String> refSet = referenceShingle.keySet();
            //Set<String> refSet = sentenceShingle.keySet();
            
            
                    /*
		int sizeh1 = h1.size();
		//Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		//h1 now contains the intersection of h1 and h2
		System.out.println("Intersection "+ h1);
		
			
		h2.removeAll(h1);
		//h2 now contains unique elements
		System.out.println("Unique in h2 "+ h2);
		
		//Union 
		int union = sizeh1 + h2.size();
		int intersection = h1.size();
		
		return (double)intersection/union;
            */
            return 0;
	}
	public static void main(String args[]){
		//System.out.println(jaccardSimilarity("153 West Squire Dr","147 West Squire Dr"));
		
	}
}

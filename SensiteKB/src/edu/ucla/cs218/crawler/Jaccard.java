/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import edu.ucla.cs218.sensite.MongoConnector;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Rishi
 */
public class Jaccard {
    
	public static double[] jaccardSimilarity(String similar2, int k, int stopwords){
		    
                //db.collection
                DB ret = MongoConnector.getDatabase();
             
                BasicDBObject row = new BasicDBObject();
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("k", k));
                obj.add(new BasicDBObject("stopwords", stopwords));
                row.put("$and", obj);
                
                DBCursor cursor = ret.getCollection("shingleClean").find(row);
                System.out.println(cursor.size());
                double[] output = new double[cursor.size()];
                int count = 0;
	while (cursor.hasNext()) {
            
		DBObject dobj = cursor.next();
             BasicDBList dobj2 =    (BasicDBList) dobj.get("sets");
             HashSet<String> h1 = new HashSet<>();
		HashSet<String> h2 = new HashSet<>();
                //for(String s: similar2.split("@")){
		h2.add(similar2);		
		//}
		System.out.println("h2 "+ h2);
	
            
         //        System.out.println(dobj2.toString()) ;
                  BasicDBObject[] dobj2Arr = dobj2.toArray(new BasicDBObject[0]);
                  
                  for(BasicDBObject dbObj : dobj2Arr) {
    // shows each item from the lights array
   // System.out.println(dbObj.get("set"));
   String set = dbObj.get("set").toString();
   // for(String s: set.split("@")){
		h1.add(set);		
//  }
             
		}
                
                  int sizeh1 = h1.size();
		//Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		//h1 now contains the intersection of h1 and h2
		System.out.println("Intersection "+ h1 + sizeh1);
		
			
		h2.removeAll(h1);
		//h2 now contains unique elements
		System.out.println("Unique in h2 "+ h2);
		
		//Union 
		int union = sizeh1 + h2.size();
		int intersection = h1.size();
                System.out.println(union + "   " + h1.size());
                 double jac = (double)intersection/union;
	//	System.out.println(jac);
		output[count] = jac;
                  System.out.println(output[count]);
		
                count++;
                 
        }
        return output;
        	
		}
public static void main(String args[]){
		System.out.println(jaccardSimilarity("density@and@sound@",3,1));
		
	}
} 
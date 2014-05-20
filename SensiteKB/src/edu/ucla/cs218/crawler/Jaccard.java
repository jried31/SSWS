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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Rishi
 */
public class Jaccard {
    
	public static double[] jaccardSimilarity(String similar2, int k, int stopwords, String phenomenon) throws IOException{
		    
             File file = new File("Jaccard.txt");
 
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file,true);
                   //    BufferedWriter bw = new BufferedWriter(fw);
	
                //db.collection
                DB ret = MongoConnector.getDatabase();
             
                BasicDBObject row = new BasicDBObject();
                List<BasicDBObject> obj = new ArrayList<BasicDBObject>();
                obj.add(new BasicDBObject("k", k));
                obj.add(new BasicDBObject("stopwords", stopwords));
                obj.add(new BasicDBObject("phenomena", phenomenon));
                row.put("$and", obj);
                
                DBCursor cursor = ret.getCollection("ManualShingle").find(row);
                System.out.println(cursor.size());
                double[] output = new double[cursor.size()];
                int count = 0;
	while (cursor.hasNext()) {
            
		DBObject dobj = cursor.next();
             BasicDBList dobj2 =    (BasicDBList) dobj.get("sets");
             HashSet<String> h1 = new HashSet<>();
		HashSet<String> h2 = new HashSet<>();
                for(String s: similar2.split("@")){
		h2.add(s+"@");		
		}
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
                System.out.println("h1 "+ h1);
	
                  int sizeh1 = h1.size();
		//Retains all elements in h3 that are contained in h2 ie intersection
		h1.retainAll(h2);
		//h1 now contains the intersection of h1 and h2
		//System.out.println("Intersection "+ h1 );
		
			
		h2.removeAll(h1);
		//h2 now contains unique elements
	//	System.out.println("Unique in h2 "+ h2);
		
		//Union 
		int union = sizeh1 + h2.size();
		int intersection = h1.size();
                System.out.println("TOTAL UNION SIZE "+  union + " INTERSECTION  " + h1.size());
                 double jac = (double)intersection/union;
	//	System.out.println(jac);
		output[count] = jac;
                  System.out.println(output[count]);
		
                count++;
                 
        }
        
			for (int i=0;i<output.length;i++){
                        
                             String out = String.valueOf(output[i]);
                             
                            fw.write("Jaccard index for "+ similar2+ " "+ "shingle length " +k+ " stopwords as " +stopwords + " is " + out +"\n");
                        }
			fw.close();
 
		//	System.out.println("Done");
        return output;
        	
		}
public static void main(String args[]) throws IOException{
		jaccardSimilarity("a rain gauge is a type of instrument used by meteorologists and hydrologists to gather and measure the amount of liquid precipitation over a set period of time",1,1,"rain");
		
	}
} 
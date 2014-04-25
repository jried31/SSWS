/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import edu.ucla.cs218.sensite.MongoConnector;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author jried31
 */
public class Analyzer {
    public static void main(String[] args) throws IOException{
        DB db = MongoConnector.getDatabase();
        DBCollection phenomenaList = db.getCollection("phenomena");
        DBCursor cursor;
        
        HashMap <String, HashMap<String,Integer>> data = new HashMap<String, HashMap<String,Integer>>();
          File file = new File("/Users/jried31/relations.csv");

        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Phenomena,Sensor,Count\n");
        
        //Fill phenomenon list
        cursor = phenomenaList.find();
        try {
            while(cursor.hasNext()) {
                DBObject phenomenaObj = cursor.next();
                String phenomena = (String) phenomenaObj.get("phenomena");
                //System.out.println("The Phenomena: "+phenomena);
                HashMap<String,Integer>sensors=new HashMap<String,Integer>();
                
		// Setup the query
		BasicDBObject query = new BasicDBObject("relations.phenomena", phenomena);
		// Execute the query and get a handle to the returned result set
		DBCursor relations = db.getCollection("websites").find(query);
		
                int noObservations = 0;//Number of sensors
                while(relations.hasNext())
                {
                    DBObject o = relations.next();
                    BasicDBList relation = (BasicDBList) o.get("relations");
                    Iterator it = relation.iterator();
                    
                    while(it.hasNext()){
                       DBObject obj=(DBObject) it.next();
                       String sensor = (String) obj.get("sensor");
                       //System.out.println("The Sensor: "+sensor);
                       Integer i = sensors.get(sensor);
                       if(i==null) 
                           sensors.put(sensor,new Integer(1));
                       else
                           sensors.put(sensor,++i);
                       String output = phenomena+","+sensor+","+sensors.get(sensor)+"\n";
                       //bw.write(output);
                       //System.out.println(output);
                       noObservations++;
                    }
                }
                DB db3 = MongoConnector.getDatabase();
                // Setup the query
                BasicDBList countList = new BasicDBList();
                for(String sensor:sensors.keySet()){
                    countList.add(new BasicDBObject("sensor",sensor).append("count", sensors.get(sensor)));
                    String output = phenomena+","+sensor+","+sensors.get(sensor)+"\n";
                    //System.out.println(output);
                    bw.write(output);
                }
                
                BasicDBObject association = new BasicDBObject("phenomenon",phenomena)
                        .append("observations", noObservations)
                        .append("association", countList);
                
                db3.getCollection("associations").insert(association);
            }
        } finally {
            cursor.close();
            bw.close();
        }
        
        /*Check the Database to see if it's already been crawled
        DB db = MongoConnector.getDatabase();
        // Setup the query
        DBCursor result = db.getCollection("websites").find();
        
        File file = new File("/Users/jried31/relations.txt");

        // if file doesnt exists, then create it
        if (!file.exists()) {
                file.createNewFile();
        }

        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("Phenomena"+"\t"+"Sensor"+"\n");
        while(result.hasNext())
        {
            DBObject o = result.next();
            BasicDBList relations = (BasicDBList) o.get("relations");
            Iterator it = relations.iterator();
            while(it.hasNext()){
               DBObject obj=(DBObject) it.next();
               String phenomena = (String)obj.get("phenomena");
               String sensor = (String) obj.get("sensor");
               bw.write(phenomena+"\t"+sensor+"\n");
            }
        }
        
        bw.close();
                */
    }
}

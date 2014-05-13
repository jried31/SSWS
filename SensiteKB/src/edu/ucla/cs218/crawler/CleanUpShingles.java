package edu.ucla.cs218.crawler;

import java.util.HashMap;

import com.mongodb.*;

import edu.ucla.cs218.sensite.MongoConnector;
import java.util.Iterator;

public class CleanUpShingles {

    //These global variables store the lists of phenomenon and sensor names
    public static HashMap<String, Boolean> phenomenaNames = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> sensorNames = new HashMap<String, Boolean>();
    

    public static void main(String[] args) {

        DB db = MongoConnector.getDatabase();
        DBCollection phenomena = db.getCollection("phenomena");
        DBCollection sensors = db.getCollection("sensors");
        DBCursor cursor;

        //Fill phenomenon list
        cursor = phenomena.find();
        try {
            while (cursor.hasNext()) {
                DBObject phenomenon = cursor.next();
                String name = (String) phenomenon.get("phenomena");
                phenomenaNames.put(name, true);
            }
        } finally {
            cursor.close();
        }

        //Fill sensor list
        cursor = sensors.find();
        try {
            while (cursor.hasNext()) {
                DBObject sensor = cursor.next();
                String name = (String) sensor.get("sensor");
                sensorNames.put(name, true);
            }
        } finally {
            cursor.close();
        }

        //Grab all the URL's to search
        for (String phenomenon : phenomenaNames.keySet()) 
        {
            //System.out.println("Phenomena: "+ phenomenon);
            //Query all phenomena of size K
            for(int k = 2;k < 6;k++)
            {
                db = MongoConnector.getDatabase();

                // Setup the query
                BasicDBObject query = new BasicDBObject("phenomena", phenomenon)
                        .append("stopwords", 0)
                        .append("k", k);
                cursor = db.getCollection("shingle").find(query);
                
                HashMap<String,Integer> shingle = new HashMap<String,Integer>();
                while(cursor.hasNext())
                {
                    DBObject data = cursor.next();
                    BasicDBList associations = (BasicDBList) data.get("sets");
                    Iterator it = associations.iterator();
                    while(it.hasNext())
                    {
                        DBObject obj = (DBObject)it.next();
                        String set = (String) obj.get("set");
                        Integer count=(Integer) obj.get("count");
                        //System.out.println("The Shingle: "+set+ " Count: "+count);
                        
                        Integer cnt = shingle.get(set);
                        if(cnt == null){
                            shingle.put(set, count);
                        }else{
                            shingle.put(set, cnt + count);
                        }
                    }
                }
                
                query = new BasicDBObject("phenomena", phenomenon)
                    .append("stopwords", 1)
                    .append("k", k);
                        
                BasicDBList relationshipStop = new BasicDBList();
                for (String set : shingle.keySet())
                {
                    Integer cnt = shingle.get(set);
                    if(cnt > 4){
                        //System.out.println("The Shingle (Stopwords Incl): "+set+ " Count: "+cnt);
                        relationshipStop.add(new BasicDBObject("set",set).append("count",cnt));
                        //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                    }
                }
                if(relationshipStop.size() > 0){
                    query.append("sets",relationshipStop);
                    db.getCollection("shingleClean").insert(query);  
                }
                //System.out.println("stopwords 0");
                
                
                
                
                
                
                db = MongoConnector.getDatabase();
                query = new BasicDBObject("phenomena", phenomenon)
                        .append("stopwords", 1)
                        .append("k", k);
                cursor = db.getCollection("shingle").find(query);
                
               shingle = new HashMap<String,Integer>();
                while(cursor.hasNext())
                {
                    DBObject data = cursor.next();
                    BasicDBList associations = (BasicDBList) data.get("sets");
                    Iterator it = associations.iterator();
                    while(it.hasNext())
                    {
                        DBObject obj = (DBObject)it.next();
                        String set = (String) obj.get("set");
                        Integer count=(Integer) obj.get("count");
                        //System.out.println("The Shingle w/StopWords: "+set+ " Count: "+count);
                        
                        Integer cnt = shingle.get(set);
                        if(cnt == null){
                            shingle.put(set, count);
                        }else{
                            shingle.put(set, cnt + count);
                        }
                    }
                }
                
                
                db = MongoConnector.getDatabase();
                query = new BasicDBObject("phenomena", phenomenon)
                    .append("stopwords", 0)
                    .append("k", k);
                        
                BasicDBList relationshipWOStop = new BasicDBList();
                for (String set : shingle.keySet()){
                    Integer cnt = shingle.get(set);
                    if(cnt > 4){
                        relationshipWOStop.add(new BasicDBObject("set",set).append("count",cnt));
                        //System.out.println("The Shingle (No Stopwords): "+set+ " Count: "+cnt);
                    }
                }
                if(relationshipWOStop.size() > 0){
                    query.append("sets",relationshipWOStop);
                    db.getCollection("shingleClean").insert(query);  
                }
               // System.out.println("stopwords 1");
            }
        }
    }
}

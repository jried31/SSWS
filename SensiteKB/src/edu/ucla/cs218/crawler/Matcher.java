package edu.ucla.cs218.crawler;           
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.ucla.cs218.sensite.MongoConnector;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Matcher {
    private WebURL webpage = null;

    public Matcher(WebURL page) {
        this.webpage = page;
    }

    public void matchPhenomenonToSensor(String text,HashMap<String,HashSet<String>>relations) {
        ArrayList<String>sensors=new ArrayList<String>();
        ArrayList<String>phenomenas=new ArrayList<String>();
        
        for (String p : Controller.phenomenaNames.values()) {
            if (text.matches(".*\\b"+p+"\\b.*")){
                phenomenas.add(p);
                /*Integer s=phenomenons.get(phenomenon);
                if(s == null)
                    phenomenons.put(p,1);
                else phenomenons.put(p, s++);
                */
            }
        }
        
        for (String s : Controller.sensorNames.values()) {
            if (text.matches(".*\\b"+s+"\\b.*")) {
                sensors.add(s);
                /*Integer s = sensors.get(sensor);
                if(s == null)
                    sensors.put(sensor,1);
                else sensors.put(sensor,s++);*/
            }
        }
        
        for(String p:phenomenas){
            HashSet<String> sensorList = relations.get(p);
            for(String s:sensors){
                if(sensorList == null){
                    sensorList=new HashSet<String>();
                }
                sensorList.add(s);
            }
            relations.put(p, sensorList);
        }
    }

    //Takes a sentence as input, and returns a list of the phenomena and sensors
    //contained in the sentence.

    /**
     *
     * @param relations
     */
        public void saveStatisticsToDB(HashMap<String,HashSet<String>>relations)  {
        // Get Database Handle
        DB db = MongoConnector.getDatabase();
        DBCollection associations = db.getCollection("websites");

        // Add matches found to Associations table in database
        if (!relations.isEmpty()) {
            // Convert the sensorsList to a MongoDB recognizable array
            
            BasicDBList phenomList = new BasicDBList(),
                    sensorList=new BasicDBList(),
                    relationsList=new BasicDBList();
            
            for(String k:relations.keySet()){
                HashSet<String>sensors=relations.get(k);
                if(sensors != null){
                    for(String s:sensors){
                        relationsList.add(new BasicDBObject("phenomena",k).append("sensor", s));//new BasicDBObject("phenomena",k),new BasicDBObject("sensor",s)));
                    }
                }
            }
            BasicDBObject data = new BasicDBObject("webpage", this.webpage.getURL());
            if(relationsList.size() > 0){
                data.append("relations",relationsList);
                associations.insert(data);
            }
        }
    }
    
            //Takes a sentence as input, and returns a list of the phenomena and sensors
    //contained in the sentence.
    String splitOn = "[ ]+";// Split on spaces
    public void saveStatisticsToDB()  {
        /*
        // Get Database Handle
        DB db = MongoConnector.getDatabase();
        DBCollection associations = db.getCollection("websites");

        // Add matches found to Associations table in database
        if (!this.phenomenons.isEmpty() && !this.sensors.isEmpty()) {
            // Convert the sensorsList to a MongoDB recognizable array
            BasicDBList phenomList = new BasicDBList(),
                    sensorList=new BasicDBList();
            phenomList.addAll(this.phenomenons.keySet());
            sensorList.addAll(this.sensors.keySet());
            
            BasicDBObject data = new BasicDBObject("webpage", this.webpage.getURL())
                    .append("phenomenons", phenomList)
                    .append(("sensors"), sensorList);
            
            associations.insert(data);
        }*/
    }  
}

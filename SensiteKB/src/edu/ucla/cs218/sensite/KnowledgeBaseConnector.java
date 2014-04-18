package edu.ucla.cs218.sensite;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.HashMap;

public class KnowledgeBaseConnector {
	ResultSet resultSet		= null;
	ResultSet resultSetTwo	= null;
	/**
	 * 
	 * @param phenomena 
	 * @return a list of sensor types related to the phenomena
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public HashMap<String, Integer> getSensors(String phenomena) throws SQLException, FileNotFoundException, IOException{
		HashMap<String, Integer> sensors 	= getSensorsList(phenomena);
		//SynonymMap map 				= new SynonymMap(new FileInputStream("\\Users\\Alvin\\Documents\\GitHub\\SS\\sensite\\lib\\wn_s.pl"));
		//String[] synonyms 			= map.getSynonyms(phenomena);
		//System.out.println(phenomena + ":" + java.util.Arrays.asList(synonyms).toString());
		
                /*
		if (sensors.isEmpty())
		{
			System.out.println("No match in knowledgebase.");
			System.out.println("Checking synset for "+phenomena+"...");
			
			for (String synonym : synonyms)
			{
				HashMap<String, Double> synonymSensors = getSensorsList(synonym);
				if (synonymSensors.isEmpty())
				{
					System.out.println("No match in knowledgebase for synonym: "+synonym);
				}else{
					System.out.println("Match in knowledgebase for synonym: "+synonym);
					sensors = synonymSensors;
					break;
				}
			}
		}
                */
		return sensors;
	}
	
	private HashMap<String, Integer> getSensorsList(String phenomena) throws SQLException
	{
		// Get Database Handle
		DB db = MongoConnector.getDatabase();
		
                HashMap<String, Integer> outArray = new HashMap<String, Integer>();
                
                
		DBCursor result = db.getCollection("associations").find(new BasicDBObject("phenomenon", phenomena));
		
                while(result.hasNext())
                {
                    DBObject o = result.next();
                    String sensor = (String) o.get("sensor");
                    int frequency = (Integer) o.get("frequency");
                    outArray.put(sensor, frequency);
                }
                
                /*
		if (result == null)
			return null;
		
		BasicDBList sensors = (BasicDBList)result.get("sensors");
		HashMap<String, Double> outArray = new HashMap<String, Double>();
		
		for (Object sensor : sensors) {
                    outArray.put(
                        ((BasicDBObject)sensor).getString("sensorName"),
                        ((BasicDBObject)sensor).getDouble("count") / ((BasicDBObject)result).getDouble("count") * 100
                    );
                }
                */
		return outArray;
		
		/*
		for (int i = 0; i < sensors.size(); i++) {
			System.out.println("Found sensor: "+ ((BasicDBObject)sensors.get(i)).getString("SensorName") + " with ratio "+ratioString+"%")
		}
		*/
	}
}
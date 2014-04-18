package edu.ucla.cs218.sensite;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.MetaData.BaseDataType;
import com.MetaData.BaseMetric;
import com.MetaData.InformationType;
import com.MetaData.MetaData;
import com.MetaData.SensorType;
import com.MetaData.locationType;
import com.MetaData.xs.anyType;
import com.MetaData.xs.dateTimeType;
import com.MetaData.xs.doubleType;
import com.altova.types.DateTime;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
public class ObservationBaseConnector {
	

	
	// fetchData takes in a 2D location, a datetime and an array of sensorTypes and returns
	// a JSONstring of Information objects from the ObservationBase that match the query
	public static JSONObject fetchData(double latitude, double longitude, DateTime date, String[] sensorTypes) throws JSONException, Exception {
		// Get Database Handle
		DB db = MongoConnector.getDatabase();
		
		// Set the startDate and EndDate from the given date. (+- 10 second margin)
		Calendar startCalendar = GregorianCalendar.getInstance(), endCalendar = GregorianCalendar.getInstance();
		startCalendar.setTime(date.getDate());
		startCalendar.add(Calendar.SECOND, -10);
		endCalendar.setTime(date.getDate());
		endCalendar.add(Calendar.SECOND, 10);
		
		// Convert the sensorsList to a MongoDB recognizable array
		BasicDBList sensorsList = new BasicDBList();
		sensorsList.addAll(Arrays.asList(sensorTypes));
		
		// Setup the query
		BasicDBObject query = new BasicDBObject()
			.append("Information.BaseData.location", (new BasicDBObject()).append("$nearSphere", (new BasicDBObject().append("lat", latitude).append("lon", longitude))).append("$maxDistance", 0.1))
			.append("Information.BaseData.dateTime", (new BasicDBObject()).append("$gt", startCalendar.getTime()).append("$lt", endCalendar.getTime()))
			.append("Information.BaseQoI.DataSource.Sensor.classification.sensorType", new BasicDBObject("$in", sensorsList));
		
		// Log the Query
		System.out.println(query);
		
		// Execute the query and get a handle to the returned result set
		DBCursor cursor = db.getCollection("data").find(query);
		
		// Setup a JSON Array that will contain the result set as JSON objects
		JSONArray outArray = new JSONArray();
		for (DBObject dbObject : cursor) {
			// Remove the '_id' key and parse the timestamp manually
			BasicDBObject baseData = (BasicDBObject)((BasicDBObject)dbObject.get("Information")).get("BaseData");
			dbObject.removeField("_id");
			baseData.put("dateTime", ((new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")).format(baseData.get("dateTime"))));
			
			// Add this object to the output array
			outArray.put(new JSONObject(JSON.serialize(dbObject)).get("Information"));
		}
		
		// Serialize the JSONArray and return it as a string
		return new JSONObject().put("Informations", outArray);
	}
	
	// A sample method to insert/fetch objects to/from the observationBase.
	/*
	public static void main(String[] args) {
		try {
			db = new MongoClient(new MongoClientURI("mongodb://sensite:sensite@ds053188.mongolab.com:53188/sensite")).getDB("sensite");
			//insertObject(32.23, 23, -34, new DateTime(2013, 5, 23, 13, 32, 45, 0, 0), "Geiger Counter");
			System.out.println(fetchData(22d, -35d, new DateTime(2013, 5, 23, 13, 32, 45, 0, -8), new String[]{"Geiger Counter", "Barometer"}));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	*/

}

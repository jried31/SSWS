/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.sensite;

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
import edu.ucla.cs218.util.Globals;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

/**
 * REST Web Service
 *
 * @author jried31
 */
@Path("service")
public class Services {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Services
     */
    public Services() {
    }
    

    @POST
    @Path("/upload")
    public void upload(double value, double latitude, double longitude, DateTime timestamp, String sensorType) {
        // Get Database Handle
        DB db = MongoConnector.getDatabase();
        try {
            // Create an XML Document
            MetaData doc = MetaData.createDocument();

            // Insert a new Information Object and get a reference to it
            InformationType information = doc.Information.append();
            {
                // Insert a new BaseData Object
                BaseDataType baseData = information.BaseData.append();
                {
                    // Insert a BaseMetric Object
                    BaseMetric metric = baseData.metric.append();
                    {
                        // Set the value of the metric
                        doubleType quantMetricDoubleType = metric.QuantitativeMetric.append();
                        quantMetricDoubleType.setValue(value);
                    }

                    // Insert a Location Object
                    locationType location = baseData.location.append();
                    {
                        // Set  the longitude
                        doubleType lon = location.lon.append();
                        lon.setValue(longitude);

                        // Set the latitude
                        doubleType lat = location.lat.append();
                        lat.setValue(latitude);
                    }

                    // Set the timestamp
                    dateTimeType dateTime = baseData.dateTime.append();
                    dateTime.setValue(timestamp);
                }

                // Insert a Sensor Object and get a reference to it
                SensorType sensor = information.BaseQoI.append().DataSource.append().Sensor.append();
                {
                    // Set the Sensor type
                    anyType sType = sensor.classification.append().sensorType2.append();
                    sType.setValue(sensorType);
                }
            }
            
            // Parse and set the timestamp to the correct format manually
            BasicDBObject obj = (BasicDBObject)com.mongodb.util.JSON.parse(XML.toJSONObject(doc.saveToString(false)).toString());
            BasicDBObject baseData = (BasicDBObject)((BasicDBObject)obj.get("Information")).get("BaseData");
            baseData.put("dateTime", DateTime.parse(baseData.getString("dateTime")).getDate());

            // Insert the object to the database and log the result
            System.out.println(db.getCollection("Data").insert(obj));
        } catch (Exception ex) {
            Logger.getLogger(Services.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
 
    @GET
    @Path("/fetch")
    public JSONObject fetch(String phenomena, double latitude, double longitude, DateTime timestamp) {
        //Get the Association
        ArrayList<String> releventSensors = new ArrayList<String>();
        DB db = MongoConnector.getDatabase();
        BasicDBObject query = new BasicDBObject("phenomenon", phenomena);
        // Execute the query and get a handle to the returned result set
        DBObject tuple = db.getCollection("associations").findOne(query);

        int sampleCount = Integer.parseInt((String)tuple.get("observations"));
        BasicDBList associations = (BasicDBList) tuple.get("association");
        Iterator it = associations.iterator();
        
        while(it.hasNext())
        {
            DBObject obj = (DBObject)it.next();
            String sensor = (String) obj.get("sensor");
            int count = Integer.parseInt((String) obj.get("count"));
            System.out.println("The Sensor: "+sensor+ " Count: "+count);
            
            if((count/sampleCount)*100 >= Globals.RELEVANCY_THRESHOLD)
                releventSensors.add(sensor);
        }
        
        //Return relevant Sensor Data
        
        //Compute the timeframe
        // Set the startDate and EndDate from the given date. (+- 10 second margin)
        Calendar startCalendar = GregorianCalendar.getInstance(), endCalendar = GregorianCalendar.getInstance();
        startCalendar.setTime(timestamp.getDate());
        startCalendar.add(Calendar.SECOND, -Globals.TIMEFRAME_BUFFER);
        endCalendar.setTime(timestamp.getDate());
        endCalendar.add(Calendar.SECOND, Globals.TIMEFRAME_BUFFER);
		
        // Convert the sensorsList to a MongoDB recognizable array
        BasicDBList sensorsList = new BasicDBList();
        sensorsList.addAll(releventSensors);

        // Setup the query
        BasicDBObject dataQuery = new BasicDBObject()
                .append("Information.BaseData.location", (new BasicDBObject()).append("$nearSphere", (new BasicDBObject().append("lat", latitude).append("lon", longitude))).append("$maxDistance", 0.1))
                .append("Information.BaseData.dateTime", (new BasicDBObject()).append("$gt", startCalendar.getTime()).append("$lt", endCalendar.getTime()))
                .append("Information.BaseQoI.DataSource.Sensor.classification.sensorType", new BasicDBObject("$in", sensorsList));

        // Log the Query
        System.out.println(dataQuery);

        // Execute the query and get a handle to the returned result set
        DBCursor cursor = db.getCollection("data").find(dataQuery);

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
    
    
    @GET
    @Path("/fetch2")
    @Produces("application/json")
    public Response fetch2(@QueryParam("phenomena") String phenomena, @QueryParam("latitude") double latitude, @QueryParam("longitude") double longitude, @QueryParam("time") DateTime timestamp) {

        System.out.println("Query received with phenomena: " + phenomena + " and location: longitude:" + longitude + " , latitude:" + latitude + " around time: " + timestamp);
        Response response;
        //Get the Association
        ArrayList<String> releventSensors = new ArrayList<String>();
        DB db = MongoConnector.getDatabase();
        BasicDBObject query = new BasicDBObject("phenomenon", phenomena);
        // Execute the query and get a handle to the returned result set
        DBObject tuple = db.getCollection("associations").findOne(query);

        int sampleCount = Integer.parseInt((String) tuple.get("observations"));
        BasicDBList associations = (BasicDBList) tuple.get("association");
        Iterator it = associations.iterator();

        while (it.hasNext()) {
            DBObject obj = (DBObject) it.next();
            String sensor = (String) obj.get("sensor");
            int count = Integer.parseInt((String) obj.get("count"));
            System.out.println("The Sensor: " + sensor + " Count: " + count);

            if ((count / sampleCount) * 100 >= Globals.RELEVANCY_THRESHOLD) {
                releventSensors.add(sensor);
            }
        }

        //Return relevant Sensor Data
        //Compute the timeframe
        // Set the startDate and EndDate from the given date. (+- 10 second margin)
        Calendar startCalendar = GregorianCalendar.getInstance(), endCalendar = GregorianCalendar.getInstance();
        startCalendar.setTime(timestamp.getDate());
        startCalendar.add(Calendar.SECOND, -Globals.TIMEFRAME_BUFFER);
        endCalendar.setTime(timestamp.getDate());
        endCalendar.add(Calendar.SECOND, Globals.TIMEFRAME_BUFFER);

        // Convert the sensorsList to a MongoDB recognizable array
        BasicDBList sensorsList = new BasicDBList();
        sensorsList.addAll(releventSensors);

        // Setup the query
        BasicDBObject dataQuery = new BasicDBObject()
                .append("Information.BaseData.location", (new BasicDBObject()).append("$nearSphere", (new BasicDBObject().append("lat", latitude).append("lon", longitude))).append("$maxDistance", 0.1))
                .append("Information.BaseData.dateTime", (new BasicDBObject()).append("$gt", startCalendar.getTime()).append("$lt", endCalendar.getTime()))
                .append("Information.BaseQoI.DataSource.Sensor.classification.sensorType", new BasicDBObject("$in", sensorsList));

        // Log the Query
        System.out.println(dataQuery);

        // Execute the query and get a handle to the returned result set
        DBCursor cursor = db.getCollection("data").find(dataQuery);

        // Setup a JSON Array that will contain the result set as JSON objects
        JSONArray outArray = new JSONArray();
        for (DBObject dbObject : cursor) {
            // Remove the '_id' key and parse the timestamp manually
            BasicDBObject baseData = (BasicDBObject) ((BasicDBObject) dbObject.get("Information")).get("BaseData");
            dbObject.removeField("_id");
            baseData.put("dateTime", ((new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'")).format(baseData.get("dateTime"))));

            // Add this object to the output array
            outArray.put(new JSONObject(JSON.serialize(dbObject)).get("Information"));
        }

        // Serialize the JSONArray and return it as a string
        response = Response.ok(new JSONObject().put("Informations", outArray), MediaType.APPLICATION_JSON).build();

        return response;
    }

    /**
     * Retrieves representation of an instance of edu.ucla.cs218.sensite.Services
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of Services
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
    
}

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
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
    
 
    @POST
    @Path("/fetch")
    public void fetch(String phenomena, double latitude, double longitude, DateTime timestamp) {
        //Get the Association
        DB db = MongoConnector.getDatabase();
        BasicDBObject query = new BasicDBObject("phenomenon", phenomena);
        // Execute the query and get a handle to the returned result set
        DBObject tuple = db.getCollection("associations").findOne(query);

        String sampleCount = (String) tuple.get("observations");
        BasicDBList associations = (BasicDBList) tuple.get("association");
        Iterator it = associations.iterator();
        
        while(it.hasNext())
        {
            DBObject obj = (DBObject)it.next();
            String sensor = (String) obj.get("sensor");
            String count = (String) obj.get("count");
            System.out.println("The Sensor: "+sensor+ " Count: "+count);
        }
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

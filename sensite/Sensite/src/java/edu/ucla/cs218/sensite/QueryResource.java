package edu.ucla.cs218.sensite;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

@Path("getObservations")
public class QueryResource {

	long tStart;
	long tEnd;
	
	@GET
        @Produces("application/json")
	public Response getSensors(
            @QueryParam("phenomena") String phenomenaReference,
            @QueryParam("longitude") double longitude,
            @QueryParam("latitude") double latitude,
            @QueryParam("time") String time1Reference)
	{
	/*	
            System.out.println("Query received with phenomena: "+phenomenaReference+" and location: longitude:"+ longitude +" , latitude:"+ latitude+" around time: "+time1Reference);
		KnowledgeBaseConnector knowledgeBase = new KnowledgeBaseConnector();
		SensorBaseConnector sensorBase = new SensorBaseConnector();
		
		Response response;
		SimpleDateFormat format;
		Date date;
		String entity;
		try {
			format                          = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			date 				= format.parse(time1Reference);
			long timestampLong 		= date.getTime();
			Timestamp timestamp		= new Timestamp(timestampLong);
			HashMap<String, Integer> sensorTypes = knowledgeBase.getSensors(phenomenaReference);
			JSONObject infoObjects = sensorBase.getSensorData(new ArrayList<String>(sensorTypes.keySet()), longitude, latitude, timestamp);
                        for (int i = 0; i < ((JSONArray)infoObjects.get("Informations")).length(); i++) {
                            JSONObject thisObj = ((JSONArray)infoObjects.get("Informations")).getJSONObject(i);
                            thisObj.put(
                                "relevancy",
                                sensorTypes.get((String)((JSONObject)((JSONObject)((JSONObject)((JSONObject)thisObj
                                    .get("BaseQoI"))
                                        .get("DataSource"))
                                            .get("Sensor"))
                                                .get("classification"))
                                                    .get("sensorType")));
                        }
                        entity = infoObjects.toString(4);
                        response = Response.ok(entity, MediaType.APPLICATION_JSON).build();
		} catch (ParseException e) {
			e.printStackTrace();
			entity = "Invalid time format.";
			response = Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
		} catch (SQLException e) {
			e.printStackTrace();
			entity = "SQL invalid.";
			response = Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
		}catch (Exception e) {
			e.printStackTrace();
			entity = e.getLocalizedMessage();
			response = Response.status(Response.Status.BAD_REQUEST).entity(entity).build();
		}
                
		return response;
                */
            return null;
	}
}

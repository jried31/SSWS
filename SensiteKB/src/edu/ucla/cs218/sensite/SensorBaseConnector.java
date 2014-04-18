package edu.ucla.cs218.sensite;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;

import com.altova.types.DateTime;
import org.json.JSONObject;

public class SensorBaseConnector {

	/**
	 * 
	 * @param sensorTypes - a list of sensor types
	 * @param longitude - longitude of the location of interest
	 * @param latitude - latitude of the location of interest
	 * @param time - the time of interest 
	 * @return SensorXML scheme for the query
	 * @throws Exception 
	 * @throws JSONException 
	 */
	public JSONObject getSensorData(ArrayList<String> sensorTypes,
			double longitude, double latitude, Timestamp time) throws JSONException, Exception {
		Date dateT = new Date(time.getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateT);
		DateTime date = new DateTime(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND), 0);
		String[] types = new String[sensorTypes.size()];
		for (int i=0; i<sensorTypes.size();i++)
		{
			String sensor = sensorTypes.get(i);
			types[i] = sensor;
		}
		
		return ObservationBaseConnector.fetchData(latitude, longitude, date, types);
	}
}

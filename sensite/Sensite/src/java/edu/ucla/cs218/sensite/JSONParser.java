package edu.ucla.cs218.sensite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JSONParser {
	
	public final static String PHENOMENON 	= "phenomenon";
	public final static String DESCRIPTION 	= "description";
	public final static String LOCATION 	= "location";
	public final static String TIME1 		= "time1";
	public final static String TIME2 		= "time2";
	
	/**
	 * parseInput parses a JSON input to a HashMap
	 * @param data
	 * @return HashMap version of the JSON input
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static HashMap<String, Object> parseInput(String data) throws JsonParseException, JsonMappingException, IOException {
		InputStream inputStream 		= new ByteArrayInputStream(data.getBytes("UTF-8"));
		
		JsonFactory factory 			= new JsonFactory();
		ObjectMapper mapper 			= new ObjectMapper(factory);
		TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {};
		HashMap<String, Object> input 	= mapper.readValue(inputStream, typeRef);
		return input;
	}
	
	/**
	 * parseResult parses the result from the database into JSON 
	 * @param result
	 * @return
	 * @throws IOException 
	 * @throws JsonGenerationException 
	 * @throws SQLException 
	 * @throws ServerException 
	 */
	public static String parseResult(Map result) throws JsonGenerationException, IOException {
		
		String key1 = DESCRIPTION;
		String key2 = LOCATION;
		String key3 = TIME1;
		String key4 = TIME2;
		String attribute1 		= (String) result.get(key1);
		String attribute2		= (String) result.get(key2);
		String attribute3 		= (String) result.get(key3);
		String attribute4		= (String) result.get(key4);
      	
		JsonFactory jfactory 	= new JsonFactory();
    	OutputStream out 		= new ByteArrayOutputStream();
    	JsonGenerator jGenerator = jfactory.createJsonGenerator(out,JsonEncoding.UTF8);
    	jGenerator.writeStartObject();
    	jGenerator.writeStringField(key1, attribute1);
    	jGenerator.writeStringField(key2, attribute2);
    	jGenerator.writeStringField(key3, attribute3);
    	jGenerator.writeStringField(key4, attribute4);
    	
    	/*
    	//If the result has a list inside one of the nodes, this creates a JSON list
    	// PSEUDOCODE!
		jGenerator.writeArrayFieldStart(listName);
		for (String key : list){
			String value = list.getValue(key);
			jGenerator.writeStartObject();
			jGenerator.writeStringField(key, value);
			jGenerator.writeEndObject();
		}
		jGenerator.writeEndArray();
		*/
    	jGenerator.writeEndObject();
    	jGenerator.close();
    	return out.toString();

	}

}

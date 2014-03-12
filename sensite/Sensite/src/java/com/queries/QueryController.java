/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Sixiang
 */
package com.queries;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import org.json.JSONObject;
import org.json.JSONException;

public class QueryController {
    public static String[] DoParsing(String query){
        String [] retVal = new String[4];
        String text = query + " "; // dumb workaround for crashing when trying to access tmptxt[0]
        text.toLowerCase();
        if(text.contains("#ss")){
            String regexmatcher = "(.*)[^\\s]+\\$[-0-9.]+,[-0-9]+\\$[^\\s]+(.*)";
            if(text.matches(regexmatcher)){ // may need to add 1 level of \
                //messy code because java sucks at regex...
                String [] tmptxt = text.split("[^\\s]+\\$[-0-9.]+,[-0-9]+\\$[^\\s]+"); // regex doesn't match correctly for date
                int tmplength = 0;
                
                
                if(tmptxt.length == 1){
                    tmplength = 0;
                } else{
                    tmplength = tmptxt[1].length();
                }
                
                String importantInfo;
                importantInfo = text.substring(tmptxt[0].length(), text.length()-tmplength); // should give substring of regex match
                String [] tmparray = importantInfo.split("\\$");              
                String [] latlong = tmparray[1].split("\\,");    
                int tmp1 = Integer.parseInt(latlong[0]);
                int tmp2 = Integer.parseInt(latlong[1]);
                if(tmp1 < -180 || tmp1 > 180)
                    latlong[0] = "0";
                if(tmp2 < -180 || tmp2 > 180)
                    latlong[1] = "0";
                retVal[0] = tmparray[0]; //phenomenon
                retVal[1] = latlong[0]; //latitude
                retVal[2] = latlong[1]; //longitude
                retVal[3] = tmparray[2]; //time
                return retVal;
            }
        }
        return null;        
    }
    
    private static String ReadAll(Reader rd) throws IOException{
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    

    /*
    ParseJson will parse information from a json_obj and format a result string
    to Facebook and Twitter handler
    TODO: Figure out what the return string should look like.
    */
    public static String ParseJson(JSONObject json_obj){
        String temp = json_obj.toString();
        return temp;
    }

    
    /*
    SendQuery will accept an array of query string which consists of
    phenomena, longitude, latitude and time. It will send those query to
    snesite knowledge base web service and return a json object
    */
    public static JSONObject SendQuery(String[] query) throws IOException, JSONException{
        JSONObject result_json = null;
        String url_string;
        url_string = "http://localhost:8084/sensite/getObservations?" + "phenomena=" + query[0] +
                "&longitude=" + query[1] + "&latitude=" + query[2] + "&time=" + query[3];
        URL url = new URL(url_string);
        InputStream is = url.openStream();
        try{        
           BufferedReader rd = new BufferedReader(new InputStreamReader(is));
           String jsonText = ReadAll(rd);
           result_json = new JSONObject(jsonText);
        }catch(IOException ioex){
            ioex.printStackTrace();
        }catch(JSONException jsonex){
            jsonex.printStackTrace();
        }finally{
            is.close();
        }
        return result_json; 
    }
}

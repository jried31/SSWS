/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package database.parse.tables;

import almonds.ParseObject;
import database.parse.util.DBGlobals;

/**
 *
 * @author jried31
 */
public class ParseSensor extends ParseObject{
    //Column names
    public static final String NAME = "sensorName";

    /**
     * Get the name object value
     * @return 
     */
    public String getName(){
        return getString(NAME);
    }
     
    /**
     * Set the name object variable
     * @param name - Name of the Phenomena
     */
    public void setName(String name){
         put(NAME,name);
    }
    
    public ParseSensor() {
        //Super class requires the table name
        super(DBGlobals.TABLE_SENSOR);
    }
}

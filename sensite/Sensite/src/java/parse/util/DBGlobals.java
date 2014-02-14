/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parse.util;

import parse.almonds.Parse;

/**
 *
 * @author jried31
 */
public class DBGlobals {
    static public String TABLE_PHENOMENA="Phenomena";
    static public String TABLE_SENSOR="Sensor";
    
    public static void InitializeParse(){

        //App Ids for Connecting to the Parse DB
        Parse.initialize("jEciFYpTp2b1XxHuIkmAs3yaP70INpkBDg9WdTl9", 	//Application ID
            "aPEXcVv80kHwfVJK1WEKWckePkWxYNEXBovIR6d5"); 	//Rest API Key
        
    }
}

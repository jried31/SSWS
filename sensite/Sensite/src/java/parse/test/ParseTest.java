package parse.test;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ashish
 */
import java.util.List;
import parse.almonds.FindCallback;
import parse.almonds.Parse;
import parse.almonds.ParseException;
import parse.almonds.ParseObject;
import parse.almonds.ParseQuery;
//import almonds.ParseObject;
//import almonds.ParseQuery;
public class ParseTest {
        public static void main(String[] args) {
        //Ashish's Parse DB test
        //Parse.initialize("0lqMra0i5HcBs6ZnjUMDF4Y4nlP1cqP1vsDF35v9", "Zdc46am7Ew31KqwAIoj5mIxM5U5V6mYMVb1HBeMf");
        //Sensite Parse DB
        Parse.initialize("jEciFYpTp2b1XxHuIkmAs3yaP70INpkBDg9WdTl9", "aPEXcVv80kHwfVJK1WEKWckePkWxYNEXBovIR6d5");
        
        /*
        ParseObject gameScore = new ParseObject("GameScore");
        gameScore.put("score", 8000);
        gameScore.put("playerName", "Ash Kp3");
        gameScore.put("cheatMode", false);
        gameScore.saveInBackground();
       
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
        */
       
        
        //ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        ParseQuery query = new ParseQuery("Phenomena");
        query.findInBackground(new FindCallback() {
            public void done(List<ParseObject> phenomenaList, ParseException e) {
                if (e == null) {
                    System.out.println(phenomenaList);
                } else {
                    System.out.println("Error");
                }
            }
        });
        /*
        //Upload Sensor Data
        ParseObject records = new ParseObject("Records");
        records.put("qol", 6);
        records.put("longt", 500);
        records.put("latitude", 15000);
        records.saveInBackground();
        int qol = 5;
        double longitude = 50;
        double latitude = 70;
        String phenomena = "earthquake";
        ParseObject record = new ParseObject("Record");
        record.put("qol/vol", 5);
        record.put("longitude", 50);
        record.put("latitude", 70);
        record.put("phenomena", "earthquake");
        record.saveInBackground();
        */
    }
        //public void main () {
        //    addTest();
   // }

}

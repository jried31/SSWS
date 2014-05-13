package edu.ucla.cs218.crawler;

import edu.ucla.cs218.sensite.data.PhenomenaSensorRelation;
import edu.ucla.cs218.util.Globals;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import wordnet.wordnet;

/**
 *
 * @author Alvin
 */

/* 
*  This class takes a sentence as input and produces a list of possible
*  phenomenon or sensor matches as output.
*/ 
public class MatchFinder {
    
    //Initialize wordnet
    private wordnet wordNet = new wordnet();
        
    //Takes a sentence as input, and returns a list of the phenomena and sensors
    //contained in the sentence.
    public List<PhenomenaSensorRelation> FindMatches(String text) {
        List<PhenomenaSensorRelation> matches = new ArrayList<PhenomenaSensorRelation>();
        PhenomenaSensorRelation relation = new PhenomenaSensorRelation();
        for(String phenomenon : Controller.phenomenaNames.keySet())
        {
            if(text.contains(phenomenon))
                relation.setPhenomena(phenomenon);
        }
        for(String sensor : Controller.sensorNames.keySet())
        {
            if(text.contains(sensor))
                relation.setSensor(sensor);
        }
        return matches;
    }
    
    
    // This version of FindMatches functions the same as the one above.
    // The difference is the way it finds matches.  This is the scalable version.
    // This is not currently used due to google IP blocking the application because
    // it makes too many queries.
    
    
    public List<String> FindMatchesWGoogle(String text) throws IOException {
        String splitOn = "[ ]+";                // Split on spaces
        String[] words = text.split(splitOn);   // Stores all tokenized words
        String currentWord;                     // Current word being parsed
        List<String> possibleMatches = new ArrayList<String>();        // Stores matches found by autocomplete
        List<String> matches = new ArrayList<String>();                // Stores found matches
    
        for (String word : words) {
            currentWord = word.toLowerCase();
            
            //Check if current word is an adjective or a noun, otherwise skip
            try {
            if(!(wordNet.isNoun(currentWord) || wordNet.isAdjective(currentWord)))
            {
                System.out.println("NOT A NOUN/ADJECTIVE: " + currentWord);
                continue;
            }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            
            //Check if sentence contains matches based on the current word.
            if(getMatches(currentWord, possibleMatches)) {
                for(String pMatch : possibleMatches) {
                    System.out.println(pMatch);
                    if(text.contains(pMatch))
                    {
                        System.out.println(pMatch);
                        matches.add(pMatch);
                    }
                }
            }
        }
        return matches;
    }
    
    
    public boolean getMatches(String word, List<String> possibleMatches ) {
        possibleMatches.clear();
       //Grab the Autocomplete Bag of words from Google Suggest
            JSONArray data = null;
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
        
            
       // GOOGLE AUTOCOMPLETE CODE:
       // Necessary for scaling if phenomenon and sensor lists
       // become too extensive.  Checks if word or top 10 google
       // autocomplete results are phenomena or sensors.
       
        try {
            //Thread.sleep(1000); // DELAY SO GOOGLE DOES NOT BLOCK?
            String str = Globals.URL_GOOGLE_SEARCH.replace("WORD", word);
            URL url = new URL(str);
            
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty(
                  "User-Agent",
                  "Mozilla/5.0 (X11; U; Linux x86_64; en-GB; rv:1.8.1.6) Gecko/20070723 Iceweasel/2.0.0.6 (Debian-2.0.0.6-0etch1)");
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while(( line = br.readLine()) != null){
                sb.append(line);
            }

            br.close();
            iStream.close();
            urlConnection.disconnect();
            
            //Convert the String array to a JSON array stores first 10 results
            data =  new JSONArray(sb.toString());
            
            //Get the bag of words: NOTE - Index 0 = search term | Index 1 = bag of words
            //We want the bag of words
            data = data.getJSONArray(1);
            //System.out.println(data);
            
            /*First, validate whether the search term matches...If no match then run though the bag of words
            Basically my point is query for the Search term 1st to see if it matches a Sensor or Phenomena
            If not then search the bag of words ... Reason why is
            ["jim",["jimmy johns","jimmy kimmel","jimmy fal
              ^                 ^
              |                 |
            Search Term    Bag of Words
            NOTICE the Bad of Words does not have "jim" in it.
            
            if(word == PHENOMENA | SENSOR) {Do somethign here}
            else {
            */
            
            //Check if word itself is a sensor or phenomenon
            if(Controller.isSensor(word) || Controller.isPhenomenon(word))
                possibleMatches.add(word);
            
            //Check if autocomplete results are sensors or phenomena
            for (int k = 0; k < data.length(); k++) {
                String term = data.getString(k);
                //System.out.println(term);
                if(Controller.isSensor(term) || Controller.isPhenomenon(term))
                    possibleMatches.add(term);
            }
            
            if(possibleMatches.isEmpty())
                return false;
            
            return true;
            /* } */
        } catch (Exception ex) {
            // handle exception here
            ex.printStackTrace();
        }
        
        return false;
    }

}



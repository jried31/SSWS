package edu.ucla.cs218.crawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import com.google.gson.Gson;
import com.mongodb.*;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import edu.ucla.cs218.crawler.GoogleResults.Result;
import edu.ucla.cs218.sensite.MongoConnector;
import edu.ucla.cs218.util.Globals;
import java.util.Iterator;

public class Controller {

    //These global variables store the lists of phenomenon and sensor names
    public static HashMap<String, String> phenomenaNames = new HashMap<String, String>();
    public static HashMap<String, String> sensorNames = new HashMap<String, String>();
    
    //Checks if a given string is in the sensor list
    public static boolean isSensor(String s)
    {
        if(sensorNames.get(s) != null)
            return true;
        return false;
    }
    
    //Checks if a given string is in the phenomenon list
    public static boolean isPhenomenon(String p)
    {
        if(phenomenaNames.get(p) != null)
            return true;
        return false;
    }
    
    //Used to get google results
    private static GoogleResults getGoogleResults(String phenomenon,int page)
    {
        //NOTE: &start=10 --- there's a start paramater that controls paging of search results
    	String googleURL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start="+page+"&q=";
        String search = phenomenon;
        String charset = "UTF-8";

        URL url;
        Reader reader;
        GoogleResults results;
        try {
            url = new URL(googleURL + URLEncoder.encode(search, charset));
            reader = new InputStreamReader(url.openStream(), charset);
            results = new Gson().fromJson(reader, GoogleResults.class);
        return results;
        } catch (MalformedURLException e) {
                e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
        } catch (IOException e) {
                e.printStackTrace();
        }
        return null;
    }
	
	private static void setCrawlerConfig(List<String> urlsToSearch) throws Exception
	{
            String crawlStorageFolder 	= "data/crawl/root";
            int numberOfCrawlers 		= Globals.MAX_NUMBER_CRAWLERS;//Crawling 10 pages at a time cause thats how many search results return
            int maxDepthOfCrawling 		= Globals.WEBPAGE_LINK_DEPTH;//Crawl only the top webpage
        
            CrawlConfig config = new CrawlConfig();
            config.setCrawlStorageFolder(crawlStorageFolder);
            config.setMaxDepthOfCrawling(maxDepthOfCrawling);
            /*
             * Instantiate the controller for this crawl.
             */
            PageFetcher pageFetcher = new PageFetcher(config);
            RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
            RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
            CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

            /*
             * For each crawl, you need to add some seed urls. These are the first
             * URLs that are fetched and then the crawler starts following links
             * which are found in these pages
             */
            for (String urlToSearch : urlsToSearch)
            {
                if (!urlToSearch.toLowerCase().contains("en.wikipedia.org".toLowerCase())){
                        controller.addSeed(urlToSearch);
                }else{
                        //crawler doesn't work on wikipedia
                }
            }
            /*
             * Start the crawl. This is a blocking operation, meaning that your code
             * will reach the line after this only when crawling is finished.
             */
            controller.start(Crawler.class, numberOfCrawlers); 
	}
	
    static String []searchTerms = {"\"?\" measurement instrument", "how to measure \"?\""};
    public static void main(String [ ] args) throws Exception {
    	
        DB db = MongoConnector.getDatabase();
        DBCollection phenomena = db.getCollection("phenomena");
        DBCollection sensors = db.getCollection("sensors");
        DBCursor cursor;
        
        //Fill phenomenon list
        cursor = phenomena.find();
        try {
            while(cursor.hasNext()) {
                DBObject phenomenon = cursor.next();
                String name = (String) phenomenon.get("phenomena");
                phenomenaNames.put(name, name);
            }
        } finally {
            cursor.close();
        }
        
        //Fill sensor list
        cursor = sensors.find();
        try {
            while(cursor.hasNext()) {
                DBObject sensor = cursor.next();
                String name = (String) sensor.get("sensor");
                sensorNames.put(name, name);
            }
        } finally {
            cursor.close();
        }
        
        //Grab all the URL's to search
    	for (String phenomenon : phenomenaNames.values()){
        /*ArrayList<String> ph = new ArrayList<String>();
        ph.add("rain");
        ph.add("temperature");
        ph.add("fire");
        ph.add("explosion");
        ph.add("bearing");
        
        for ( Iterator<String> it = ph.iterator();it.hasNext();) {
            String phenomenon = it.next();*/
            System.out.println("Phenomena: " + phenomenon);
                
            //Query for each search term
            for(String searchterm : searchTerms ){
                System.out.println("Search Term: "+searchterm.replace("?", phenomenon));
                for(int i = 1;i <= Globals.SEARCH_PAGE_DEPTH;i++){
                    GoogleResults results = getGoogleResults(searchterm.replace("?", phenomenon),i);
                    List<Result> resultList = results.getResponseData().getResults();
                
                    List<String> urlsToSearch = new ArrayList<String>();
                    for (Result result : resultList)
                    {
                        String urlToSearch = result.getUrl();
                        System.out.println("Searching URL: "+urlToSearch);
                        urlsToSearch.add(urlToSearch);
                    }
                    //Start the crawlers
                    setCrawlerConfig(urlsToSearch);
                }
            }
    	}
    }
}

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
import edu.ucla.cs218.crawler.GoogleResults.ResponseData;
import edu.ucla.cs218.crawler.GoogleResults.Result;
import edu.ucla.cs218.sensite.MongoConnector;
import edu.ucla.cs218.util.Globals;
import java.util.Iterator;

public class Controller {

    //These global variables store the lists of phenomenon and sensor names
    public static HashMap<String, Boolean> phenomenaNames = new HashMap<String, Boolean>();
    public static HashMap<String, Boolean> sensorNames = new HashMap<String, Boolean>();
    private static boolean BUILD_SHINGLE = true,
            BUILD_ASSOCIATION = false;

    //Checks if a given string is in the sensor list
    public static boolean isSensor(String s) {
        return sensorNames.get(s) != null;
    }

    //Checks if a given string is in the phenomenon list
    public static boolean isPhenomenon(String p) {
        return phenomenaNames.get(p) != null;
    }

    //Used to get google results
    private static GoogleResults getGoogleResults(String phenomenon, int page) {
        //NOTE: &start=10 --- there's a start paramater that controls paging of search results
        String googleURL = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&start=" + page + "&q=";
        String search = phenomenon;
        String charset = "UTF-8";

        URL url;
        Reader reader;
        GoogleResults results = null;
        try {
            url = new URL(googleURL + URLEncoder.encode(search, charset));
            reader = new InputStreamReader(url.openStream(), charset);
            results = new Gson().fromJson(reader, GoogleResults.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    static CrawlController []controllers=null;
    static int crawlerIndex = 0;
    private static void setCrawlerConfig(List<String> urlsToSearch) throws Exception {
        String crawlStorageFolder = "data/crawl/root";
        int numberOfCrawlers = Globals.MAX_NUMBER_CRAWLERS;//Crawling 10 pages at a time cause thats how many search results return
        int maxDepthOfCrawling = Globals.WEBPAGE_LINK_DEPTH;//Crawl only the top webpage

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setMaxDepthOfCrawling(maxDepthOfCrawling);
        config.setPolitenessDelay(500);
        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        controllers[crawlerIndex] = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        for (String urlToSearch : urlsToSearch) {
            //crawler doesn't work on wikipedia
            if (!urlToSearch.toLowerCase().contains("en.wikipedia.org".toLowerCase())) {
                controllers[crawlerIndex].addSeed(urlToSearch);
            }
        }
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        
        if (BUILD_SHINGLE) {
            controllers[crawlerIndex].startNonBlocking(ShingleBuilderCrawler.class, numberOfCrawlers);
        }

        if (BUILD_ASSOCIATION) {
            controllers[crawlerIndex].startNonBlocking(AssociationCrawler.class, numberOfCrawlers);
        }
        crawlerIndex++;
    }

    static String[] searchTerms = {"\"?\" measurement instrument", "how to measure \"?\""};


    public static void main(String[] args) throws Exception {

        DB db = MongoConnector.getDatabase();
        DBCollection phenomena = db.getCollection("phenomena");
        DBCollection sensors = db.getCollection("sensors");
        DBCursor cursor;

        //Fill phenomenon list
        cursor = phenomena.find();
        try {
            while (cursor.hasNext()) {
                DBObject phenomenon = cursor.next();
                String name = (String) phenomenon.get("phenomena");
                phenomenaNames.put(name, true);
            }
        } finally {
            cursor.close();
        }

        //Fill sensor list
        cursor = sensors.find();
        try {
            while (cursor.hasNext()) {
                DBObject sensor = cursor.next();
                String name = (String) sensor.get("sensor");
                sensorNames.put(name, true);
            }
        } finally {
            cursor.close();
        }

        //Initialize the controller objects
        controllers = new CrawlController[phenomenaNames.size()];
        
        //Grab all the URL's to search
        for (String phenomenon : phenomenaNames.keySet()) 
        {
            //System.out.println("Phenomena: " + phenomenon);

            List<String> urlsToSearch = new ArrayList<String>();
            //Query for each search term
            for (String searchterm : searchTerms) {
                System.out.println("Search Term: " + searchterm.replace("?", phenomenon));
                for (int i = 1; i <= Globals.SEARCH_PAGE_DEPTH; i++) {
                    GoogleResults results = getGoogleResults(searchterm.replace("?", phenomenon), i);
                    
                    if(results == null)continue;
                   ResponseData responseData = results.getResponseData();
                   if(responseData == null)continue;
                    List<Result> resultList = results.getResponseData().getResults();
                    if(resultList == null)continue;
                    for (Result result : resultList) {
                        String urlToSearch = result.getUrl();
                        //System.out.println("Searching URL: " + urlToSearch);
                        urlsToSearch.add(urlToSearch);
                    }
                }
            }

            //Start the crawlers
            setCrawlerConfig(urlsToSearch);
            
            
            //Wait for the crawler ot finish crawling all sites for phenomena
            //controllers[crawlerIndex-1].waitUntilFinish();
            
            //Query all phenomena of size K
            for(int k = 2;k < 6;k++)
            {
                db = MongoConnector.getDatabase();

                // Setup the query
                BasicDBObject query = new BasicDBObject("phenomena", phenomenon)
                        .append("stopwords", 0)
                        .append("k", k);
                cursor = db.getCollection("shingle").find(query);
                
                HashMap<String,Integer> shingle = new HashMap<String,Integer>();
                while(cursor.hasNext())
                {
                    DBObject data = cursor.next();
                    BasicDBList associations = (BasicDBList) data.get("sets");
                    Iterator it = associations.iterator();
                    while(it.hasNext())
                    {
                        DBObject obj = (DBObject)it.next();
                        String set = (String) obj.get("set");
                        int count = Integer.parseInt((String) obj.get("count"));
                        System.out.println("The Shingle: "+set+ " Count: "+count);
                        
                        Integer cnt = shingle.get(set);
                        if(cnt == null){
                            shingle.put(set, count);
                        }else{
                            shingle.put(set, cnt + count);
                        }
                    }
                }
            }
        }
        
        //Wait for he crawlers to finish
        for(int i = 0;i <= crawlerIndex;i++){
            controllers[crawlerIndex].waitUntilFinish();
            System.out.println("Controller "+ crawlerIndex + " is finished.");
        }
        
    }
}

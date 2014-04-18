package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.ucla.cs218.sensite.MongoConnector;
import java.util.HashMap;
import java.util.HashSet;

public class Crawler extends WebCrawler {

    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     * @return 
     */
    @Override
    public boolean shouldVisit(WebURL url) {
        String href = url.getURL().toLowerCase();
        //Check the Database to see if it's already been crawled
        if (FILTERS.matcher(href).matches()) {
            return false;
        }

        //Check the Database to see if it's already been crawled
        DB db = MongoConnector.getDatabase();

        // Setup the query
        BasicDBObject query = new BasicDBObject("webpage", href);
        DBObject obj = db.getCollection("webpages").findOne(query);
        return obj == null;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
//            String url = page.getWebURL().getURL();
//            System.out.println("URL: " + url);

        Matcher matcher = new Matcher(page.getWebURL());
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();

            // Clean and format the text
            String textClean = text.trim().replaceAll("\\s+", " ");
            textClean = textClean.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
            textClean = textClean.replaceAll("\\s+", " ");
   
            //Match phenomenons and sensors based in text from the website crawled
            if (SPLIT_ON_PUNCTIONATION_MARKS) {
                String[] lines = textClean.split("[.?!;:\\r?\\n]+");
                HashMap <String, HashSet<String>>relations=new HashMap <String, HashSet<String>>();
                for (String line : lines) {
                    
                    matcher.matchPhenomenonToSensor(line,relations);
                }
                matcher.saveStatisticsToDB(relations);
            }
        }
    }
}

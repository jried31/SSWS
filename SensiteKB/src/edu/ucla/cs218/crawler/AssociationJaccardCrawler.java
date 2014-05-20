package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.ucla.cs218.sensite.MongoConnector;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;
import wordnet.wordnet;

public class AssociationJaccardCrawler extends WebCrawler {

    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private  static int k = 3; //stopwords
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
    public void visit(Page page) 
    {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        Matcher matcher = new Matcher(page.getWebURL());
        if (page.getParseData() instanceof HtmlParseData) 
        {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getHtml();
            
            //Use JSoup to grab the P elements ONLY
            Document doc = Jsoup.parse(text);
            Elements ptag = doc.getElementsByTag("p");
            StringBuilder dataBuffer = new StringBuilder();
            for (Element data : ptag){
                dataBuffer.append(data.text().toLowerCase()+" ");
            }
            String content = dataBuffer.toString();
            System.out.println(content);
            System.out.println("done");
            
            wordnet wordNet = new wordnet();
    
            // Clean and format the text
            String textClean = text.trim().replaceAll("\\s+", " ");
   
            //Match phenomenons and sensors based in text from the website crawled
            if (SPLIT_ON_PUNCTIONATION_MARKS) 
            {
                String[] lines = textClean.split("[.?!]+-");
                SnowballStemmer stemmer = new SnowballStemmer();  // initialize stopwords
                Stopwords stopwords = new Stopwords();
                stemmer.setStemmer("english");
                                    
                                    
                for (String phenomena : Controller.phenomenaNames.keySet()) 
                {
                    HashMap<String,Integer> freqCountStopwords = new HashMap<String,Integer>();
                    HashMap<String,Integer> freqCountWithoutStopwords = new HashMap<String,Integer>();

                    for (String line : lines) 
                    {
                        line = line.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
                        line = line.replaceAll("\\s+", " ");

                        if (line.matches(".*\\b"+phenomena+"\\b.*"))
                        {
                            //System.out.println("Visit() Phenomena Match: "+phenomena);
                            String words[]=line.split(" ");
                            //Remove stopwords
                            LinkedList<String> sentenceStopWords = new LinkedList<String> (),
                                    sentencewithoutStopWords = new LinkedList<String> ();
                            
                            //Split the words by space word by word
                            for(String word:words){
                                if(word.length() > 0){
                                    word=stemmer.stem(word);
                                    //anding one with and one w/o stopwords
                                    if(!stopwords.is(word))
                                        sentencewithoutStopWords.add(word);

                                    sentenceStopWords.add(word);
                                }
                            }
                            //System.out.println("List w/o stops: "+sentencewithoutStopWords);


                             //Put the words into k "hard coded" shingles without stopwords
                            if(sentencewithoutStopWords.size() >= k)
                            {
                                for(int i = 0;i < sentencewithoutStopWords.size()-(k);i++)
                                {
                                    String set=sentencewithoutStopWords.get(i)+"@";
                                    for(int j = i+1;j < i+(k);j++){
                                        set+=sentencewithoutStopWords.get(j)+"@";
                                    }

                                    //Add the set to the Hashmap & count freq
                                    Integer count=freqCountWithoutStopwords.get(set);
                                    if(count == null)
                                        count=1;
                                    else count++;
                                    freqCountWithoutStopwords.put(set, count);
                                    //System.out.println("Set w/o stop: "+ set);
                                }
                            }


                            //System.out.println("List w stops: "+sentenceStopWords);
                             //Put the words into k "hard coded" shingles with stopwords
                            if(sentenceStopWords.size() >= k)
                            {
                                for(int i = 0;i < sentenceStopWords.size()-(k);i++)
                                {
                                    String set=sentenceStopWords.get(i)+"@";
                                    for(int j = i+1;j < i+(k);j++){
                                        set+=sentenceStopWords.get(j)+"@";
                                    }

                                    //Add the set to the Hashmap & count freq
                                    Integer count=freqCountStopwords.get(set);
                                    if(count == null)
                                        count=1;
                                    else count++;
                                    freqCountStopwords.put(set, count);

                                    //System.out.println("Set w/Stop: "+ set);

                                //matcher.matchPhenomenonToSensor(line,relations);
                                }
                            }
                            
                            //Compare sentence shingle set to known Shingles from DB
                            DB db = MongoConnector.getDatabase();
                            
                            //Grab known shingles of size k
                            BasicDBObject query = new BasicDBObject("phenomena", phenomena)
                                    .append("stopwords", 0)
                                    .append("k", k);
                            DBCursor cursor = db.getCollection("shingleClean").find(query);

                            HashMap<String,Integer> shinglesWOStopWords = new HashMap<String,Integer>();
                            while(cursor.hasNext())
                            {
                                DBObject data = cursor.next();
                                BasicDBList associations = (BasicDBList) data.get("sets");
                                Iterator it = associations.iterator();
                                while(it.hasNext())
                                {
                                    DBObject obj = (DBObject)it.next();
                                    String set = (String) obj.get("set");
                                    Integer count=(Integer) obj.get("count");
                                    //System.out.println("The Shingle: "+set+ " Count: "+count);

                                    Integer cnt = shinglesWOStopWords.get(set);
                                    if(cnt == null){
                                        shinglesWOStopWords.put(set, count);
                                    }else{
                                        shinglesWOStopWords.put(set, cnt + count);
                                    }
                                }
                            }
                
                            //Find out Similarity between sentance and stop words one (Argument 1 is shingleswostopwords from the shingles database, argument 2 is the sentence parsed into shingles)
                           //JaccardIndex jaccard = new JaccardIndex(shinglesWOStopWords,freqCountStopwords);
                           
                                    
                            //Compare sentence shingle set to known Shingles from DB
                            db = MongoConnector.getDatabase();
                            
                            //Grab known shingles of size k
                            query = new BasicDBObject("phenomena", phenomena)
                                    .append("stopwords", 1)
                                    .append("k", k);
                            cursor = db.getCollection("shingleClean").find(query);

                            HashMap<String,Integer> shinglesStopWords = new HashMap<String,Integer>();
                            while(cursor.hasNext())
                            {
                                DBObject data = cursor.next();
                                BasicDBList associations = (BasicDBList) data.get("sets");
                                Iterator it = associations.iterator();
                                while(it.hasNext())
                                {
                                    DBObject obj = (DBObject)it.next();
                                    String set = (String) obj.get("set");
                                    Integer count=(Integer) obj.get("count");
                                    //System.out.println("The Shingle: "+set+ " Count: "+count);

                                    Integer cnt = shinglesWOStopWords.get(set);
                                    if(cnt == null){
                                        shinglesWOStopWords.put(set, count);
                                    }else{
                                        shinglesWOStopWords.put(set, cnt + count);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
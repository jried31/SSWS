package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;
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
import java.util.LinkedList;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;
import wordnet.wordnet;

public class ShingleBuilderCrawler extends WebCrawler {

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
        DBObject obj = db.getCollection("shingle").findOne(query);
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
            System.out.println("Visit() URL: " + url);

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
            //System.out.println(content);
            
            wordnet wordNet = new wordnet();
            LinkedList<HashMap<String,HashMap<String,Integer>>> shinglesStopWords = new LinkedList<HashMap<String,HashMap<String,Integer>>>();
            LinkedList<HashMap<String,HashMap<String,Integer>>> shinglesWOStopWords = new LinkedList<HashMap<String,HashMap<String,Integer>>>();
            if (SPLIT_ON_PUNCTIONATION_MARKS) 
            {
                String[] lines = content.split("[.?!]+-");
                SnowballStemmer stemmer = new SnowballStemmer();  // initialize stopwords
                Stopwords stopwords = new Stopwords();
                stemmer.setStemmer("english");
                
                for(int k = 2;k < 6;k++)
                {
                    HashMap<String,HashMap<String,Integer>> shingleKStopWords = new HashMap<String,HashMap<String,Integer>>();
                    HashMap<String,HashMap<String,Integer>> shingleKWOStopWords = new HashMap<String,HashMap<String,Integer>>();
                    shinglesStopWords.add(shingleKStopWords);
                    shinglesWOStopWords.add(shingleKWOStopWords);
                    
                    for (String phenomena : Controller.phenomenaNames.keySet()) 
                    {
                        HashMap<String,Integer> freqCountStopwords = new HashMap<String,Integer>();
                        HashMap<String,Integer> freqCountWithoutStopwords = new HashMap<String,Integer>();
                        shingleKStopWords.put(phenomena, freqCountStopwords);
                        shingleKWOStopWords.put(phenomena, freqCountWithoutStopwords);
                        
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
                                
                                
                                 //Create the Shingle list without the stop words
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
                            }
                        }
                    }
                }
                
                
                
                
               //Ready to insert values into the DB
               DB db3 = MongoConnector.getDatabase();
               //Iterate through the Shingle LinkList
               for(int i = 2;i < 6;i++)
               {
                   //Grab the Stopwords for each K
                   HashMap<String,HashMap<String,Integer>> shingleKStopWords=shinglesStopWords.get(i-2);
                   HashMap<String,HashMap<String,Integer>> shingleKWOStopWords=shinglesWOStopWords.get(i-2);
                   Set<String>phenomenasStop = shingleKStopWords.keySet();
                   Set<String>phenomenasWOStop = shingleKWOStopWords.keySet();
                    
                   //Iterate through the Sets ~ Phenomena
                   for(String phenomena:phenomenasStop)
                   {
                       //Grab Freq Counts for Stopwords
                        HashMap<String,Integer> freqCountStopwords = shingleKStopWords.get(phenomena);
                        
                        BasicDBObject rowStop = new BasicDBObject("phenomena",phenomena)
                            .append("webpage", url)
                            .append("k",i)
                            .append("stopwords",0);
                
                        
                        //Iterate through each shingle set
                        Set<String>shingleStop = freqCountStopwords.keySet();      
                                                
                        if(shingleStop.isEmpty())
                            continue;
                        
                        BasicDBList relationship = new BasicDBList();
                        for(String set:shingleStop){
                            relationship.add(new BasicDBObject("set",set).append("count",freqCountStopwords.get(set)));
                            //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                        }
                        
                        rowStop.append("sets",relationship);
                        //System.out.println("Set With Stopwords:\n"+rowStop);
                        db3.getCollection("shingle").insert(rowStop);  
                   }
                   
                   
                //Iterate through the Sets ~ Phenomena without stopwords
                   for(String phenomena:phenomenasWOStop)
                   {
                        //Grab Freq Counts for vector w/o Stopwords
                        HashMap<String,Integer> freqCountWithoutStopwords = shingleKWOStopWords.get(phenomena);
                        
                        BasicDBObject rowWOStop = new BasicDBObject("phenomena",phenomena)
                            .append("webpage", url)
                            .append("k",i)
                            .append("stopwords",1);
                
                        //Iterate through each shingle set
                        Set<String>shingleWOStop = freqCountWithoutStopwords.keySet();  
                        
                        //Loop over for sets that are empty
                        if(shingleWOStop.isEmpty())
                            continue;
                        
                        BasicDBList relationshipWOStop = new BasicDBList();
                        for(String set:shingleWOStop){
                            relationshipWOStop.add(new BasicDBObject("set",set).append("count",freqCountWithoutStopwords.get(set)));
                            //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                        }
                        
                        rowWOStop.append("sets",relationshipWOStop);
                        //System.out.println("Set Without Stopwords:\n"+relationshipWOStop);
                        db3.getCollection("shingle").insert(rowWOStop);  
                   }
               }  
            }
        }
    }
}
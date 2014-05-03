package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.ucla.cs218.sensite.MongoConnector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;
import wordnet.wordnet;

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
/*
        //Check the Database to see if it's already been crawled
        DB db = MongoConnector.getDatabase();

        // Setup the query
        BasicDBObject query = new BasicDBObject("webpage", href);
        DBObject obj = db.getCollection("webpages").findOne(query);
        return obj == null;
        */
        return true;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
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
            StringBuffer dataBuffer = new StringBuffer();
            for (Element data : ptag) {
                dataBuffer.append(data.text().toLowerCase()+" ");
            }
            String content = dataBuffer.toString();
            System.out.println(content);
            System.out.println("done");
            
            wordnet wordNet = new wordnet();
            if (SPLIT_ON_PUNCTIONATION_MARKS) 
            {
                String[] lines = content.split("[.?!]+");
                SnowballStemmer stemmer = new SnowballStemmer();  // initialize stopwords
                Stopwords stopwords = new Stopwords();
                stemmer.setStemmer("english");
                for(int k = 2;k < 6;k++)
                {
                    for (String line : lines) 
                    {
                        line = line.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
                        line = line.replaceAll("\\s+", " ");
                        //Remove stop words
                        for (String phenomena : Controller.phenomenaNames.values()) 
                        {
                            HashMap<String,Integer> freqCountWithoutStopwords = new HashMap<String,Integer>(),
                                    freqCountStopwords = new HashMap<String,Integer>();
                            if (line.matches(".*\\b"+phenomena+"\\b.*"))
                            {
                                System.out.println("Phenomena: "+phenomena);
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
                                System.out.println("List w/o stops: "+sentencewithoutStopWords);
                                
                                 //Create the Shingle list without the stop words
                                int size = sentencewithoutStopWords.size();
                                if(size >= k)
                                {
                                    for(int i = 0;i < sentencewithoutStopWords.size()-(k);i++)
                                    {
                                        String set=sentencewithoutStopWords.get(i)+"-";
                                        for(int j = i+1;j < i+(k);j++){
                                            set+=sentencewithoutStopWords.get(j)+"-";
                                        }
                                        
                                        //Add the set to the Hashmap & count freq
                                        Integer count=freqCountWithoutStopwords.get(set);
                                        if(count == null)
                                            count=1;
                                        else count++;
                                        freqCountWithoutStopwords.put(set, count);
                                        System.out.println("Set: "+ set);
                                    }
                                    
                                    //Add to DB                   
                                    DB db3 = MongoConnector.getDatabase();
                                    // Setup the query
                                    BasicDBList shingle = new BasicDBList();
                                    BasicDBObject row =new BasicDBObject("phenomena",phenomena)
                                            .append("k",k)
                                            .append("stopwords",0);
                                    BasicDBList relationship = new BasicDBList();
                                    for(String key: freqCountWithoutStopwords.keySet()){
                                        relationship.add(new BasicDBObject("set",key).append("count",freqCountWithoutStopwords.get(key)));
                                        //System.out.println("Key: "+key + " - Value: "+ freqCountWithoutStopwords.get(key));
                                    }
                                    row.append("sets",relationship);
                                    shingle.add(row);
                                    db3.getCollection("shingle").insert(row);
                                }
                                
                                
                                System.out.println("List w stops: "+sentenceStopWords);
                                if(sentenceStopWords.size() >= k)
                                {
                                    for(int i = 0;i < sentenceStopWords.size()-(k);i++)
                                    {
                                        String set=sentenceStopWords.get(i)+"-";
                                        for(int j = i+1;j < i+(k);j++){
                                            set+=sentenceStopWords.get(j)+"-";
                                        }

                                        //Add the set to the Hashmap & count freq
                                        Integer count=freqCountStopwords.get(set);
                                        if(count == null)
                                            count=1;
                                        else count++;
                                        freqCountStopwords.put(set, count);

                                        //System.out.println("Set: "+ set);
                                    
                                    //matcher.matchPhenomenonToSensor(line,relations);
                                    }
                                    
                                    // Setup the query
                                    DB db3 = MongoConnector.getDatabase();
                                    BasicDBList shingle = new BasicDBList();
 BasicDBObject row =new BasicDBObject("phenomena",phenomena)
                                            .append("k",k)
                                            .append("stopwords",0);
                                                                      

// shingle.add(new BasicDBObject("phenomena",phenomena)
                                     //       .append("k",k)
                                       //     .append("stopwords",1));
                                    BasicDBList relationship = new BasicDBList();
                                    for(String key: freqCountStopwords.keySet()){
                                        relationship.add(new BasicDBObject("set",key).append("count",freqCountStopwords.get(key)));
                                        //System.out.println("Key: "+key + " - Value: "+ freqCountStopwords.get(key));
                                    }
                                    row.append("sets",relationship);
                                    db3.getCollection("shingle").insert(row);
                                }
                            }
                        }
                    }
                }
            }
            /*
            // Clean and format the text
            String textClean = text.trim().replaceAll("\\s+", " ");
   
            //Match phenomenons and sensors based in text from the website crawled
            if (SPLIT_ON_PUNCTIONATION_MARKS) 
            {
                String[] lines = textClean.split("[.?!]+");
                HashMap <String, HashSet<String>>relations=new HashMap <String, HashSet<String>>();
                for (String line : lines) {
                    
                    line = line.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
                    line = line.replaceAll("\\s+", " ");
                    System.out.println("Line: "+ line);
                    for(String word:line.split(" ")){
                        try {
                            word = word.toLowerCase();
                                if(wordNet.isNoun(word)){
                                    System.out.print("NOUN: " + word +" | ");
                                }
                                if(wordNet.isVerb(word))
                                {
                                    System.out.print("VERB: " + word +" | ");
                                }
                                if(wordNet.isAdjective(word))
                                {
                                    System.out.print("ADJECTIVE: " + word +" | ");
                                }
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
                        //matcher.matchPhenomenonToSensor(line,relations);
                    }
                    System.out.println("\n\n");
                }
                matcher.saveStatisticsToDB(relations);
            }
            */
        }
    }
}

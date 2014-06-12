package edu.ucla.cs218.crawler;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import static edu.ucla.cs218.crawler.Jaccard.jaccardSimilarity;
import edu.ucla.cs218.sensite.MongoConnector;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.ArrayUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import wordnet.wordnet;
import weka.core.Stopwords;
import weka.core.stemmers.SnowballStemmer;

public class AssociationCrawlerWithThreshold extends WebCrawler {
    
    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|bmp|gif|jpe?g"
            + "|png|tiff?|mid|mp2|mp3|mp4"
            + "|wav|avi|mov|mpeg|ram|m4v|pdf"
            + "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     *
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
        
        //filteredWebsites is using Jaccard Similarity Index; websites1 is without (as before)
        DBObject obj = db.getCollection(Controller.FILTERED_ON ? "filteredWebsites" : "websites1").findOne(query);
        return obj == null;
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL();
        System.out.println("URL: " + url);

        File file = new File("Jaccard.txt");
        FileWriter fw = null;

        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                file.createNewFile();
                fw = new FileWriter(file, true);
            } catch (IOException ex) {
                Logger.getLogger(AssociationCrawlerWithThreshold.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Matcher matcher = new Matcher(page.getWebURL());
        Jaccard jaccard = new Jaccard();
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getHtml();

            //Use JSoup to grab the P elements ONLY
            Document doc = Jsoup.parse(text);
            Elements ptag = doc.getElementsByTag("p");
            StringBuilder dataBuffer = new StringBuilder();
            for (Element data : ptag) {
                dataBuffer.append(data.text().toLowerCase() + " ");
            }
            String content = dataBuffer.toString();
            System.out.println(content);
            System.out.println("done");

            //wordnet wordNet = new wordnet();
            // Clean and format the text
            String textClean = text.trim().replaceAll("\\s+", " ");

            //Match phenomenons and sensors based in text from the website crawled
            if (SPLIT_ON_PUNCTIONATION_MARKS) {
                String[] lines = textClean.split("[.?!+-]");
                SnowballStemmer stemmer = new SnowballStemmer();  // initialize stopwords
                Stopwords stopwords = new Stopwords();
                stemmer.setStemmer("english");

                HashMap<String, HashSet<String>> relations = new HashMap<>();
                for (String line : lines) {
                    HashMap<String, HashSet<String>> lineRelations = new HashMap<>();
                    HashMap<String, Double> similarityMap = new HashMap<String, Double>();

                    line = line.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
                    line = line.replaceAll("\\s+", " ");
                    System.out.println("Line: " + line);
                    /*
                     for (String word : line.split(" ")) {
                     try {
                     word = word.toLowerCase();
                     if (wordNet.isNoun(word)) {
                     System.out.print("NOUN: " + word + " | ");
                     }
                     if (wordNet.isVerb(word)) {
                     System.out.print("VERB: " + word + " | ");
                     }
                     if (wordNet.isAdjective(word)) {
                     System.out.print("ADJECTIVE: " + word + " | ");
                     }
                     } catch (Exception e) {
                     e.printStackTrace();
                     }
                     }
                     */

                    matcher.matchPhenomenonToSensor(line, lineRelations);
                    System.out.println("\n\n");

                    for (String phenomenon : lineRelations.keySet()) {
                        String lineWOPhenSens = line.toLowerCase().replace(phenomenon, "");
                        for (String sensor : lineRelations.get(phenomenon)) {
                            lineWOPhenSens = lineWOPhenSens.replace(sensor, "");
                        }
                        try {
                            HashSet<String> Shingle = new HashSet<>();
                            double similarity[] = Jaccard.jaccardSimilarity(lineWOPhenSens, 1, 1, phenomenon,Shingle);
                            //fw.write("Url: " + url + "; Line: " + line + "; Similarity: " + Double.toString(similarity_sum / similarity.length));
                            
                            if ((!Controller.FILTERED_ON || Collections.max(Arrays.asList(ArrayUtils.toObject(similarity))) > 0.05) && !lineRelations.get(phenomenon).isEmpty()) {
                                if (!relations.containsKey(phenomenon)) {
                                    relations.put(phenomenon, new HashSet<String>());
                                }
                                relations.get(phenomenon).addAll(lineRelations.get(phenomenon));
                                /*
                                for (String rel : lineRelations.get(phenomenon)) {
                                    relations.get(phenomenon).add(rel);
                                }
                                */
                                similarityMap.put(phenomenon, Collections.max(Arrays.asList(ArrayUtils.toObject(similarity))));
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(AssociationCrawlerWithThreshold.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    matcher.saveStatisticsToDB_Filtered(relations, similarityMap);
                    relations.clear();
                }
            }
        }
        /*
        try {
            fw.close();
        } catch (IOException ex) {
            Logger.getLogger(AssociationCrawlerWithThreshold.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import edu.cmu.lti.jawjaw.pobj.POS;
import edu.cmu.lti.jawjaw.pobj.Synset;
import edu.cmu.lti.jawjaw.util.WordNetUtil;
import edu.cmu.lti.lexical_db.data.Concept;
import edu.cmu.lti.ws4j.Relatedness;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import weka.core.Stopwords;
import wordnet.wordnet;
import wordnet.wordnet.RelatednessMetric;

/**
 *
 * @author jried31
 */
public class zzz {
    private static final double MIN_THRESHOLD = 0.85;
    private static final wordnet wordNet = new wordnet();



    public static void main(String args[]){
        
       /*
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date = format.parse("2014/06/10 13:30:00");
            System.out.println(date.getTime());
            
            long start = System.currentTimeMillis();
            // do your work...
            long elapsed = 1402599080000L;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH 'hours', mm 'mins,' ss 'seconds'");
            System.out.println(df.format(new Date(elapsed)));
        } catch (ParseException ex) {
            Logger.getLogger(zzz.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(true)return;
        */
    
        String w1 = "A manager fired the worker.",
                w2 = "An employee was terminated from work by his boss.";
        
        Stopwords stopwords = new Stopwords();
        
        w1 = w1.replaceAll("[.?!]", "");
        w2 = w2.replaceAll("[.?!]", "");
        
        List<String>w1split = wordNet.lemmatize(w1),
                w2split = wordNet.lemmatize(w2);
        
        
        //Remove stopwords
        ArrayList<String> w1NoStop = new ArrayList<String>();
        ArrayList<String> w2NoStop = new ArrayList<String>();
        for(String w:w1split){
            if (!stopwords.is(w)) {
                w1NoStop.add(w);
            }
        }
                
        for(String w:w2split){
            if (!stopwords.is(w)) {
                w2NoStop.add(w);
            }
        }
        
       System.out.println("Stopwords Removed");
       System.out.println("w1: "+w1NoStop);
       System.out.println("w2: "+w2NoStop);
       
       


       /*
       words have multiple senses (ie: definition ranks) so for each word, must iterate through each sense definition
       */
       
       //Iterate through each word in w1
       for(int i = 0;i < w1NoStop.size();i++)
       {
            String word1 = w1NoStop.get(i);
                
            //Iterate through each word w2
            for(int j = 0;j < w2NoStop.size();j++)
            {      
                String word2 = w2NoStop.get(j);
                //System.out.println("Word 1: "+ word1 + " Word 2: "+ word2);
                double score = wordNet.compareWordRelationship(word1,word2,RelatednessMetric.WUP);
                //System.out.println("Score: "+score);
                
                //Obtains replaces words with similar words with same position
                if(score > MIN_THRESHOLD){
                    w2NoStop.set(j, word1);
                    /*System.out.println("Common Words");
                    System.out.println("W1: "+ word1);
                    System.out.println("W2: "+ word2);
                   */
                }
                //System.out.println("\n\n");
            }
        }
       
        System.out.println("New W1: "+w1NoStop);
        System.out.println("New W2: "+w2NoStop);
        
        //wordnet.calculateJaccardIndex(w1NoStop,w2NoStop);
        wordNet.calculateCosineSimilarity(w1NoStop, w2NoStop);
    }
}

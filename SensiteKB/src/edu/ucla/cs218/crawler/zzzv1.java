/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import edu.smu.tspell.wordnet.Synset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import weka.core.Stopwords;
import wordnet.wordnet;

/**
 *
 * @author jried31
 */
public class zzzv1 {
    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private static final wordnet wordNet = new wordnet();

    private static final double MIN_THRESHOLD = 0.90;
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
        
        wordnet wn = new wordnet();
        Stopwords stopwords = new Stopwords();
        
        w1 = w1.replaceAll("[.?!]", "");
        w2 = w2.replaceAll("[.?!]", "");
        
        List<String>w1split = wn.lemmatize(w1),
                w2split = wn.lemmatize(w2);
        
        
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
            //Get definition/synonym sense list for the word (ie: senses in which a wod is used) -  1st order senses only
            Synset[] w1senses = wn.getSynsets(word1);
            //iterate through each sense of the word
            for(int s1idx = 0;s1idx < w1senses.length;s1idx++)
            {
                
                //Iterate through each word w2
                for(int j = 0;j < w2NoStop.size();j++)
                {      
                    String word2 = w2NoStop.get(j);
                    
                    //Get definition/synonym sense list for the word (ie: senses in which a wod is used) - 1st order senses only
                    Synset[] w2senses = wn.getSynsets(word2);
                    //iterate through each sense of the word in w2
                    for(int s2idx = 0;s2idx < w2senses.length;s2idx++)
                    {
                        //Compare the sense relationship for each (sense of a word)
                        double score = compareSynsetRelationship(w1senses[s1idx],w2senses[s2idx]);
                        /*if(score > MIN_THRESHOLD)
                        {
                            w1NoStop.set(i, w1senses[0].getWordForms()[0]);
                            w2NoStop.set(j, w1senses[0].getWordForms()[0]);
                        }*/
                    }
                }
            }
       }
       
        System.out.println("Common Words");
        System.out.println("W1: "+ w1NoStop);
        System.out.println("W2: "+ w2NoStop);
    }

    //Run Jaccard algorithm as that's prettymuch the key factor
    private static double compareSynsetRelationship(Synset w1senses,Synset w2senses){
        String w1synset[] = w1senses.getWordForms(),
                w2synset[] = w2senses.getWordForms();
        
        return compareSenses(w1synset,w2synset);
                
        //because if 2 words are the same, then given the right sense (definition), the bag of words will be the seme
        //Therefore jaccard should work for now at 1st
        //jaccard OR pagerank
    }

    private static double compareSenses(String[] s1Array, String[] s2Array) {
        List<String> listIntersection = new LinkedList<String> ();
        Set<String> listUnion = new TreeSet<String>();
        
        List<String> s1List = Arrays.asList(s1Array);
        List<String> s2List = Arrays.asList(s2Array);
        
        
        System.out.println("\n\n");
        System.out.println("S1 Open "+s1List);
        System.out.println("S2 Open "+s2List);
            
        //Add union (no duplicates using)
        listUnion.addAll(s1List);
        listUnion.addAll(s2List);
        
        //intersection
        for(String s1 : s1List){
            for(String s2:s2List){
                if(s1.equals(s2)){
                    listIntersection.add(s1);
                    break;
                }
            }
        }
        
        
        System.out.println("S Intersection "+listIntersection);
        System.out.println("S Union "+listUnion);
            
        
        int intersectionSize = listIntersection.size(),
                unionSize = listUnion.size(),
                jaccardIndex = unionSize == 0 ? 0:intersectionSize/unionSize;
        
            System.out.println("Intersection size "+ unionSize);
            System.out.println("Union size "+ intersectionSize);
            System.out.println("Jaccard "+jaccardIndex);
            
        /*if(jaccardIndex > 0){
           // System.out.println("S1 Open "+s1List);
            System.out.println("S2 Open "+s2List);

            System.out.println("S Intersection "+listIntersection);
            System.out.println("S Union "+listUnion);

            System.out.println("Intersection size "+ unionSize);
            System.out.println("Union size "+ intersectionSize);
            System.out.println("Jaccard "+jaccardIndex);
        }*/
        return jaccardIndex;
    }
    
}

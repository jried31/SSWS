/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wordnet;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.ucla.cs218.util.Globals;
import java.io.IOException;
import java.lang.System.*;

import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
/**
 * Reference includes information from WS4J, JawJaw API's 
 *
 * @author Jay
 */

public class wordnet {
    public static WordNetDatabase database = WordNetDatabase.getFileInstance();

    public enum RelatednessMetric {
        HSO, LCH, LESK, WUP,
        RES, JCN, LIN,PTH
    }
    
    protected StanfordCoreNLP pipeline;
    
    //WS4J Relatedness calculation
    private static ILexicalDatabase db = new NictWordNet();
    private static RelatednessCalculator[] rcs = {
        new HirstStOnge(db), new LeacockChodorow(db), new Lesk(db),  new WuPalmer(db), 
        new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db)
    };

    public wordnet() {
        System.setProperty("wordnet.database.dir",Globals.WORDNET_DIR);
        
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        this.pipeline = new StanfordCoreNLP(props);
    }
    
    public String lemmatizeSingle(String word)
    {
        String retVal = null;
        List<String> lemmas = new LinkedList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(word);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                retVal= token.get(LemmaAnnotation.class);
                break;
            }
            break;
        }
        return retVal;
    }
        
    public List<String> lemmatize(String documentText)
    {
        List<String> lemmas = new LinkedList<String>();
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas.add(token.get(LemmaAnnotation.class));
            }
        }
        return lemmas;
    }

    public Synset[] getSynsets(String word){
        return database.getSynsets(word);
    }
    
    public boolean isNoun(String word) throws IOException{
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN , false);
    
        //System.out.println(synsets.length);
        if(synsets.length == 0){
            return false;
        }
        else{
            return true;
        }
    }
    
    public boolean isAdjective(String word) throws IOException{
        
        Synset[] synsets = database.getSynsets(word, SynsetType.ADJECTIVE , false);
     
        //System.out.println(synsets.length);
        if(synsets.length == 0){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean isVerb(String word) {
            Synset[] synsets = database.getSynsets(word, SynsetType.VERB , false);
        
        //System.out.println(synsets.length);
        if(synsets.length == 0){
            return false;
        }
        else{
            return true;
        }
    }
    
    public double calculateCosineSimilarity(ArrayList<String> w1NoStop, ArrayList<String> w2NoStop) {
        //NOTE: Point.x = w1 count and Point.y represents w2 count 
        HashMap<String,Point>shingles = new HashMap<String,Point>();
        Point point = null;
        
        /*
                Create Data structure of Word, Point(w1Count, w2Count)
                Word | w1Count | w2Count 
        */
        for(String s:w1NoStop){
            if(shingles.containsKey(s)){
                point = shingles.get(s);
                point.x++;
            }else{
                shingles.put(s,new Point(1,0));
            }
        }
        for(String s:w2NoStop){
            if(shingles.containsKey(s)){
                point = shingles.get(s);
                point.y++;
            }else{
                shingles.put(s,new Point(0,1));
            }
        }
                 
        
        //Calculate dot product A*B 
        double dotProduct = 0;
        for(String s:shingles.keySet()){
            point = shingles.get(s);
            dotProduct += point.x*point.y;
        }
        //Calculate |A|
        double magnitudeOfA = 0;
        for(String s:shingles.keySet()){
            point = shingles.get(s);
            magnitudeOfA += point.x*point.x;
        }
        magnitudeOfA=Math.sqrt(magnitudeOfA);
        
        
        //Calculate |B|
        double magnitudeOfB = 0;
        for(String s:shingles.keySet()){
            point = shingles.get(s);
            magnitudeOfB += point.y*point.y;
        }
        magnitudeOfB=Math.sqrt(magnitudeOfB);
        
        //Calculate Cosine similarity A*B/|A|*|B|
        double magnitude = (magnitudeOfA*magnitudeOfB);
        System.out.println("Cosine Siilarity: "+dotProduct/magnitude);
        return magnitude == 0 ? 0:dotProduct/magnitude;	
    }
    
    public double calculateJaccardIndex(ArrayList<String> w1NoStop, ArrayList<String> w2NoStop) {
        List<String> listIntersection = new LinkedList<String> ();
        Set<String> listUnion = new TreeSet<String>();
        
        System.out.println("\n\n");
        System.out.println("S1 Open "+w1NoStop);
        System.out.println("S2 Open "+w2NoStop);
            
        //Add union (no duplicates using)
        listUnion.addAll(w1NoStop);
        listUnion.addAll(w2NoStop);
        
        //intersection
        for(String s1 : w1NoStop){
            for(String s2:w2NoStop){
                if(s1.equals(s2)){
                    listIntersection.add(s1);
                    break;
                }
            }
        }
        
        
        System.out.println("S Intersection "+listIntersection);
        System.out.println("S Union "+listUnion);
            
        
        int intersectionSize = listIntersection.size(),
                unionSize = listUnion.size();
        double jaccardIndex = unionSize == 0 ? 0:intersectionSize/(1.0*unionSize);
        
        System.out.println("Intersection size "+ unionSize);
        System.out.println("Union size "+ intersectionSize);
        System.out.println("Jaccard "+jaccardIndex);

        return jaccardIndex;
    }
    
    public double compareWordRelationship(String word1, String word2,RelatednessMetric metric) {
        //setMFS restricts the calculation to the most frequenly used senses rather than ALL, 
        //which reduces precision in order to save processing time ... Set to false 
        WS4JConfiguration.getInstance().setMFS(false);
        
        double s = rcs[metric.ordinal()].calcRelatednessOfWords(word1, word2);
        return s;
    }
    
    
}
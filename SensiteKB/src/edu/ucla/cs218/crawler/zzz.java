/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.ucla.cs218.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import java.util.HashMap;
import java.util.HashSet;
import wordnet.wordnet;

/**
 *
 * @author jried31
 */
public class zzz {
    private static final boolean SPLIT_ON_PUNCTIONATION_MARKS = true;
    private static final wordnet wordNet = new wordnet();

    public static void main(String args[]){
        WebURL url = new WebURL();
        url.setURL("http://www.cocorahs.org/Content.aspx?page=measurerain");
        
        Page page = new Page(url);
        
        HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
        if(htmlParseData != null){
            String text = htmlParseData.getHtml();

            // Clean and format the text
            String textClean = text.trim().replaceAll("\\s+", " ");
            textClean = textClean.replaceAll("[^0-9a-zA-Z!.?'\\-]", " ");
            textClean = textClean.replaceAll("\\s+", " ");
   
            //Match phenomenons and sensors based in text from the website crawled
            if (SPLIT_ON_PUNCTIONATION_MARKS) {
                String[] lines = textClean.split("[.?!;:\\r?\\n]+");
                HashMap <String, HashSet<String>>relations=new HashMap <String, HashSet<String>>();
                for (String line : lines) {
                    for(String word:line.split(" ")){
                        try {
                            if(wordNet.isNoun(word)){
                                System.out.println("NOUN: " + word);
                            }
                            if(wordNet.isAdjective(word))
                            {
                                System.out.println("NOT A NOUN/ADJECTIVE: " + word);
                            }
                        }catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("\n\n");
                }
            }     
        }
    }
}

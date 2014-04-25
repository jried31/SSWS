/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wordnet;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import edu.ucla.cs218.util.Globals;
import java.io.IOException;
import java.lang.System.*;

/**
 * Returns 0 if a noun, 1 if not a noun.
 *
 * @author Jay
 */

public class wordnet {
    public static WordNetDatabase database = WordNetDatabase.getFileInstance();
    
    public wordnet() {
        System.setProperty("wordnet.database.dir",Globals.WORDNET_DIR);
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
    
}
package main;

import grammar.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stats {
    private HashMap<String, Values> words = new HashMap<>();
    private HashMap<String, Values> inflections = new HashMap<>();
    private HashMap<String, Values> partsOfSpeech = new HashMap<>();
    private int nrOfRights = 0;
    private int nrOfWrongs = 0;

    private class Values{
        int correct = 0, incorrect = 0;
    }

    /**
     * Save new statistical data
     * @param word A word
     * @param inflection the inflection of the word
     * @param correct whether the word was correctly translated or not
     */
    public void pushStats(Word word, String inflection, boolean correct){
        if(!words.containsKey(word.getForeign()))
            words.put(word.getForeign(), new Values());
        if(!inflections.containsKey(inflection))
            inflections.put(inflection, new Values());
        if(!partsOfSpeech.containsKey(word.getCategory()))
            partsOfSpeech.put(word.getCategory(), new Values());

        if(correct){
            nrOfRights++;
            words.get(word.getForeign()).correct++;
            inflections.get(inflection).correct++;
            partsOfSpeech.get(word.getCategory()).correct++;
        }else{
            nrOfWrongs++;
            words.get(word.getForeign()).incorrect++;
            inflections.get(inflection).incorrect++;
            partsOfSpeech.get(word.getCategory()).incorrect++;
        }
    }

    /**
     * Remove all statistical data
     */
    public void reset(){
        words = new HashMap<>();
        inflections = new HashMap<>();
        partsOfSpeech = new HashMap<>();
    }

    /**
     * Returns a list of the percentages of all words
     * @return
     */
    public Map<String, Integer> getStatsForWords(){
        Map<String, Integer> list = new HashMap<>();
        for(String w : words.keySet())
            list.put(w, getStatsForWord(w));
        return list;
    }

    /**
     * Returns a list of the percentages of all inflections
     * @return
     */
    public Map<String, Integer> getStatsForInflections(){
        Map<String, Integer> list = new HashMap<>();
        for(String i : inflections.keySet())
            list.put(i, getStatsForInflection(i));
        return list;
    }

    public Map<String, Integer> getStatsForPartsOfSpeech(){
        Map<String, Integer> list = new HashMap<>();
        for(String pos : partsOfSpeech.keySet()){
            list.put(pos, getStatsForPartsOfSpeech(pos));
        }
        return list;
    }

    /**
     * Calculates the percentage of correct translations of this word
     * @param word A word in its foreign default form
     * @return the percentage
     */
    private int getStatsForWord(String word){
        if(words.containsKey(word)){
            int total = words.get(word).correct + words.get(word).incorrect;
            return (int) (((float)words.get(word).correct) / ((float)total) * 100);
        }else
            return 0;
    }

    /**
     * Calculates the percentage of correct translations in this inflection
     * @param inflection a inflection
     * @return the percentage
     */
    private int getStatsForInflection(String inflection){
        if(inflections.containsKey(inflection)){
            int total = inflections.get(inflection).correct + inflections.get(inflection).incorrect;
            return (int) (((float)inflections.get(inflection).correct) / ((float)total) * 100);
        }else
            return 0;
    }

    private int getStatsForPartsOfSpeech(String pos){
        if(partsOfSpeech.containsKey(pos)){
            int total = partsOfSpeech.get(pos).correct + partsOfSpeech.get(pos).incorrect;
            return (int) (((float)partsOfSpeech.get(pos).correct) / ((float)total) * 100);
        }else
            return 0;
    }

    public int getNrOfRights(){
        return nrOfRights;
    }

    public int getNrOfWrongs(){
        return nrOfWrongs;
    }
}

package main;

import grammar.Word;

import java.util.HashMap;

public class Stats {
    HashMap<String, Values> words = new HashMap<>();
    HashMap<String, Values> inflections = new HashMap<>();
    HashMap<String, Values> partsOfSpeech = new HashMap<>();

    class Values{
        int correct = 0, incorrect = 0;
    }

    public void pushStats(Word word, String inflection, boolean correct){
        if(!words.containsKey(word.getFunction()))
            words.put(word.getFunction(), new Values());
        if(!inflections.containsKey(inflection))
            inflections.put(inflection, new Values());
        if(!partsOfSpeech.containsKey(word.getCategory()))
            partsOfSpeech.put(word.getCategory(), new Values());

        if(correct){
            words.get(word.getFunction()).correct++;
            inflections.get(inflection).correct++;
            partsOfSpeech.get(word.getCategory()).correct++;
        }else{
            words.get(word.getFunction()).incorrect++;
            inflections.get(inflection).incorrect++;
            partsOfSpeech.get(word.getCategory()).incorrect++;
        }
    }

    public void reset(){
        words = new HashMap<>();
        inflections = new HashMap<>();
        partsOfSpeech = new HashMap<>();
    }

    public int getStatsForWord(Word word){
        if(words.containsKey(word.getFunction())){
            int total = words.get(word.getFunction()).correct + words.get(word.getFunction()).incorrect;
            return (words.get(word.getFunction()).correct / total) * 100;
        }else
            return 0;
    }

    public int getStatsForInflection(String inflection){
        if(inflections.containsKey(inflection)){
            int total = inflections.get(inflection).correct + inflections.get(inflection).incorrect;
            return (inflections.get(inflection).correct / total) * 100;
        }else
            return 0;
    }
}

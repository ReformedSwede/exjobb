package com.example.reformed_swede.grammartrainer.main;

import com.example.reformed_swede.grammartrainer.grammar.Session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GrammarContainer implements Serializable{

    private Session session;

    Map<String, List<String>> partsOfSpeechAndInflections = new HashMap<>();
    Map<String, List<Word>> words = new HashMap<>();

    public class Word implements Serializable {
        public String partOfSpeech;
        public List<String> nativeInflections;
        public List<String> foreignInflections;
    }

    public GrammarContainer(Session session){
        this.session = session;
    }

    public void addPartOfSpeech(String pos, List<String> inflections){
        partsOfSpeechAndInflections.put(pos, inflections);
    }

    public void addWord(String pos, List<String> nativeInflections, List<String> foreignInflections){
        Word w = new Word();
        w.partOfSpeech = pos;
        w.nativeInflections = nativeInflections;
        w.foreignInflections = foreignInflections;
        words.put(pos, new ArrayList<Word>());
        words.get(pos).add(w);
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public List<String> getPartsOfSpeech(){
        return Arrays.asList(partsOfSpeechAndInflections.keySet().toArray(new String[0]));
    }

    public List<String> getInflectionsByPartOfSpeech(String partOfSpeech){
        return partsOfSpeechAndInflections.get(partOfSpeech);
    }

    public List<Word> getAllWords(String partOfSpeech){
        return words.get(partOfSpeech);
    }
}

package com.example.reformed_swede.grammartrainer.grammar;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrammarManager {

    GrammarContainer grammarContainer;

    public GrammarManager(Session session, Context context){
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "grammar"
                + File.separator + session.getTitle());

        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            grammarContainer = (GrammarContainer)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<Session> getSessions(Context context){
        File dir = new File(context.getFilesDir().getAbsolutePath() + File.separator + "grammar");
        dir.mkdir();
        List<Session> sessions = new ArrayList<>();
        for(File f : dir.listFiles()){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
                GrammarContainer gc = (GrammarContainer) ois.readObject();
                sessions.add(gc.getSession());
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
        return sessions;
    }

    public List<String> getPartsOfSpeech(){
        return grammarContainer.getPartsOfSpeech();
    }

    public List<String> getInflections(String pos){return grammarContainer.getInflectionsByPartOfSpeech(pos);}

    public List<GrammarContainer.Word> getAllWords(String partOfSpeech){
        return grammarContainer.getAllWords(partOfSpeech);
    }

    /**
     * Generates a random word from the gives parts of speech
     * @param partsOfSpeech The categories to choose from
     * @return A word
     */
    public GrammarContainer.Word getRandomWord(List<String> partsOfSpeech){
        String randPos = partsOfSpeech.get(new Random().nextInt(partsOfSpeech.size()));
        List<GrammarContainer.Word> words = grammarContainer.getAllWords(randPos);

        return words.get(new Random().nextInt(words.size()));
    }

    public int getNrOfWords(){
        int nr = 0;
        for(String pos : grammarContainer.getPartsOfSpeech())
            nr+= grammarContainer.getAllWords(pos).size();
        return nr;
    }

    public int getRandomInflectionId(String pos, List<String> inflections){
        return grammarContainer.getInflectionId(pos,
                inflections.get(new Random().nextInt(inflections.size())));
    }

    public Session getCurrentSession(){
        return grammarContainer.getSession();
    }
}

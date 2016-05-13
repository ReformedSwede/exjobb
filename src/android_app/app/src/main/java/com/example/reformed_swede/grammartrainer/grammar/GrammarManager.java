package com.example.reformed_swede.grammartrainer.grammar;

import android.content.Context;

import com.example.reformed_swede.grammartrainer.main.GrammarContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GrammarManager {

    GrammarContainer container;

    public GrammarManager(Session session, Context context){
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + "grammar"
                + File.separator + session.getTitle());

        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(new FileInputStream(file));
            container = (GrammarContainer)ois.readObject();
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

    public GrammarContainer.Word getRandomWord(){
        List<String> partsOfSpeech = container.getPartsOfSpeech();
        String randPos = partsOfSpeech.get(new Random().nextInt(partsOfSpeech.size()));
        List<GrammarContainer.Word> words = container.getAllWords(randPos);

        return words.get(new Random().nextInt(words.size()));
    }

    public int getNrOfWords(){
        int nr = 0;
        for(String pos : container.getPartsOfSpeech())
            nr+=container.getAllWords(pos).size();
        return nr;
    }

    public int getRandomInflectionId(String pos){
        return new Random().nextInt(container.getInflectionsByPartOfSpeech(pos).size());
    }
}

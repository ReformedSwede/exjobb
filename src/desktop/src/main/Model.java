package main;


import grammar.GrammarManager;
import org.grammaticalframework.pgf.ParseError;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for MVC design pattern. Provides data for views and controllers.
 * Provides an interface for the desktop application to the grammar module.
 */
public class Model {

    private String nativeLangCode;
    private String foreignLangCode;
    GrammarManager manager;

    public void initialize(String nativeLang, String foreignLang){
        this.nativeLangCode = nativeLang;
        this.foreignLangCode = foreignLang;
        manager = new GrammarManager(nativeLang, foreignLang);
    }

    public String getNativeLangCode() {
        return nativeLangCode;
    }

    public String getForeignLangCode() {
        return foreignLangCode;
    }

    public String getRandomWord(){
        return manager.getRandomWord(foreignLangCode);
    }

    public boolean gradeAnswer(String foreignWord, String usersAnswer){
        return manager.gradeAnswer(foreignWord, usersAnswer);
    }

    public List<String> getAllPartOfSpeech(){
        return manager.getAllPartOfSpeech(foreignLangCode);
    }

    public List<String> getAllForeignWords(){
        List<String> words = new ArrayList<>();
        getAllPartOfSpeech().forEach((part) -> words.addAll(manager.getAllWords(foreignLangCode, part)));
        return words;
    }
}
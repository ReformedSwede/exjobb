package main;


import grammar.GrammarManager;

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
        return manager.getRandomWord();
    }

    public boolean gradeAnswer(String foreignWord, String usersAnswer){
        return manager.gradeAnswer(foreignWord, usersAnswer);
    }
}
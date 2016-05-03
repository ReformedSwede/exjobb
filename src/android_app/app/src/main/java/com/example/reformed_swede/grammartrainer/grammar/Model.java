package com.example.reformed_swede.grammartrainer.grammar;


import org.grammaticalframework.pgf.Concr;

import java.util.*;

/**
 * Model for MVC design pattern. Provides data for views and controllers.
 * Provides an interface for the desktop application to the grammar module.
 */
public class Model {

    private String nativeLangCode;
    private String foreignLangCode;
    private GrammarManager manager;

    /**
     * Starts a new session
     * @param nativeLang The native language of the session
     * @param foreignLang The foreign language of the session
     */
    public void initialize(String nativeLang, String foreignLang){
        this.nativeLangCode = nativeLang;
        this.foreignLangCode = foreignLang;
        manager = new GrammarManager(nativeLang, foreignLang);
    }

    /**
     * Ends the initialized session session. If no session was ever initialized, nothing interesting happens.
     */
    public void endSession(){
        nativeLangCode = foreignLangCode = null;
        manager = null;
    }

    /*****Getters*****/

    /**
     * Gives the language code for the native language of the session (i.e. "eng" for English)
     * @return the native language code
     */
    public String getNativeLangCode() {
        return nativeLangCode;
    }

    /**
     * Gives the language code for the foreign language of the session (i.e. "eng" for English)
     * @return the foreign language code
     */
    public String getForeignLangCode() {
        return foreignLangCode;
    }

    /**
     * Returns a random word, but not from the specified categories
     * @param doNotInclude A list of categories not to include
     * @return a random Word
     */
    public Word getRandomWord(String... doNotInclude){
        List<String> catsToInclude = manager.getAllCategories();
        if(doNotInclude.length != 0) {
            final List<String> catsToRemove = Arrays.asList(doNotInclude);
            for (Iterator<String> iterator = catsToInclude.iterator(); iterator.hasNext(); ) {
                String cat = iterator.next();
                if (catsToRemove.contains(cat))
                    iterator.remove();
            }
        }

        List<Word> words = new ArrayList<>();
        for(String cat : catsToInclude)
            words.addAll(manager.getAllWords(cat));
        return words.get(new Random().nextInt(words.size()));
    }

    /**
     * Returns all categories
     */
    public List<String> getAllCategories(){
        return manager.getAllCategories();
    }

    /**
     * Returns all words
     */
    public List<Word> getAllWords(){
        return manager.getAllWords();
    }

    /**
     * Gives all words in the specified category
     * @param cat The category
     * @return All words
     */
    public List<Word> getAllWords(String cat){
        return manager.getAllWords(cat);
    }

    /**
     * Returns a Word object from the specified category, corresponding to the specified foreign word
     * @param category A category
     * @param foreignWord A word in the foreign language
     * @return A Word
     */
    public Word getWordByString(String category, String foreignWord){
        for (Word w : manager.getAllWords(category)){
            if(w.getForeign().equals(foreignWord))
                return w;
        }
        return null;
    }

    /*****Setters******/

    /**
     * Inserts a new word into the file system.
     * @param cat The type of the word, gf category (Keep first letter capitalized!)
     * @param nativeWord The new word in the user's native language
     * @param foreignWord The new word in the foreign language
     * @return True, if the insertion succeeded
     */
    public boolean addNewWord(String cat, String nativeWord, String foreignWord){
        return manager.addWord(cat, nativeWord, foreignWord);
    }

    /**
     * Inserts a new word into the file system.
     * @param category The type of the word, gf category (Keep first letter capitalized!)
     * @param nativeWords All inflection forms of the new word in the user's native language
     * @param foreignWords All inflection forms of the new word in the foreign language
     * @return True, if the insertion succeeded
     */
    public boolean addWordWithInflections(String category, List<String> nativeWords, List<String> foreignWords){
        return manager.addWordWithInflections(category, nativeWords, foreignWords);
    }

    /**
    * Removes a word from the file system.
    * @param cat The type of the word (Keep first letter capitalized!)
    * @param fun The GF function of the word
    */
    public void removeWord(String cat, String fun){
        manager.removeWord(cat, fun);
    }

    /*******Extras********/

    /**
     * Translates a word from one language in the session to the other
     * @param word The word to translate
     * @param cat It's category
     * @param fromForeign If true, translation will happen into the native language. O/w into the foreign
     * @return The translation
     */
    public String translate(String word, String cat, boolean fromForeign){
        return manager.translate(word, cat, fromForeign);
    }
}
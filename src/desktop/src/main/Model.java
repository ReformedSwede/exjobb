package main;


import grammar.GrammarManager;
import grammar.Word;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Model for MVC design pattern. Provides data for views and controllers.
 * Provides an interface for the desktop application to the grammar module.
 */
public class Model {

    private String nativeLangCode;
    private String foreignLangCode;
    private GrammarManager manager;

    /**
     *
     * @param nativeLang
     * @param foreignLang
     */
    public void initialize(String nativeLang, String foreignLang){
        this.nativeLangCode = nativeLang;
        this.foreignLangCode = foreignLang;
        manager = new GrammarManager(nativeLang, foreignLang);
    }

    /**
     *
     */
    public void endSession(){
        nativeLangCode = foreignLangCode = null;
        manager = null;
    }

    /*****Getters*****/

    /**
     *
     * @return
     */
    public String getNativeLangCode() {
        return nativeLangCode;
    }

    /**
     *
     * @return
     */
    public String getForeignLangCode() {
        return foreignLangCode;
    }

    /**
     * Returns a random word, but not from the specified categories
     */
    public Word getRandomWord(String... doNotInclude){
        List<String> catsToInclude = manager.getAllCategories();
        if(doNotInclude.length != 0) {
            final List<String> list = Arrays.asList(doNotInclude);
            catsToInclude = catsToInclude.stream().filter((o) -> !list.contains(o)).collect(Collectors.toList());
        }

        List<Word> words = new ArrayList<>();
        catsToInclude.forEach(s -> words.addAll(manager.getAllWords(s)));
        return words.get(new Random().nextInt(words.size()));
    }

    /**
     *
     * @return
     */
    public List<String> getAllCategories(){
        return manager.getAllCategories();
    }

    /**
     *
     * @return
     */
    public List<Word> getAllWords(){
        return manager.getAllWords();
    }

    /**
     *
     * @param partOfSpeech
     * @return
     */
    public List<Word> getAllWords(String partOfSpeech){
        return manager.getAllWords(partOfSpeech);
    }

    /**
     *
     * @param category
     * @param foreignWord
     * @return
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
}
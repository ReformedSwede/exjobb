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

    private Session session;
    private GrammarManager manager;

    /**
     * Starts a new session
     * @param session the session
     */
    public void initialize(Session session){
        this.session = session;
        manager = new GrammarManager(session);
    }

    /**
     * Ends the initialized session session. If no session was ever initialized, nothing interesting happens.
     */
    public void endSession(){
        session = null;
        manager = null;
    }

    /*****Getters*****/

    /**
     * Gives the language code for the native language of the session (i.e. "eng" for English)
     * @return the native language code
     */
    public String getNativeLangCode() {
        return session.getNativeCode();
    }

    /**
     * Gives the language code for the foreign language of the session (i.e. "eng" for English)
     * @return the foreign language code
     */
    public String getForeignLangCode() {
        return session.getForeignCode();
    }

    /**
     * Returns a random word, but not from the specified categories
     * @param doNotInclude A list of categories not to include
     * @return a random Word
     */
    public Word getRandomWord(String... doNotInclude){
        List<String> catsToInclude = ResourceManager.getPartOfSpeechCats();
        if(doNotInclude.length != 0) {
            final List<String> list = Arrays.asList(doNotInclude);
            catsToInclude = catsToInclude.stream().filter(cat -> !list.contains(cat)).collect(Collectors.toList());
        }

        List<Word> words = new ArrayList<>();
        catsToInclude.forEach(s -> words.addAll(getAllWords(s)));
        return words.get(new Random().nextInt(words.size()));
    }

    /**
     * Returns all categories
     */
    public List<String> getAllCategories(){
        return ResourceManager.getPartOfSpeechCats();
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
     * Returns the total number of words in the database
     * @return # of words
     */
    public int getNrOfWords(){
        int nr = 0;
        for(String partOspeech : ResourceManager.getPartOfSpeechCats())
            nr += manager.getAllWords(partOspeech).size();
        return nr;
    }

    /**
     * Returns a Word object from the specified category, corresponding to the specified foreign word
     * @param category A category
     * @param foreignWord A word in the foreign language, inflected in the first listen form
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
     * @return The just inserted word, as a Word object, or null if it already exists.
     */
    public Word addNewWord(String cat, String nativeWord, String foreignWord){
        return manager.addWord(cat, nativeWord, foreignWord);
    }

    /**
     * Inserts a new word into the file system.
     * @param category The type of the word, gf category (Keep first letter capitalized!)
     * @param nativeWords All inflection forms of the new word in the user's native language
     * @param foreignWords All inflection forms of the new word in the foreign language
     * @return The just inserted word, as a Word object, or null if it already exists.
     */
    public Word addWordWithInflections(String category, List<String> nativeWords, List<String> foreignWords){
        return manager.addWordWithInflections(category, nativeWords, foreignWords);
    }

    /**
    * Removes a word from the file system.
    * @param fun The GF function of the word
    */
    public void removeWord(String fun){
        manager.removeWord(fun);
    }
}
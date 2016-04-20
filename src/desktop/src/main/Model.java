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
    GrammarManager manager;

    public void initialize(String nativeLang, String foreignLang){
        this.nativeLangCode = nativeLang;
        this.foreignLangCode = foreignLang;
        manager = new GrammarManager(nativeLang, foreignLang);
    }

    public void endSession(){
        nativeLangCode = foreignLangCode = null;
        manager = null;
    }

    /*****Getters*****/

    public String getNativeLangCode() {
        return nativeLangCode;
    }

    public String getForeignLangCode() {
        return foreignLangCode;
    }

    /**
     * Returns a random word, but not from the specified parts of speech
     */
    public Word getRandomWord(String... doNotInclude){
        List<String> posToInclude = manager.getAllPartsOfSpeech();
        if(doNotInclude.length != 0) {
            final List<String> list = Arrays.asList(doNotInclude);
            posToInclude = posToInclude.stream().filter((o) -> !list.contains(o)).collect(Collectors.toList());
        }

        List<Word> words = new ArrayList<>();
        posToInclude.forEach(s -> words.addAll(manager.getAllWords(s)));
        return words.get(new Random().nextInt(words.size()));
    }

    public List<String> getAllPartOfSpeech(){
        return manager.getAllPartsOfSpeech();
    }

    public List<Word> getAllWords(){
        return manager.getAllWords();
    }

    public List<Word> getAllWords(String partOfSpeech){
        return manager.getAllWords(partOfSpeech);
    }

    public Word getWordByString(String partOfSpeech, String foreignWord){
        for (Word w : manager.getAllWords(partOfSpeech)){
            if(w.getForeign().equals(foreignWord))
                return w;
        }
        return null;
    }

    /*****Setters******/

    public void addNewWord(String pos, String nativeWord, String foreignWord){
        manager.addWord(pos, nativeWord, foreignWord);
    }

    public void removeWord(String pos, String word){
        manager.removeWord(pos, word);
    }
}
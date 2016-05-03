package com.example.reformed_swede.grammartrainer.grammar;

import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;

import java.util.*;

/**
 * Class containing information about a GF word, i.e. a GF function.
 * Contains methods to get inflection forms of the word in either the native or foreign language.
 */
public class Word {

    private Concr nativeConcr;
    private Concr foreignConcr;
    private String function;
    private String category;

    /**
     * Initializes a new word object
     * @param nativeConcr The concrete syntax of the native language
     * @param foreignConcr The concrete syntax of the foreign language
     * @param function The gf function of the word
     * @param category The gf cat of the word
     */
    public Word(Concr nativeConcr, Concr foreignConcr, String function, String category){
        this.nativeConcr = nativeConcr;
        this.foreignConcr = foreignConcr;
        this.function = function;
        this.category = category;
    }

    /**
     * Initialized a new word object
     * @param pgf The pgf file that the word comes from
     * @param nativeLangCode The language code of the native language (e.g. "eng" for English)
     * @param foreignLangCode The language code of the foreign language (e.g. "swe" for Swedish)
     * @param function The gf function of the word
     * @param category The gf cat of the word
     */
    public Word(PGF pgf, String nativeLangCode, String foreignLangCode, String function, String category){
        nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLangCode));
        foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreignLangCode));
        this.function = function;
        this.category = category;
    }

    /**
     * Gives the word in its default native form
     * @return the native word
     */
    public String getNative(){
        return nativeConcr.linearize(new Expr(function, new Expr[0]));
    }

    /**
     * Gives the word in its default foreign form
     * @return the foreign word
     */
    public String getForeign(){
        return foreignConcr.linearize(new Expr(function, new Expr[0]));
    }

    /**
     * Gives the gf function of the word
     * @return the function
     */
    public String getFunction(){
        return function;
    }

    /**
     * Gives the gf cat of the word
     * @return The category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gives the name of the native language
     * @return The native language
     */
    public String getNativeLanguage(){
        return Utils.gfToName(nativeConcr.getName());
    }

    /**
     * Gives the name of the foreign language
     * @return The foreign language
     */
    public String getForeignLanguage(){
        return Utils.gfToName(foreignConcr.getName());
    }

    /**
     * Returns the word, either native or foreign, depending on parameter
     * @param getNative If true, returns the native word, o/w the foreign
     * @return The word, either native or foreign
     */
    public String getWord(boolean getNative){
        return getNative ? getNative() : getForeign();
    }

    /**
     * Returns an array with the names of all foreign inflection forms.
     * @return All foreign inflection names
     */
    public List<String> getForeignInflectionNames(){
        return new ArrayList<>(foreignConcr.tabularLinearize(new Expr(function, new Expr[0])).keySet());
    }

    /**
     * Returns an array with the names of all native inflection forms.
     * @return All native inflection names
     */
    public List<String> getNativeInflectionNames(){
        return new ArrayList<>(nativeConcr.tabularLinearize(new Expr(function, new Expr[0])).keySet());
    }

    /**
     * Gives a list of all inflection names, either native or foreign, depending on parameter.
     * @param foreign If true returns all foreign inflections. Else, returns all native inflections
     * @return all inflection names
     */
    public List<String> getInflectionNames(boolean foreign){
        if(foreign)
            return getForeignInflectionNames();
        else
            return getNativeInflectionNames();
    }

    /**
     * Returns a random foreign inflection form name
     */
    public String getRandomForeignInflectionName(){
        List<String> inflections = getForeignInflectionNames();
        return inflections.get(new Random().nextInt(inflections.size()));
    }

    /**
     * Returns a random native inflection form name
     */
    public String getRandomNativeInflectionName(){
        List<String> inflections = getNativeInflectionNames();
        return inflections.get(new Random().nextInt(inflections.size()));
    }

    /**
     * Returns a random inflection form name
     * @param foreign If true returns a foreign inflection. Else, returns native inflection
     * @return a random inflection name
     */
    public String getRandomInflectionName(boolean foreign){
        if(foreign) {
            List<String> inflections = getForeignInflectionNames();
            return inflections.get(new Random().nextInt(inflections.size()));
        }else {
            List<String> inflections = getNativeInflectionNames();
            return inflections.get(new Random().nextInt(inflections.size()));
        }
    }


    /**
     * Returns the word inflected in the specified form
     * @param inflectionName The form in which the word should be inflected
     * @return The word in the foreign language
     */
    public String getForeignInflectionFormByName(String inflectionName){
        return foreignConcr.tabularLinearize(new Expr(function, new Expr[0])).get(inflectionName);
    }

    /**
    * Returns the word inflected in the specified form
    * @param inflectionName The form in which the word should be inflected
    * @return The word in the native language
    */
    public String getNativeInflectionFormByName(String inflectionName){
        return nativeConcr.tabularLinearize(new Expr(function, new Expr[0])).get(inflectionName);
    }

    /**
     * Returns the word inflected in the specified form
     * @param inflectionName The form in which the word should be inflected
     * @param getNative Specifies whether the returned string should be native or foreign
     * @return The word in either native or foreign language
     */
    public String getWordInflectionFormByName(String inflectionName, boolean getNative){
        return getNative ? getNativeInflectionFormByName(inflectionName) :
                getForeignInflectionFormByName(inflectionName);
    }

    /**
     * Checks if the answer is a correct translation of the word and in the correct inflection form.
     * @param answer A word in the foreign language
     * @param inflectionName The form that the answer is supposed to be inflected in.
     * @return true if correct translation, false o/w
     */
    public boolean checkInflectedAnswer(String answer, String inflectionName, boolean translateToNative) {
        Expr e = null;
        if (translateToNative){
            try {
                e = foreignConcr.parse(category, getForeignInflectionFormByName(inflectionName)).iterator().next().getExpr();
            } catch (ParseError parseError) {
                parseError.printStackTrace();
            }
                return nativeConcr.linearize(e).equals(answer);
        }else{
            try {
                e = nativeConcr.parse(category, getNativeInflectionFormByName(inflectionName)).iterator().next().getExpr();
            } catch (ParseError parseError) {
                parseError.printStackTrace();
            }
                return foreignConcr.linearize(e).equals(answer);
        }
    }

    /**
     * Checks if the answer is a correct translation of the word.
     * @param answer A word in the foreign language
     * @return true if correct translation, false o/w
     */
    public boolean checkAnswer(String answer, boolean translateToNative){
        if(translateToNative)
            return answer.equals(getNative());
        else
            return answer.equals(getForeign());
    }

    /**
     * Returns a expression, i.e. abstract syntax tree, of this word
     * @return
     */
    public Expr getExpr(){
        return new Expr(function, new Expr[0]);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this)
            return true;
        if(obj instanceof Word){
            Word word = (Word)obj;
             return this.function.equals(word.function);
        }
        return false;
    }
}

package grammar;

import main.ResourceManager;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;

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
        nativeConcr = pgf.getLanguages().get(ResourceManager.codeToGF(nativeLangCode));
        foreignConcr = pgf.getLanguages().get(ResourceManager.codeToGF(foreignLangCode));
        this.function = function;
        this.category = category;
    }

    /**
     * Gives the word in the native language, inflected in the first listed form
     * @return the native word
     */
    public String getNative(){
        return getNativeInflectionFormByName(ResourceManager.getInflectionRealNamesByCat(category).get(0));
    }

    /**
     * Gives the word in the foreign language, inflected in the first listed form
     * @return the foreign word
     */
    public String getForeign(){
        return getForeignInflectionFormByName(ResourceManager.getInflectionRealNamesByCat(category).get(0));
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
        return ResourceManager.gfToName(nativeConcr.getName());
    }

    /**
     * Gives the name of the foreign language
     * @return The foreign language
     */
    public String getForeignLanguage(){
        return ResourceManager.gfToName(foreignConcr.getName());
    }

    /**
     * Returns a random inflection form name, but not from among the specified inflections
     * @param doNotIncllude Set of inflection forms that must not be returned
     * @return a random inflection name
     */
    public String getRandomInflectionName(Set<String> doNotIncllude){
        List<String> inflections = ResourceManager.getInflectionRealNamesByCat(category);
        if(doNotIncllude != null)
            for (Iterator<String> iterator = inflections.iterator(); iterator.hasNext(); )
                if(doNotIncllude.contains(iterator.next()))
                    iterator.remove();
        return inflections.get(new Random().nextInt(inflections.size()));
    }

    /**
     * Returns the word inflected in the specified form
     * @param inflectionName The form in which the word should be inflected
     * @return The word in the foreign language
     */
    public String getForeignInflectionFormByName(String inflectionName){
        Expr word = new Expr(function, new Expr[0]);
        Expr form = new Expr(inflectionName, new Expr[0]);
        return foreignConcr.linearize(new Expr(ResourceManager.getPartOfSpeechLinCatByName(category), word, form));
    }

    /**
    * Returns the word inflected in the specified form
    * @param inflectionName The form in which the word should be inflected
    * @return The word in the native language
    */
    public String getNativeInflectionFormByName(String inflectionName){
        Expr word = new Expr(function, new Expr[0]);
        Expr form = new Expr(inflectionName, new Expr[0]);
        return nativeConcr.linearize(new Expr(ResourceManager.getPartOfSpeechLinCatByName(category), word, form));
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
     * Also returns true if there is another inflection form, that would have the same form. E.g:
     * fish (sing) and fish (plur) will both be acceptable answers to fish.
     * @param answer A word in the foreign language
     * @param inflectionName The form that the answer is supposed to be inflected in.
     * @return true if correct translation, false o/w
     */
    public boolean checkAnswer(String answer, String inflectionName, boolean translateToNative) {
        //Check for correct translation among other inflections
        List<String> otherInflections = ResourceManager.getInflectionRealNamesByCat(category);
        otherInflections.remove(inflectionName);
        for(String inflection : otherInflections)
            if(getWordInflectionFormByName(inflection, !translateToNative)
                    .equals(getWordInflectionFormByName(inflectionName, !translateToNative)) &&
                getWordInflectionFormByName(inflection, translateToNative).equals(answer))
                    return true;

        //Check correct answer in specified inflection
        return (translateToNative ? getNativeInflectionFormByName(inflectionName)
                : getForeignInflectionFormByName(inflectionName)).equals(answer);
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

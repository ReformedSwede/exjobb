package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;

import java.util.*;

public class Word {

    private Concr nativeConcr;
    private Concr foreignConcr;
    private String function;
    private String category;

    public Word(Concr nativeConcr, Concr foreignConcr, String function, String category){
        this.nativeConcr = nativeConcr;
        this.foreignConcr = foreignConcr;
        this.function = function;
        this.category = category;
    }

    public Word(PGF pgf, String nativeLang, String foreitnLang, String function, String category){
        nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLang));
        foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreitnLang));
        this.function = function;
        this.category = category;
    }

    /**
     * Returns the word in its default native form
     */
    public String getNative(){
        return nativeConcr.linearize(new Expr(function, new Expr[0]));
    }

    /**
     * Returns the word in its default foreign form
     */
    public String getForeign(){
        return foreignConcr.linearize(new Expr(function, new Expr[0]));
    }

    /**
     *

    @Test
    public void getAllInflectionNames(){
        noun.getAllInflectionNames().forEach(System.out::println);
        verb.getAllInflectionNames().forEach(System.out::println);
    }
     * @return
     */
    public String getFunction(){
        return function;
    }

    /**
     *
     * @return
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @return
     */
    public String getNativeLanguage(){
        return Utils.gfToName(nativeConcr.getName());
    }

    /**
     *
     * @return
     */
    public String getForeignLanguage(){
        return Utils.gfToName(foreignConcr.getName());
    }

    /**
     * Returns the word, either native or foreign, depending on parameter
     */
    public String getWord(boolean getNative){
        return getNative ? getNative() : getForeign();
    }

    /**
     * Returns an array with the names of all inflection forms.
     */
    public List<String> getForeignInflectionNames(){
        return new ArrayList<>(foreignConcr.tabularLinearize(new Expr(function, new Expr[0])).keySet());
    }

    /**
     * Returns an array with the names of all inflection forms.
     */
    public List<String> getNativeInflectionNames(){
        return new ArrayList<>(nativeConcr.tabularLinearize(new Expr(function, new Expr[0])).keySet());
    }

    /**
     *
     * @param foreign If true returns all foreign inflections. Else, returns all native inflections
     * @return
     */
    public List<String> getInflectionNames(boolean foreign){
        if(foreign)
            return getForeignInflectionNames();
        else
            return getNativeInflectionNames();
    }

    /**
     * Returns a random inflection form name
     */
    public String getRandomForeignInflectionName(){
        List<String> inflections = getForeignInflectionNames();
        return inflections.get(new Random().nextInt(inflections.size()));
    }

    /**
     * Returns a random inflection form name
     */
    public String getRandomNativeInflectionName(){
        List<String> inflections = getNativeInflectionNames();
        return inflections.get(new Random().nextInt(inflections.size()));
    }

    /**
     * Returns a random inflection form name
     * @param foreign If true returns a foreign inflection. Else, returns native inflection
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

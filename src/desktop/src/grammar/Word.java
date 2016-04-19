package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;

import java.util.*;

public class Word {

    private Concr nativeConcr;
    private Concr foreignConcr;
    private String fun;

    public Word(Concr nativeConcr, Concr foreignConcr, String fun){
        this.nativeConcr = nativeConcr;
        this.foreignConcr = foreignConcr;
        this.fun = fun;
    }

    public Word(PGF pgf, String nativeLang, String foreitnLang, String fun){
        nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLang));
        foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreitnLang));
        this.fun = fun;
    }

    /**
     * Returns the word in its default native form
     */
    public String getNative(){
        return nativeConcr.linearize(new Expr(fun, new Expr[0]));
    }

    /**
     * Returns the word in its default foreign form
     */
    public String getForeign(){
        return foreignConcr.linearize(new Expr(fun, new Expr[0]));
    }

    public boolean hasInflections(){
        return getAllInflectionNames().size() > 1;
    }

    /**
     * Returns an array with the names of all inflection forms.
     */
    public List<String> getAllInflectionNames(){
        return new ArrayList<>(foreignConcr.tabularLinearize(new Expr(fun, new Expr[0])).keySet());
    }

    /**
     * Returns a random inflection form name
     */
    public String getRandomInflectionName(){
        return getAllInflectionNames().get(new Random().nextInt(getAllInflectionNames().size()));
    }

    /**
     * Returns the word inflected in the specified form
     * @param inflectionName The form in which the word should be inflected
     * @return The word in the foreign language
     */
    public String getForeignInflectionFormByName(String inflectionName){
        return foreignConcr.tabularLinearize(new Expr(fun, new Expr[0])).get(inflectionName);
    }

    /**
    * Returns the word inflected in the specified form
    * @param inflectionName The form in which the word should be inflected
    * @return The word in the native language
    */
    public String getNativeInflectionFormByName(String inflectionName){
        return nativeConcr.tabularLinearize(new Expr(fun, new Expr[0])).get(inflectionName);
    }

    /**
     * Checks if the answer is a correct translation of the word and in the correct inflection form.
     * @param answer A word in the foreign language
     * @param inflectionName The form that the answer is supposed to be inflected in.
     * @return true if correct translation, false o/w
     */
    public boolean checkInflectedAnswer(String answer, String inflectionName){
        return answer.equals(getNativeInflectionFormByName(inflectionName));
    }

    /**
     * Checks if the answer is a correct translation of the word.
     * @param answer A word in the foreign language
     * @return true if correct translation, false o/w
     */
    public boolean checkAnswer(String answer){
        return answer.equals(getNative());
    }

    public Expr getExpr(){
        return new Expr(fun, new Expr[0]);
    }
}

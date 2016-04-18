package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;

import java.util.Map;
import java.util.Random;
import java.util.Set;

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

    public String getNative(){
        return nativeConcr.linearize(new Expr(fun, new Expr[0]));
    }

    public String getForeign(){
        return foreignConcr.linearize(new Expr(fun, new Expr[0]));
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

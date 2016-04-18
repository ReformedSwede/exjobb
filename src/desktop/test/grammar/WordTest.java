package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class WordTest {

    Word word;

    @Before
    public void setUp(){
        try {
            PGF pgf = PGF.readPGF("Words.pgf");
            word = new Word(
                    pgf.getLanguages().get(Utils.codeToGF("swe")),
                    pgf.getLanguages().get(Utils.codeToGF("eng")),
                    "Car");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkAnswer() throws Exception {
        assertTrue(word.checkAnswer("bil"));
    }

}
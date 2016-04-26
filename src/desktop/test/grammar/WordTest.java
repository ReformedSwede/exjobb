package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class WordTest {

    Word noun;
    Word verb;

    @Before
    public void setUp(){
        try {
            PGF pgf = PGF.readPGF("Words.pgf");
            noun = new Word(
                    pgf.getLanguages().get(Utils.codeToGF("swe")),
                    pgf.getLanguages().get(Utils.codeToGF("eng")),
                    "Car", "Noun");
            verb = new Word(
                    pgf.getLanguages().get(Utils.codeToGF("swe")),
                    pgf.getLanguages().get(Utils.codeToGF("eng")),
                    "Eat","Noun");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkAnswer() throws Exception {
        assertTrue(noun.checkAnswer("bil", true));
    }

    @Test
    public void getAllInflectionNames(){
        noun.getAllInflectionNames().forEach(System.out::println);
        verb.getAllInflectionNames().forEach(System.out::println);
    }

    @Test
    public void checkInflectedAnswer(){
        assertTrue(verb.checkInflectedAnswer("åt", "s VPast", true));
    }

}
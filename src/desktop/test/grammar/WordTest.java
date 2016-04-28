package grammar;

import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

public class WordTest {

    private Word noun;
    private Word verb;

    @Before
    public void setUp(){
        try {
            PGF pgf = PGF.readPGF(System.getProperty("user.home") + "/grammar/sweeng/Words.pgf");
            noun = new Word(
                    pgf.getLanguages().get(Utils.codeToGF("swe")),
                    pgf.getLanguages().get(Utils.codeToGF("eng")),
                    "Car", "Noun");
            verb = new Word(
                    pgf.getLanguages().get(Utils.codeToGF("swe")),
                    pgf.getLanguages().get(Utils.codeToGF("eng")),
                    "Eat","Verb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkAnswer() throws Exception {
        assertTrue(noun.checkAnswer("bil", true));
    }

    @Test
    public void checkInflectedAnswer(){
        assertTrue(verb.checkInflectedAnswer("åt", "s VPast", true));
    }

    @Test
    public void compareInflections(){
        verb.getForeignInflectionNames().forEach(System.out::println);
        verb.getNativeInflectionNames().forEach(System.out::println);
    }
}
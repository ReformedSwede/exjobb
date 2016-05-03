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
}
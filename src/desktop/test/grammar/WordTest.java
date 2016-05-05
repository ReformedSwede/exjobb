package grammar;

import main.ResourceManager;
import org.grammaticalframework.pgf.PGF;
import org.junit.Before;

import java.io.FileNotFoundException;

public class WordTest {

    private Word noun;
    private Word verb;

    @Before
    public void setUp(){
        try {
            PGF pgf = PGF.readPGF("grammar/sweeng/Words.pgf");
            noun = new Word(
                    pgf.getLanguages().get(ResourceManager.codeToGF("swe")),
                    pgf.getLanguages().get(ResourceManager.codeToGF("eng")),
                    "Car", "Noun");
            verb = new Word(
                    pgf.getLanguages().get(ResourceManager.codeToGF("swe")),
                    pgf.getLanguages().get(ResourceManager.codeToGF("eng")),
                    "Eat","Verb");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
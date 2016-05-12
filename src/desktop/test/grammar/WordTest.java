package grammar;

import main.ResourceManager;
import org.grammaticalframework.pgf.PGF;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class WordTest{

    private List<Word> wordsToTest = new ArrayList<>();

    @Before
    public void setUp(){
        System.out.println("Setting up...");
        try {
            PGF pgf = PGF.readPGF("grammar/Main_sweeng/Words.pgf");
            for(String partOfSpeech : ResourceManager.getPartOfSpeechCats()){
                List<String> words = pgf.getFunctionsByCat(partOfSpeech);
                wordsToTest.add(
                        new Word(pgf, "swe", "eng", words.get(new Random().nextInt(words.size())), partOfSpeech));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void checkAllInflections(){
        for(Word word : wordsToTest) {
            for (String inflection : ResourceManager.getInflectionRealNamesByCat(word.getCategory())) {
                String nativeWord = word.getNativeInflectionFormByName(inflection);
                assertTrue(word.checkAnswer(nativeWord, inflection, true));
                System.out.println(inflection + " was translated correctly!");
            }
            System.out.println("All inflections of " + word.getCategory() + " were translated correctly!");
        }
    }
}
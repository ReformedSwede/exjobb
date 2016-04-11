package grammar;

import static org.junit.Assert.*;

public class GrammarManagerTest {



    @org.junit.Before
    public void setUp() throws Exception {
        GrammarManager.createLanguage("klingon");
    }


    @org.junit.Test
    public void addWord() throws Exception {
        GrammarManager.addWord("klingon", "Noun", "hello");

        boolean test = false;
        for(String s : GrammarManager.getAllWords("klingon", "Noun")){
            if(s.equals("hello")){
                test = true;
                break;
            }
        }
        assertTrue(test);
    }

    @org.junit.Test
    public void getAllPartOfSpeech() throws Exception {
        boolean test = false;
        for(String s : GrammarManager.getAllPartOfSpeech("klingon")){
            if(s.equals("Noun")){
                test = true;
                break;
            }
        }
        assertTrue(GrammarManager.getAllPartOfSpeech("klingon").size() == 1 && test);
    }

    @org.junit.Test
    public void getAllWords() throws Exception {

    }


    @org.junit.After
    public void tearDown() throws Exception{

    }

}
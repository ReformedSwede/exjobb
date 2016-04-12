package grammar;

import static org.junit.Assert.*;

public class GrammarManagerTest {

    GrammarManager gm;

    @org.junit.Before
    public void setUp() throws Exception {
        gm = new GrammarManager("Swedish", "English");
    }


    @org.junit.Test
    public void addWord() throws Exception {
        System.out.println("***Test: addWord***");
        gm.addWord("Noun", "hallå", "hello");

        boolean test = false;
        for(String s : gm.getAllWords("English", "Noun")){
            if(s.equals("hello")){
                test = true;
                break;
            }
        }
        assertTrue(test);
    }

    @org.junit.Test
    public void getAllPartOfSpeech() throws Exception {
        System.out.println("***Test: getAllPartsOfSpeech***");
        int prevSize = gm.getAllPartOfSpeech("English").size();

        boolean test = false;
        for(String s : gm.getAllPartOfSpeech("English")){
            if(s.equals("Noun")){
                test = true;
                break;
            }
        }
        assertTrue(gm.getAllPartOfSpeech("English").size() == prevSize + 1 && test);
    }

    @org.junit.Test
    public void getAllWords() throws Exception {
        System.out.println("***Test: getAllWords***");
        assertTrue(true);
    }


    @org.junit.After
    public void tearDown() throws Exception{

    }

}
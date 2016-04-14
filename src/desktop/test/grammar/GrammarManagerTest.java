package grammar;

import static org.junit.Assert.*;

public class GrammarManagerTest {

    private GrammarManager gm;

    @org.junit.Before
    public void setUp() throws Exception {
        gm = new GrammarManager("swe", "eng");
    }

    @org.junit.Test
    public void addWord() throws Exception {
        System.out.println("***Test: addWord***");
        gm.addWord("Noun", "hallå", "hello");

        boolean added = false;
        for(String s : gm.getAllWords("English", "Noun")){
            if(s.equals("hello")){
                added = true;
                break;
            }
        }
        assertTrue(added);
    }

    @org.junit.Test
    public void removeWord() throws  Exception{
        System.out.println("***Test: removeWord***");
        gm.removeWord("Noun", "hello");

        boolean removed = true;
        for(String s : gm.getAllWords("English", "Noun")){
            if(s.equals("hello")){
                removed = false;
                break;
            }
        }
        assertTrue(removed);

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

    @org.junit.Test
    public void gradeAnswer() throws Exception{
        System.out.println("***Test: gradeAnswer***");
        assertTrue(gm.gradeAnswer("fish", "fisk"));
    }


    @org.junit.After
    public void tearDown() throws Exception{

    }

}
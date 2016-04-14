package grammar;

import static org.junit.Assert.*;

public class GrammarManagerTest {

    private GrammarManager manager;

    @org.junit.Before
    public void setUp() throws Exception {
        manager = new GrammarManager("swe", "eng");
    }

    @org.junit.Test
    public void addWord() throws Exception {
        System.out.println("***Test: addWord***");
        manager.addWord("Noun", "hallå", "hello");

        boolean added = false;
        for(String s : manager.getAllWords("eng", "Noun")){
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
        manager.removeWord("Noun", "hello");

        boolean removed = true;
        for(String s : manager.getAllWords("eng", "Noun")){
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
        int prevSize = manager.getAllPartOfSpeech("eng").size();

        boolean test = false;
        for(String s : manager.getAllPartOfSpeech("eng")){
            if(s.equals("Noun")){
                test = true;
                break;
            }
        }
        assertTrue(manager.getAllPartOfSpeech("eng").size() == prevSize + 1 && test);
    }

    @org.junit.Test
    public void getAllWords() throws Exception {
        System.out.println("***Test: getAllWords***");
        assertTrue(true);
    }

    @org.junit.Test
    public void gradeAnswer() throws Exception{
        System.out.println("***Test: gradeAnswer***");
        assertTrue(manager.gradeAnswer("fish", "fisk"));
    }


    @org.junit.Test
    public void tmp() throws Exception{
        manager.getAllWords("swe", "Adjective").forEach(System.out::println);
    }

}
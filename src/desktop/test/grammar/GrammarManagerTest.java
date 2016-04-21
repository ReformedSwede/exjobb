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
        for(Word s : manager.getAllWords()){
            if(s.getForeign().equals("hello")){
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
        for(Word s : manager.getAllWords()){
            if(s.getForeign().equals("hello")){
                removed = false;
                break;
            }
        }
        assertTrue(removed);

    }

    @org.junit.Test
    public void getAllPartOfSpeech() throws Exception {
        System.out.println("***Test: getAllCategories***");
        int prevSize = manager.getAllCategories().size();

        boolean test = false;
        for(String s : manager.getAllCategories()){
            if(s.equals("Noun")){
                test = true;
                break;
            }
        }
        assertTrue(manager.getAllCategories().size() == prevSize + 1 && test);
    }

    @org.junit.Test
    public void getAllWords() throws Exception {
        System.out.println("***Test: getAllWords***");
        manager.getAllWords().forEach(word -> System.out.println(word.getForeign()));
        assertTrue(manager.getAllWords().size() > 0);
    }

    @org.junit.Test
    public void getAllWordsPos() throws Exception {
        System.out.println("***Test: getAllWords***");
        manager.getAllWords("V").forEach(word -> System.out.println(word.getForeign()));
        assertTrue(manager.getAllWords().size() > 0);
    }

    /*@org.junit.Test
    public void tmp() throws Exception{
        manager.tmp();
    }*/

}
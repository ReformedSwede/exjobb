package grammar;

import main.Session;
import org.junit.After;

import static org.junit.Assert.*;

public class GrammarManagerTest {

    private GrammarManager manager;

    @org.junit.Before
    public void setUp() throws Exception {
        GrammarManager.createSession("ATest", "Swedish", "English");
        manager = new GrammarManager(new Session("swe", "eng", "ATest"));
    }

    @org.junit.Test
    public void testAddAndRemoveWord() throws Exception {
        //ADD
        Word word = manager.addWord("Noun", "katt", "cat");

        boolean added = false;
        for(Word w : manager.getAllWords("Noun")){
            if(w.equals(word)){
                added = true;
                break;
            }
        }
        assertTrue(added && manager.getAllWords("Noun").size() == 1);

        //Remove
        manager.removeWord("Cat");

        boolean removed = true;
        for(Word s : manager.getAllWords("Noun")){
            if(s.getForeign().equals("hello")){
                removed = false;
                break;
            }
        }
        assertTrue(removed && manager.getAllWords("Noun").size() == 0);
    }

    @After
    public void breakDown(){
        manager.removeSession("ATest");
    }
}
package grammar;

import static org.junit.Assert.*;

public class PGFInterfaceTest {



    @org.junit.Before
    public void setUp() throws Exception {
        PGFInterface.createLanguage("klingon");
    }


    @org.junit.Test
    public void addWord() throws Exception {
        PGFInterface.addWord("klingon", "Noun", "hello");

        boolean test = false;
        for(String s : PGFInterface.getAllWords("klingon", "Noun")){
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
        for(String s : PGFInterface.getAllPartOfSpeech("klingon")){
            if(s.equals("Noun")){
                test = true;
                break;
            }
        }
        assertTrue(PGFInterface.getAllPartOfSpeech("klingon").size() == 1 && test);
    }

    @org.junit.Test
    public void getAllWords() throws Exception {

    }


    @org.junit.After
    public void tearDown() throws Exception{

    }

}
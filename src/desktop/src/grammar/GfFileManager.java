package grammar;

class GfFileManager {

    private String concreteGrammarFile;
    private GfFileEditor editor;

    GfFileManager(String file){
        concreteGrammarFile = file;
    }

    /**
     * Inserts a new word of type 'cat' into the grammar.
     * @param cat The type of the word (Keep first letter capitalized!)
     * @param word The word to enter (Keep all letters lowercase!)
     * @return True, if an insertion was made, False, o/w
     */
    boolean insertWord(String cat, String word){
        String fun = Character.toUpperCase(word.charAt(0)) + word.toLowerCase().substring(1);

        //abstract
        editor = new GfFileEditor("src/grammar/Words.gf");
        editor.insert(fun + " : " + cat, "fun");
        editor.saveToFile();

        //concrete
        editor = new GfFileEditor(concreteGrammarFile);
        editor.insert(fun + " = {s = \"" + word + "\"}", "lin");

        editor.saveToFile();
        return true;
    }

    void testPrint(){
        editor = new GfFileEditor("src/grammar/Words.gf");
        editor.printFile();
        editor = new GfFileEditor(concreteGrammarFile);
        editor.printFile();
    }
}

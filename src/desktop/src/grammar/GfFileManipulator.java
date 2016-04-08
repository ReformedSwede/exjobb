package grammar;

class GfFileManipulator {

    private String concreteGrammarFile;
    private FileManipulator fm;

    GfFileManipulator(String file){
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
        fm = new FileManipulator("src/grammar/Words.gf");
        fm.insert(fun + " : " + cat, "fun");
        fm.saveToFile();

        //concrete
        fm = new FileManipulator(concreteGrammarFile);
        fm.insert(fun + " = {s = \"" + word + "\"}", "lin");

        fm.saveToFile();
        return true;
    }

    void testPrint(){
        fm = new FileManipulator("src/grammar/Words.gf");
        fm.printFile();
        fm = new FileManipulator(concreteGrammarFile);
        fm.printFile();
    }
}

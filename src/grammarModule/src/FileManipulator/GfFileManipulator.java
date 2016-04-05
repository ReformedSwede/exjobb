
public class GfFileManipulator {

    private String[] files = {"Food.gf", "FoodEng.gf"}; //TODO: Replace with constructor

    /**
     * Inserts a new word of type 'cat' into the grammar.
     * @param cat The type of the word (Keep first letter capitalized!)
     * @param word The word to enter (Keep all letters lowercase!)
     * @return True, if an insertion was made, False, o/w
     */
    public boolean insertWord(String cat, String word){
        String fun = Character.toUpperCase(word.charAt(0)) + word.toLowerCase().substring(1);

        //abstract
        FileManipulator fm = new FileManipulator(files[0]);
        fm.insert(fun + " : " + cat, "fun");
        fm.saveToFile();

        //concrete
        fm = new FileManipulator(files[1]);
        fm.insert(fun + " = {s = \"" + word + "\"}", "lin");

        fm.saveToFile();
        return true;
    }

    public void test(){
        FileManipulator fm = new FileManipulator(files[0]);
        fm.printFile();
        fm = new FileManipulator(files[1]);
        fm.printFile();
    }
}

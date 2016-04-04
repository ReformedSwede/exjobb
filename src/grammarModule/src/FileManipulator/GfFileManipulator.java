
public class GfFileManipulator {

    private String[] files = {"Food.gf", "FoodEng.gf"}; //TODO: Replace with constructor

    /**
     * Inserts a new word of type 'cat' into the grammar.
     * @param cat The type of the word
     * @param word The word to enter
     * @return True, if an insertion was made, False, o/w
     */
    public boolean insertLin(String cat, String word){
        String fun = Character.toUpperCase(word.charAt(0)) + word.toLowerCase().substring(1);

        FileManipulator fm = new FileManipulator(files[1]);
        fm.printFile();

        /*FileManipulator fm = new FileManipulator(files[1]);
        if (fm.findPlace(" : ?(" + cat + ") ?;", false)) {
            fm.insert(", " + fun, false);

            fm = new FileManipulator(files[0]);
            if (fm.findPlace(";\\s*(}|param)", false))
                fm.insert("; \n" + fun + " = {s = \"" + word + "\"}", false);
        }else {
            System.out.println("Couldn't find " + cat);
            return false;
        }*/
        return true;
    }

    public void test(){
        FileManipulator fm = new FileManipulator(files[0]);
        fm.printFile();
    }
}

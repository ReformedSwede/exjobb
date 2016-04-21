package grammar;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import javafx.util.Pair;
import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;

public class GrammarManager {

	private String dir;
    private PGF pgf;
	private String nativeLangCode;
	private String foreignLangCode;
    private Concr nativeConcr;
    private Concr foreignConcr;

	/**
	 * Initializes the grammar manager for this session. Finds the session's folder and compiles necessary files.
	 * @param nativeLang The language native to the user. Preferably a three letter abbreviation.
	 * @param foreignLang The language foreign to the user. Preferably a three letter abbreviation.
     */
	public GrammarManager(String nativeLang, String foreignLang){
		//TODO: Insert checks for directory access. For now, assume directories exist
		dir = System.getProperty("user.home") + File.separator + "grammar" +
				File.separator + nativeLang.substring(0, 3).toLowerCase() + foreignLang.substring(0, 3).toLowerCase();
		this.nativeLangCode = nativeLang.substring(0, 3).toLowerCase();
		this.foreignLangCode = foreignLang.substring(0, 3).toLowerCase();
        try {
            pgf = PGF.readPGF("Words.pgf");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLang));
        foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreignLang));

        //Create PGF file TODO: compile correct file in correct directory with correct filename
		/*String file = dir + File.separator + Utils.codeToGF(foreignLang) + ".gf";
		if(!new File(file).exists())*/
		compilePGF(true, dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf",
				dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf");
	}

	/**
	 * Lists all sessions.
	 * @return A list of all sessions. Each element is a pair on the form (nativeLangCode, foreignLangCode)
     */
	public static List<Pair<String, String>> getSessions(){
		ArrayList<Pair<String, String>> sessions = new ArrayList<>();
		String path = System.getProperty("user.home") + File.separator + "grammar";
		for(String folder : new File(path).list())
			sessions.add(new Pair<>(folder.substring(0, 3), folder.substring(3, 6)));
		return sessions;
	}

	/**
	 * Inserts a new word into the file system.
	 * @param category The type of the word, gf category (Keep first letter capitalized!)
	 * @param nativeWord The new word in the user's native language
	 * @param foreignWord The new word in the foreign language
	 */
	public void addWord(String category, String nativeWord, String foreignWord){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";
		String fun = Character.toUpperCase(foreignWord.charAt(0)) + foreignWord.toLowerCase().substring(1);

		//Insert into abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
		editor.insert(fun + " : " + category, "fun");
		editor.saveToFile();

		//Insert into native concrete
		editor = new GfFileEditor(nativeConcreteFile);
		editor.insert(fun + " = mk" + category.charAt(0) + " \"" + nativeWord + "\"", "lin");
		editor.saveToFile();

		//Insert into foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
		editor.insert(fun + " = mk" + category.charAt(0) + " \"" + foreignWord + "\"", "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, nativeConcreteFile, foreignConcreteFile);
	}

	/**
	 * Removes a word from the file system.
	 * @param category The type of the word (Keep first letter capitalized!)
	 * @param foreignWord The word in the foreign language
	 */
	public void removeWord(String category, String foreignWord){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";
		String fun = Character.toUpperCase(foreignWord.charAt(0)) + foreignWord.toLowerCase().substring(1);

		//Remove from abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
		editor.delete(fun, "fun");
		editor.saveToFile();

		//Remove from native concrete
		editor = new GfFileEditor(nativeConcreteFile);
		editor.delete(fun, "lin");
		editor.saveToFile();

		//Remove from foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
		editor.delete(fun, "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, nativeConcreteFile, foreignConcreteFile);
	}

	/**
	 * Returns a list of all languages in the pgf file
	 * @return List of all languages
     */
	public Collection<String> getAllLanguages(){
        Collection<String> langs = pgf.getLanguages().keySet();
        langs.forEach((s -> {
            s = Utils.gfToName(s);
        }));
        return langs;
	}

	/**
	 * Return a list of all categories
     */
	public List<String> getAllCategories(){
        List<String> cats;
        cats = pgf.getCategories().stream().filter((category) ->
                !category.equals("Int")&&!category.equals("String")&&!category.equals("Float"))
                .collect(Collectors.toList());
        return cats;
	}

	/**
	 * Returns a list of all words in the file system
     */
    public List<Word> getAllWords(){
        List<Word> list = new ArrayList<>();
        getAllCategories().forEach(cat -> {
            list.addAll(getAllWords(cat));
        });
        return list;
    }

	/**
	 * Returns a list of all words of the specified category
     */
	public List<Word> getAllWords(String category){
        List<String> functions = pgf.getFunctionsByCat(category);
        return functions.stream().map(fun -> new Word(nativeConcr, foreignConcr, fun)).collect(Collectors.toList());
	}

	/*public void tmp(){
		Word word = new Word(nativeConcr, foreignConcr, "Go");
        nativeConcr.tabularLinearize(word.getExpr()).forEach((key, value) -> {System.out.println(key + value);});
	}*/

	/*****Private methods******/

	/**
	 * Compiles .gf files to .pgf files
	 * @param files The files to compile
     */
	private void compilePGF(boolean compileLocally, String... files) {
        if (compileLocally){
            try {
                String[] args = new String[files.length + 2];
                args[0] = "gf";
                args[1] = "-make";
                System.arraycopy(files, 0, args, 2, files.length);
                Process builder = new ProcessBuilder(args).start();
                printCompileLog(builder);

                pgf = PGF.readPGF("Words.pgf");
                nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLangCode));
                foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreignLangCode));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            GfCloudProxy proxy = new GfCloudProxy();
            //remove old files

            //upload new files

            //compile

            //download pgf
        }
	}

	/**
	 * Prints all output from the specified process
	 * @throws IOException
     */
	private void printCompileLog(Process pro) throws IOException{
		BufferedReader reader =	new BufferedReader(new InputStreamReader(pro.getInputStream()));
		StringBuilder builder = new StringBuilder();
		String line;
		while ( (line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		System.out.println(builder.toString());
	}
}

package grammar;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import javafx.util.Pair;
import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;

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
            pgf = PGF.readPGF(dir + File.separator + "Words.pgf");
            nativeConcr = pgf.getLanguages().get(Utils.codeToGF(nativeLang));
            foreignConcr = pgf.getLanguages().get(Utils.codeToGF(foreignLang));
        } catch (FileNotFoundException e) {
            compilePGF(true, new File(dir), dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf",
                    dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf");
        }

        compilePGF(true, new File(dir), dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf",
              dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf");
	}

	/**
	 * Lists all sessions.
	 * @return A list of all sessions. Each element is a pair on the form (nativeLangCode, foreignLangCode)
     */
	public static List<Pair<String, String>> getSessions(){
		ArrayList<Pair<String, String>> sessions = new ArrayList<>();
		String path = System.getProperty("user.home") + File.separator + "grammar";
		for(File folder : new File(path).listFiles())
			if(folder.isDirectory())
				sessions.add(new Pair<>(folder.getName().substring(0, 3), folder.getName().substring(3, 6)));
		return sessions;
	}

	/**
	 * Creates a new session by making a new directory and initialize files there
	 * @param nativeLang The native language of the session
	 * @param foreignLang The foreign language of the session
     */
	public static void createSession(String nativeLang, String foreignLang){
		//Create directory
		File folder = new File(System.getProperty("user.home") + File.separator + "grammar" + File.separator +
				nativeLang.substring(0, 3).toLowerCase() + foreignLang.substring(0, 3).toLowerCase());
		folder.mkdir();

        String abstractFile = folder.getAbsolutePath() + File.separator + "Words.gf";
        String nativeConcreteFile = folder.getAbsolutePath() + File.separator + Utils.nameToGf(nativeLang) + ".gf";
        String foreignConcreteFile = folder.getAbsolutePath() + File.separator + Utils.nameToGf(foreignLang) + ".gf";

        GfFileEditor.initAbstractFile(abstractFile);
        GfFileEditor.initConcreteFile(nativeConcreteFile, nativeLang);
        GfFileEditor.initConcreteFile(foreignConcreteFile, foreignLang);
	}

	/**
	 * Inserts a new word into the file system.
	 * @param category The type of the word, gf category (Keep first letter capitalized!)
	 * @param nativeWord The new word in the user's native language
	 * @param foreignWord The new word in the foreign language
     * @return True if an insertion was made, false o/w
	 */
	public boolean addWord(String category, String nativeWord, String foreignWord){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";
		String fun = Character.toUpperCase(foreignWord.charAt(0)) + foreignWord.toLowerCase().substring(1);
        fun = convertToASCII(fun);

		//Insert into abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
        //Check for duplicate entry
        String duplicate;
        if((duplicate = editor.isDuplicateInsertion(fun, "fun")) != null){
            if(duplicate.endsWith(category))
                return false;
            fun = fun + category.substring(0, 1).toUpperCase();
        }
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
		compilePGF(true, new File(dir), nativeConcreteFile, foreignConcreteFile);
        return true;
	}

	/**
	 * Inserts a new word into the file system.
	 * @param category The type of the word, gf category (Keep first letter capitalized!)
	 * @param nativeWords All inflection forms of the new word in the user's native language
	 * @param foreignWords All inflection forms of the new word in the foreign language
     * @return True if an insertion was made, false o/w
	 */
	public boolean addWordWithInflections(String category, List<String> nativeWords, List<String> foreignWords){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";
		String fun = Character.toUpperCase(foreignWords.get(0).charAt(0)) + foreignWords.get(0).toLowerCase().substring(1);
        fun = convertToASCII(fun);

		//Insert into abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
        //Check for duplicate entry
        String duplicate;
        if((duplicate = editor.isDuplicateInsertion(fun, "fun")) != null){
            if(duplicate.endsWith(category))
                return false;
            fun = fun + category.substring(0, 1).toUpperCase();
        }
		editor.insert(fun + " : " + category, "fun");
		editor.saveToFile();

		//Insert into native concrete
		editor = new GfFileEditor(nativeConcreteFile);
        StringBuilder stringToInsert = new StringBuilder(fun + " = mk" + category.charAt(0)); //e.g. "mkV" for verbs
        for(String word : nativeWords)
            stringToInsert.append(" \"").append(word).append("\"");
		editor.insert(stringToInsert.toString(), "lin");
		editor.saveToFile();

		//Insert into foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
        stringToInsert = new StringBuilder(fun + " = mk" + category.charAt(0));
        for(String word : foreignWords)
            stringToInsert.append(" \"").append(word).append("\"");
		editor.insert(stringToInsert.toString(), "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, new File(dir), nativeConcreteFile, foreignConcreteFile);
        return true;
	}

	/**
	 * Removes a word from the file system.
	 * @param category The type of the word (Keep first letter capitalized!)
	 * @param function The word in the foreign language
	 */
	public void removeWord(String category, String function){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";

		//Remove from abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
		editor.delete(function, "fun");
		editor.saveToFile();

		//Remove from native concrete
		editor = new GfFileEditor(nativeConcreteFile);
		editor.delete(function, "lin");
		editor.saveToFile();

		//Remove from foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
		editor.delete(function, "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, new File(dir), nativeConcreteFile, foreignConcreteFile);
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
                !category.equals("Int") && !category.equals("String") && !category.equals("Float"))
                .collect(Collectors.toList());
        return cats;
	}

	/**
	 * Returns a list of all words in the file system
     */
    public List<Word> getAllWords(){
        List<Word> list = new ArrayList<>();
        getAllCategories().forEach(cat -> list.addAll(getAllWords(cat)));
        return list;
    }

	/**
	 * Returns a list of all words of the specified category
     */
	public List<Word> getAllWords(String category){
        List<String> functions = pgf.getFunctionsByCat(category);
        return functions.stream().map(fun -> new Word(nativeConcr, foreignConcr, fun, category)).collect(Collectors.toList());
	}

    public String translate(String word, String startcat, boolean fromForeign){
        Expr e = null;
        if(fromForeign){
            try {
                e = foreignConcr.parse(startcat, word).iterator().next().getExpr();
            } catch (ParseError parseError) {
                parseError.printStackTrace();
            }
            return nativeConcr.linearize(e);
        }else{
            try {
                e = nativeConcr.parse(startcat, word).iterator().next().getExpr();
            } catch (ParseError parseError) {
                parseError.printStackTrace();
            }
            return foreignConcr.linearize(e);
        }
    }

	public void tmp(){
		/*List<Word> all = getAllWords("Noun");
		Word w = getAllWords("Noun").get(new Random().nextInt(all.size()));
		String form = w.getForeignInflectionFormByName(w.getRandomForeignInflectionName());


		System.out.println(pgf.getStartCat() + form);

		try {
			Expr e = foreignConcr.parse(pgf.getStartCat(), form).iterator().next().getExpr();
			System.out.println(nativeConcr.linearize(e));
		} catch (ParseError parseError) {
			parseError.printStackTrace();
		}
*/
		List<String> l = pgf.getFunctionsByCat("Noun");
		String word = l.get(new Random().nextInt(l.size()));
		Concr eng = pgf.getLanguages().get("WordsEng");
		eng.tabularLinearize(new Expr("Wine", new Expr[0])).forEach((s, s2) -> {
			try {
				eng.parse("Noun", s2);
			} catch (ParseError parseError) {
				parseError.printStackTrace();
			}
		});
	}

	/*****Private methods******/

    /**
     * If s if not purley ascii, it is converted to a pseudo-random ascii string. o/w s is returned
     * @param s
     */
    private String convertToASCII(String s){
        if(!s.matches("\\A\\p{ASCII}*\\z")) {
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++)
                chars[i] = (char) (((int) chars[i] % (65 - 90)) + 65);
            return new String(chars);
        }else
            return s;
    }

	/**
	 * Compiles .gf files to .pgf files, then updates the pgf and Concr objects.
     * @param compileLocally If false, compilation will be done using gf cloud
     * @param compilePath Specifies where the finished pgf file should be put
	 * @param files The files to compile
     */
	private void compilePGF(boolean compileLocally, File compilePath, String... files) {
        if (compileLocally){
            try {
                String[] args = new String[files.length + 2];
                args[0] = "gf";
                args[1] = "-make";
                System.arraycopy(files, 0, args, 2, files.length);
                Process builder = new ProcessBuilder(args).start();
                printCompileLog(builder);

                //Move pgf file
                File file = new File("Words.pgf");
                if(file.exists() &&
                        !file.renameTo(new File(compilePath.getAbsolutePath() + File.separator + "Words.pgf")))
                    throw new IOException("Could not move file Word.pgf to " + compilePath.getAbsolutePath());


                //Reinitialize variables
                pgf = PGF.readPGF(compilePath.getAbsolutePath() + File.separator + "Words.pgf");
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
		BufferedReader reader =	new BufferedReader(new InputStreamReader(pro.getErrorStream()));
		StringBuilder builder = new StringBuilder();
		String line;
		while ( (line = reader.readLine()) != null) {
			builder.append(line);
			builder.append(System.getProperty("line.separator"));
		}
		System.out.println(builder.toString());

        reader =	new BufferedReader(new InputStreamReader(pro.getErrorStream()));
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        System.out.println(builder.toString());
	}
}

package grammar;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import main.ResourceManager;
import main.Session;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;

/**
 * Class used for manipulating grammar using gf files.
 * Contains methods for reading, inserting and removing data from gf files.
 */
public class GrammarManager {

	private String dir;
    private PGF pgf;
	private String nativeLangCode;
	private String foreignLangCode;
    private Concr nativeConcr;
    private Concr foreignConcr;

	/**
	 * Initializes the grammar manager for this session. Finds the session's folder and compiles necessary files.
	 * @param session A session object containing names of languages and title
     */
	public GrammarManager(Session session){
		nativeLangCode = session.getNativeCode();
		foreignLangCode = session.getForeignCode();

		//Create grammar dir if non-existent
		new File("grammar" + File.separator).mkdir();

		//Check if session directory exists. If not, create
		dir = "grammar" + File.separator + session.getTitle() + "_" + nativeLangCode + foreignLangCode;
		if(!new File(dir).exists())
			createSession(session.getTitle(), nativeLangCode, foreignLangCode);

		//Try to read PGF. If it's not found, compile a new one
        try {
            pgf = PGF.readPGF(dir + File.separator + "Words.pgf");
            nativeConcr = pgf.getLanguages().get(ResourceManager.codeToGF(nativeLangCode));
            foreignConcr = pgf.getLanguages().get(ResourceManager.codeToGF(foreignLangCode));
        } catch (FileNotFoundException e) {
            compilePGF(true, new File(dir), dir + File.separator + ResourceManager.codeToGF(nativeLangCode) + ".gf",
                    dir + File.separator + ResourceManager.codeToGF(foreignLangCode) + ".gf");
        }

		//Compile new pgf to make sure we are using fresh objects (I guess this line could be removed)
        compilePGF(true, new File(dir), dir + File.separator + ResourceManager.codeToGF(nativeLangCode) + ".gf",
              dir + File.separator + ResourceManager.codeToGF(foreignLangCode) + ".gf");
	}

	/**
	 * Lists all sessions.
	 * @return A list of all sessions. Each element is a pair on the form (nativeLangCode, foreignLangCode)
     */
	public static List<Session> getSessions(){
		List<Session> sessions = new ArrayList<>();
		String path = "grammar";
		new File(path).mkdir();

		for(File folder : new File(path).listFiles())
			if(folder.isDirectory()) {
				String[] folderName = folder.getName().split("_");
				sessions.add(new Session(folderName[1].substring(0, 3), folderName[1].substring(3, 6),folderName[0]));
			}
		return sessions;
	}

	/**
	 * Creates a new session by making a new directory and initialize files there
	 * @param nativeLang The name of the native language of the session
	 * @param foreignLang The name of the foreign language of the session
     */
	public static void createSession(String name, String nativeLang, String foreignLang){
		//Check if name already exists
		for(File folder : new File("grammar").listFiles())
			if(folder.isDirectory() && folder.getName().startsWith(name))
				return;

		//Create directory
		File folder = new File("grammar" + File.separator + name + "_" +
				nativeLang.substring(0, 3).toLowerCase() + foreignLang.substring(0, 3).toLowerCase());
		folder.mkdir();

		//Create file paths
        String abstractFile = folder.getAbsolutePath() + File.separator + "Words.gf";
        String nativeConcreteFile = folder.getAbsolutePath() + File.separator
				+ ResourceManager.nameToGf(nativeLang) + ".gf";
        String foreignConcreteFile = folder.getAbsolutePath() + File.separator
				+ ResourceManager.nameToGf(foreignLang) + ".gf";

		//Fill files which content
        GfFileEditor.initAbstractFile(abstractFile);
        GfFileEditor.initConcreteFile(nativeConcreteFile, nativeLang);
        GfFileEditor.initConcreteFile(foreignConcreteFile, foreignLang);
	}

	/**
	 * Removes a session folder and all its files.
	 * @param sessionName The title of the session
     */
	public static void removeSession(String sessionName){
		File sessionFolder = null;

		//Find the folder that contains the title in the name
		for(File folder : new File("grammar").listFiles())
			if(folder.isDirectory() && folder.getName().startsWith(sessionName)) {
				sessionFolder = folder;
				break;
			}

		//Remove all files in folder
		File[] files = sessionFolder.listFiles();
		for(File f : files)
			f.delete();

		//Remove the folder itself
		sessionFolder.delete();
	}

	/**
	 * Inserts a new word into the file system.
	 * @param category The type of the word, gf category (Keep first letter capitalized!)
	 * @param nativeWord The new word in the user's native language
	 * @param foreignWord The new word in the foreign language
     * @return The just inserted word, as a Word object, or null if it already exists.
	 */
	public Word addWord(String category, String nativeWord, String foreignWord){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + ResourceManager.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + ResourceManager.codeToGF(foreignLangCode) + ".gf";

		GfFileEditor editor = new GfFileEditor(abstractFile);
		String fun = "fun" + editor.getNextFunctionNumber();

		//Insert into abstract
		editor.insert(fun + " : " + category, "fun");
		editor.saveToFile();

		//Insert into native concrete
		editor = new GfFileEditor(nativeConcreteFile);
		editor.insert(fun + " = " + ResourceManager.getOperationByCatName(category) + " \"" + nativeWord + "\"", "lin");
		editor.saveToFile();

		//Insert into foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
		editor.insert(fun + " = " + ResourceManager.getOperationByCatName(category) + " \"" + foreignWord + "\"", "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, new File(dir), nativeConcreteFile, foreignConcreteFile);
        return new Word(nativeConcr, foreignConcr, fun, category);
	}

	/**
	 * Inserts a new word with several inflections into the file system.
	 * @param category The type of the word, gf category (Keep first letter capitalized!)
	 * @param nativeWords All inflection forms of the new word in the user's native language
	 * @param foreignWords All inflection forms of the new word in the foreign language
     * @return The just inserted word, as a Word object, or null if it already exists.
	 */
	public Word addWordWithInflections(String category, List<String> nativeWords, List<String> foreignWords){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + ResourceManager.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + ResourceManager.codeToGF(foreignLangCode) + ".gf";

		GfFileEditor editor = new GfFileEditor(abstractFile);
		String fun = "fun" + editor.getNextFunctionNumber();

        //Insert into abstract
		editor.insert(fun + " : " + category, "fun");
		editor.saveToFile();

		//Insert into native concrete
		editor = new GfFileEditor(nativeConcreteFile);
        StringBuilder stringToInsert = new StringBuilder(fun + " = " + ResourceManager.getOperationByCatName(category));
        for(String word : nativeWords)
            stringToInsert.append(" \"").append(word).append("\"");
		editor.insert(stringToInsert.toString(), "lin");
		editor.saveToFile();

		//Insert into foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
        stringToInsert = new StringBuilder(fun + " = " + ResourceManager.getOperationByCatName(category));
        for(String word : foreignWords)
            stringToInsert.append(" \"").append(word).append("\"");
		editor.insert(stringToInsert.toString(), "lin");
		editor.saveToFile();

		//Compile
		compilePGF(true, new File(dir), nativeConcreteFile, foreignConcreteFile);
		return new Word(nativeConcr, foreignConcr, fun, category);
	}

	/**
	 * Removes a word from the file system.
	 * @param function The word in the foreign language
	 */
	public void removeWord(String function){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + ResourceManager.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + ResourceManager.codeToGF(foreignLangCode) + ".gf";

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
	 * Gives a list of all categories in the pgf file
	 * @return A list of all categories
     */
	 List<String> getAllCategories(){
        List<String> cats;
        cats = pgf.getCategories().stream().filter((category) ->
                !category.equals("Int") && !category.equals("String") && !category.equals("Float"))
                .collect(Collectors.toList());
        return cats;
	}

	/**
	 * @return A list of all words of the specified category
     */
	public List<Word> getAllWords(String category){
        List<String> functions = pgf.getFunctionsByCat(category);
        return functions.stream()
				.map(fun -> new Word(nativeConcr, foreignConcr, fun, category))
				.collect(Collectors.toList());
	}

	/*****Private methods******/

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
                nativeConcr = pgf.getLanguages().get(ResourceManager.codeToGF(nativeLangCode));
                foreignConcr = pgf.getLanguages().get(ResourceManager.codeToGF(foreignLangCode));
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

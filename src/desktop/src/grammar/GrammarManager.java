package grammar;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import main.Utils;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.Expr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;

public class GrammarManager {

	private String dir;
	private String nativeLangCode;
	private String foreignLangCode;

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

		//Create PGF file TODO: compile correct file in correct directory with correct filename
		/*String file = dir + File.separator + Utils.codeToGF(foreignLang) + ".gf";
		if(!new File(file).exists())*/
		//compilePGF(dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf",
		//		dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf");
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
	 * @param partOfSpeech The type of the word (Keep first letter capitalized!)
	 * @param nativeWord The new word in the user's native language
	 * @param foreignWord The new word in the foreign language
	 */
	public void addWord(String partOfSpeech, String nativeWord, String foreignWord){
		//Define file paths and names
		String abstractFile = dir + File.separator + "Words.gf";
		String nativeConcreteFile = dir + File.separator + Utils.codeToGF(nativeLangCode) + ".gf";
		String foreignConcreteFile = dir + File.separator + Utils.codeToGF(foreignLangCode) + ".gf";
		String fun = Character.toUpperCase(foreignWord.charAt(0)) + foreignWord.toLowerCase().substring(1);

		//Insert into abstract
		GfFileEditor editor = new GfFileEditor(abstractFile);
		editor.insert(fun + " : " + partOfSpeech, "fun");
		editor.saveToFile();

		//Insert into native concrete
		editor = new GfFileEditor(nativeConcreteFile);
		editor.insert(fun + " = {s = \"" + nativeWord + "\"}", "lin");
		editor.saveToFile();

		//Insert into foreign concrete
		editor = new GfFileEditor(foreignConcreteFile);
		editor.insert(fun + " = {s = \"" + foreignWord + "\"}", "lin");
		editor.saveToFile();

		//Compile
		compilePGF(nativeConcreteFile, foreignConcreteFile);
	}

	public void removeWord(String partOfSpeech, String foreignWord){
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
		compilePGF(nativeConcreteFile, foreignConcreteFile);
	}

	/**
	 * Returns a list of all languages in the pgf file
	 * @return List of all languages
     */
	public Collection<String> getAllLanguages(){
		try {
			Collection<String> langs = PGF.readPGF("Words.pgf").getLanguages().keySet();
			langs.forEach((s -> {
				s = Utils.gfToName(s);
			}));
			return langs;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	/**
	 * Return a list of all languages, i.e. GF categories
     */
	public List<String> getAllPartOfSpeech(String language){
		try {
			List<String> parts;
			parts = PGF.readPGF("Words.pgf").getCategories().stream().filter((category) ->
					!category.equals("Int")&&!category.equals("String")&&!category.equals("Float"))
					.collect(Collectors.toList());
			return parts;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}
	
	public List<String> getAllWords(String language, String partOfSpeech){
		try {
			/*Concr cf = PGF.readPGF("Words.pgf").getLanguages().get(Utils.codeToGF(language));
			List<String> functions = PGF.readPGF("Words.pgf").getFunctionsByCat(partOfSpeech);
			List<Expr> expressions = functions.stream().map(Expr::new).collect(Collectors.toList());
			return expressions.stream().map((cf::linearize)).collect(Collectors.toList());*/
			return PGF.readPGF("Words.pgf").getFunctionsByCat(partOfSpeech);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}

	/**
	 * Returns a random word from the grammar files
	 * Each string is actually the name of a gf function
     */
	public String getRandomWord(String language){
		int nrOfCats = getAllPartOfSpeech(Utils.codeToGF(language)).size();
		String randPartOfSpeech = getAllPartOfSpeech(Utils.codeToGF(language))
				.get(new Random().nextInt(nrOfCats));
		List<String> allWords = getAllWords(language, randPartOfSpeech);
		return allWords.get(new Random().nextInt(allWords.size()));
	}

	/**
	 * Returns a random word from a specified part of speech.
	 * Each string is actually the name of a gf function
     */
	public String getRandomWord(String language, String partOfSpeech){
		int nrOfWords = getAllWords(Utils.codeToGF(language), partOfSpeech).size();
		return getAllWords(Utils.codeToGF(language), partOfSpeech).get(new Random().nextInt(nrOfWords));
	}

	public boolean gradeAnswer(String foreignWord, String usersAnswer){
		try {
			Concr cn = PGF.readPGF("Words.pgf").getLanguages().get(Utils.codeToGF(nativeLangCode));
			Concr cf = PGF.readPGF("Words.pgf").getLanguages().get(Utils.codeToGF(foreignLangCode));

			Expr expr = cn.parse("Noun", usersAnswer).iterator().next().getExpr();
			String linearization = cf.linearize(expr);
			return linearization.equals(foreignWord);
		} catch(ParseError e){
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/*ublic void tmp(){
		try {
			Concr cn = PGF.readPGF("Words.pgf").getLanguages().get(Utils.codeToGF(nativeLangCode));
			Concr cf = PGF.readPGF("Words.pgf").getLanguages().get(Utils.codeToGF(foreignLangCode));

			Expr e = new Expr(getRandomWord("eng", "V"));
			cf.tabularLinearize(e).entrySet().forEach((entry) -> {
				System.out.println("key: " + entry.getKey() + " value: " + entry.getValue());
			});

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}*/

	/*****Private methods******/

	/**
	 * Compiles .gf files to .pgf files
	 * @param files The files to compile
     */
	private void compilePGF(String... files){
		try {
			for (String file : files) {
				String[] args = new String[files.length + 2];
				args[0] = "gf";
				args[1] = "-make";
				System.arraycopy(files, 0, args, 2, files.length);
				Process builder = new ProcessBuilder(args).start();
				printCompileLog(builder);
			}
		}catch(IOException e){
			e.printStackTrace();
		}

	}

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

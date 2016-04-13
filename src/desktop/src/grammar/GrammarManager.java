package grammar;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javafx.util.Pair;
import org.grammaticalframework.pgf.Concr;
import org.grammaticalframework.pgf.PGF;
import org.grammaticalframework.pgf.ParseError;

public class GrammarManager {

	private String dir;
	private String nativeLang;
	private String foreignLang;

	public GrammarManager(String nativeLang, String foreignLang){
		//TODO: Insert checks for directory access. For now, assume directories exist
		dir = System.getProperty("user.home") + File.separator + "grammar" +
				File.separator + nativeLang.substring(0, 3).toLowerCase() + foreignLang.substring(0, 3).toLowerCase();
		this.nativeLang = nativeLang;
		this.foreignLang = foreignLang;

		//Create PGF file TODO: compile to correct directory with correct filename
		String file = dir + File.separator +
				"Words" + foreignLang.substring(0, 1).toUpperCase() + foreignLang.substring(1, 3) + ".gf";
		if(!new File(file).exists()) {
			try {
				Process pro = new ProcessBuilder("gf", "-make", file).start();
				printCompileLog(pro);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Lists all sessions.
	 * @return A list of all sessions. Each element is a pair on the form (nativeLang, foreignLang)
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
		try{
			//Define file paths and names
			String abstractFile = dir + File.separator + "Words.gf";
			String nativeConcreteFile = dir + File.separator + "Words" +
					nativeLang.substring(0, 1).toUpperCase() + nativeLang.substring(1, 3) + ".gf";
			String foreignConcreteFile = dir + File.separator + "Words" +
					foreignLang.substring(0, 1).toUpperCase() + foreignLang.substring(1, 3) + ".gf";
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
			Process pro = new ProcessBuilder("gf", "-make", nativeConcreteFile).start();
			printCompileLog(pro);
			pro = new ProcessBuilder("gf", "-make", foreignConcreteFile).start();
			printCompileLog(pro);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void removeWord(String partOfSpeech, String foreignWord){
		try{
			//Define file paths and names
			String abstractFile = dir + File.separator + "Words.gf";
			String nativeConcreteFile = dir + File.separator + "Words" +
					nativeLang.substring(0, 1).toUpperCase() + nativeLang.substring(1, 3) + ".gf";
			String foreignConcreteFile = dir + File.separator + "Words" +
					foreignLang.substring(0, 1).toUpperCase() + foreignLang.substring(1, 3) + ".gf";
			String fun = Character.toUpperCase(foreignWord.charAt(0)) + foreignWord.toLowerCase().substring(1);

			//Remove from abstract
			GfFileEditor editor = new GfFileEditor(abstractFile);
			editor.delete(fun, "fun");

			//Remove from native concrete
			editor = new GfFileEditor(nativeConcreteFile);
			editor.delete(fun, "lin");

			//Remove from foreign concrete
			editor = new GfFileEditor(foreignConcreteFile);
			editor.delete(fun, "lin");

			//Compile
			Process pro = new ProcessBuilder("gf", "-make", nativeConcreteFile).start();
			printCompileLog(pro);
			pro = new ProcessBuilder("gf", "-make", foreignConcreteFile).start();
			printCompileLog(pro);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of all languages in the pgf file
	 * @return List of all languages
     */
	public Collection<String> getAllLanguages(){
		try {
			
			return PGF.readPGF("Words.pgf").getLanguages().keySet();
			
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
			
			return PGF.readPGF("Words.pgf").getCategories();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public List<String> getAllWords(String language, String partOfSpeech){
		try {
			return PGF.readPGF("Words.pgf").getFunctionsByCat(partOfSpeech);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}


	public String getRandomWord(){
		int nrOfCats = getAllPartOfSpeech(foreignLang).size();
		String randPartOfSpeech = getAllPartOfSpeech(foreignLang).get(new Random().nextInt(nrOfCats));
		int nrOfWords = getAllWords(foreignLang, randPartOfSpeech).size();
		return getAllWords(foreignLang, randPartOfSpeech).get(new Random().nextInt(nrOfWords));
	}

	public boolean gradeAnswer(String foreignWord, String usersAnswer){
		boolean correct = false;
		try {
			Concr c = PGF.readPGF("Words.pgf").getLanguages().get(foreignLang);
			String linearization = c.linearize(c.parse("Noun", usersAnswer).iterator().next().getExpr());
			correct = linearization.equals(foreignWord);
		} catch (FileNotFoundException | ParseError e) {
			e.printStackTrace();
		}
		return correct;
	}


	/*****Private methods******/
	
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

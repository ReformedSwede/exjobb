package grammar;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.grammaticalframework.pgf.PGF;

public class PGFInterface {

	public static void createLanguage(String languageName){
		if(existsLanguage(languageName))
			return;

		//TODO Create new folder. Create abstract & concrete grammar files.
		new File(languageName).mkdir();
	}

	private static boolean existsLanguage(String language){
		return new File(language).exists();
	}

	/**
	 * Inserts a new word into the file system.
	 * @param language The language that the word belongs to
	 * @param partOfSpeech The type of the word (Keep first letter capitalized!)
	 * @param word The word to enter (Keep all letters lowercase!)
	 */
	public static void addWord(String language, String partOfSpeech, String word){
		//TODO check if files for language already exists. If so, run code below. o/w, autogenerate new files
		try{
			String file = "src/grammar/" +
					"Words" + language.substring(0, 1).toUpperCase() + language.substring(1, 3) + ".gf";

			//Insert word
			GfFileManipulator gf = new GfFileManipulator(file);
			gf.insertWord(partOfSpeech, word);
			
			//Compile
			Process pro = new ProcessBuilder("gf", "-make", file).start();
			printCompileLog(pro);

		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Returns a list of all languages in the pgf file
	 * @return List of all languages
     */
	public static Collection<String> getAllLanguages(){
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
	public static List<String> getAllPartOfSpeech(String language){
		try {
			
			return PGF.readPGF("Insert file name here").getCategories();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public static List<String> getAllWords(String language, String partOfSpeech){
		try {
			return PGF.readPGF("Insert file name here").getFunctionsByCat(partOfSpeech);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	private static void printCompileLog(Process pro) throws IOException{
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

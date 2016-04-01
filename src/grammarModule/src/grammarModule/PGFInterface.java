package grammarModule;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.grammaticalframework.pgf.PGF;

public class PGFInterface {
	
	public PGFInterface(){
		 
	}
	
	public void addWord(String language, String partOfSpeech, String word){
		try{
			//Should the word be added to an existing file, or to a new one?
			//Find out!
			
			//Insert word to file;
			
			//Compile;
			new ProcessBuilder("gf", "-make" /*Insert files here*/).start();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public Collection<String> getAllLanguages(){
		try {
			
			return PGF.readPGF("Insert file name here").getLanguages().keySet();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public List<String> getAllPartOfSpeech(String language){
		try {
			
			return PGF.readPGF("Insert file name here").getCategories();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return new ArrayList<String>();
	}
	
	public List<String> getAllWords(String language, String partOfSpeech){
		return null;
	}
	
	

}

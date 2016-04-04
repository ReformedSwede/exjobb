package grammarModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.grammaticalframework.pgf.ExprProb;
import org.grammaticalframework.pgf.PGF;

public class Main {

	public static void main(String[] args) {
		PGF p = null;
		try {
			p = PGF.readPGF("res/Zero.pgf");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for(ExprProb s : p.generateAll(p.getStartCat()))
			System.out.println(s.getExpr().toString());
		
	}

}

package edu.nlt.ling570.project2;

import java.io.File;

import edu.nlt.shallow.data.Vocabulary;
import edu.nlt.shallow.data.vector.DocumentVector;

public class PrintVector {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] Vocabulary
	 * 
	 *            args[1] Path to file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		Vocabulary vocabulary = Util.getVocabulary(new File(args[0]), -1);

		DocumentVector documentVector = Util.getDocumentVector(new File(args[1]), vocabulary);

		documentVector.print();
	}

}

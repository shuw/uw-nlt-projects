package project2;

import java.io.File;
import java.util.HashSet;

import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.vector.DocumentVector;

public class PrintVector {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] IDFTable
	 * 
	 *            args[1] Vocabulary
	 * 
	 *            args[2] Path to file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		IDFTable idfTable = Util.getIDFTable(new File(args[0]));

		HashSet<String> vocabulary = Util.getVocabulary(new File(args[1]));

		DocumentVector documentVector = Util.getDocumentVector(new File(args[2]), idfTable,
				vocabulary);

		documentVector.print();
	}

}

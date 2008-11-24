package project2;

import java.io.File;
import java.util.HashSet;

import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;

public class PrintVectors {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] IDFTable file
	 * 
	 *            args[1] Vocabulary file
	 * 
	 *            args[2] Path to collection of files
	 * 
	 * 
	 */
	public static void main(String[] args) {

		final IDFTable idfTable = Util.getIDFTable(new File(args[0]));

		final HashSet<String> vocabulary = Util.getVocabulary(new File(args[1]));

		InputUtil.processFiles(args[2], new FileProcessor() {

			@Override
			public void processFile(File file) {
				DocumentVector documentVector = Util.getDocumentVector(file, idfTable, vocabulary);

				System.out.println("file:\t" + file.getName());
				documentVector.print();

				System.out.println();
				System.out.println();

			}
		});

	}
}

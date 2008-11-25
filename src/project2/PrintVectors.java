package project2;

import java.io.File;

import project2.data.Vocabulary;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;

public class PrintVectors {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] Vocabulary file
	 * 
	 * 
	 *            args[1] Path to collection of files
	 * 
	 * 
	 */
	public static void main(String[] args) {

		final Vocabulary vocabulary = Util.getVocabulary(new File(args[0]), -1);

		InputUtil.processFiles(args[1], new FileProcessor() {

			@Override
			public void processFile(File file) {
				DocumentVector documentVector = Util.getDocumentVector(file, vocabulary);

				System.out.println("file:\t" + file.getName());
				documentVector.print();

				System.out.println();
				System.out.println();

			}
		});

	}
}

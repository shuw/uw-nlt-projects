package project2;

import java.io.File;

import project2.data.Vocabulary;
import project2.processor.GoldStandard;
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
	 *            args[1] Path to collection of files
	 * 
	 *            args[2] (optional) Gold Standard - to apply filter to only
	 *            linguistic documents
	 * 
	 * 
	 */
	public static void main(String[] args) {

		final Vocabulary vocabulary = Util.getVocabulary(new File(args[0]), -1);

		final GoldStandard goldStandard = (args.length >= 3) ? Util.getGoldStandard(new File(
				args[2])) : null;

		InputUtil.processFiles(args[1], new FileProcessor() {

			@Override
			public void processFile(File file) {
				if (goldStandard == null || goldStandard.isLinguistic(file.getName())) {

					DocumentVector documentVector = Util.getDocumentVector(file, vocabulary);

					System.out.println("file:\t" + file.getName());
					documentVector.print();

					System.out.println();
					System.out.println();
				}

			}
		});

	}
}

package edu.nlt.ling570.project2;

import java.io.File;

import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.shallow.classifier.NotClassifiedException;
import edu.nlt.shallow.data.Vocabulary;
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

		final ClassifierGoldStandard goldStandard = (args.length >= 3) ? Util
				.getGoldStandard(new File(args[2])) : null;

		InputUtil.processFiles(args[1], new FileProcessor() {

			@Override
			public void processFile(File file) {
				try {
					if (goldStandard == null || goldStandard.isPositive(file.getName())) {

						DocumentVector documentVector = Util.getDocumentVector(file, vocabulary);

						System.out.println("file:\t" + file.getName());
						documentVector.print();

						System.out.println();
						System.out.println();
					}
				} catch (NotClassifiedException e) {
					e.printStackTrace(System.err);
				}

			}
		});

	}
}

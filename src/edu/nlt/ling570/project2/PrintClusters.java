package edu.nlt.ling570.project2;

import java.io.File;
import java.util.Collection;

import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.ling570.project2.processor.KMeansRunner;
import edu.nlt.ling570.project2.processor.VectorProcessor;
import edu.nlt.shallow.data.Vocabulary;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.Globals;
import edu.nlt.util.InputUtil;

public class PrintClusters {

	/**
	 * @param args
	 * 
	 *            args[0] - GoldStandardFile
	 * 
	 *            args[1] - Vocabulary file
	 * 
	 *            args[2] - File containing vectors
	 *            <p>
	 *            (generated using PrintVectors)
	 *            </p>
	 */
	public static void main(String[] args) {

		ClassifierGoldStandard goldStandard = Util.getGoldStandard(new File(args[0]));

		VectorProcessor processor = new VectorProcessor();
		InputUtil.process(new File(args[2]), processor);

		Collection<DocumentVector> vectors = processor.getVectors();

		Vocabulary vocabulary = Util.getVocabulary(new File(args[1]), 1000);
		KMeansRunner runner = new KMeansRunner(goldStandard, vocabulary, vectors);

		runner.run(50, 5);

		if (Globals.IsDebugEnabled) {
			System.out.println("Max F-Score: " + runner.getMaxFScore());
		}
		runner.printClusters();

	}
}

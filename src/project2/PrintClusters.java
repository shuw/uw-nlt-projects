package project2;

import java.io.File;
import java.util.Collection;

import project2.data.Vocabulary;
import project2.processor.GoldStandard;
import project2.processor.KMeansRunner;
import project2.processor.VectorProcessor;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.Globals;
import edu.nlt.util.InputUtil;

public class PrintClusters {

	/**
	 * @param args
	 * 
	 *            args[1] - GoldStandardFile
	 * 
	 *            args[0] - Vocabulary file
	 * 
	 *            args[2] - File containing vectors
	 *            <p>
	 *            (generated using PrintVectors)
	 *            </p>
	 */
	public static void main(String[] args) {

		GoldStandard goldStandard = Util.getGoldStandard(new File(args[0]));

		VectorProcessor processor = new VectorProcessor();
		InputUtil.process(new File(args[2]), processor);

		Collection<DocumentVector> vectors = processor.getVectors();

		Vocabulary vocabulary = Util.getVocabulary(new File(args[1]), 2800);
		KMeansRunner runner = new KMeansRunner(goldStandard, vocabulary, vectors);

		runner.run(3, 15);

		if (Globals.IsDebugEnabled) {
			System.out.println("Wrong classifications: " + runner.getMinWrongClassifications());
		}
		runner.printClusters();

	}
}

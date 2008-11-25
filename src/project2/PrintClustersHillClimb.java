package project2;

import java.io.File;
import java.util.Collection;

import project2.data.Vocabulary;
import project2.processor.GoldStandard;
import project2.processor.KMeansRunner;
import project2.processor.VectorProcessor;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;

public class PrintClustersHillClimb {

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

		for (int vocabSize = 50; vocabSize <= 10000; vocabSize += 50) {

			Vocabulary vocabulary = Util.getVocabulary(new File(args[1]), vocabSize);
			KMeansRunner runner = new KMeansRunner(goldStandard, vocabulary, vectors);

			for (int clusters = 3; clusters <= 3; clusters++) {
				System.out.println("\nVocab size: " + vocabSize + " | Cluster size: " + clusters);
				runner.run(clusters, 100);

				System.out.println();
				System.out.println("Wrong classifications: " + runner.getMinWrongClassifications());
			}
		}

	}
}

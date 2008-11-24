package project2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import project2.helper.GoldStandard;
import edu.nlt.algorithm.KMeansAlgorithm;
import edu.nlt.algorithm.KMeansAlgorithm.Cluster;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;
import edu.nlt.util.Singletons;
import edu.nlt.util.processor.LineProcessor;

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

		HashSet<String> vocabulary = Util.getVocabulary(new File(args[1]));

		VectorProcessor processor = new VectorProcessor();
		InputUtil.process(new File(args[2]), processor);

		Collection<DocumentVector> vectors = processor.getVectors();

		KMeansRunner runner = new KMeansRunner(goldStandard, vocabulary, vectors);

		for (int clusters = 2; clusters < 30; clusters++) {
			System.out.println("Cluster size: " + clusters);
			runner.run(clusters, 30);

			runner.printResults();
		}

	}
}

class VectorProcessor implements LineProcessor {

	private LinkedList<DocumentVector> vectors = new LinkedList<DocumentVector>();
	private Hashtable<String, DocumentFeature> currentVectorTable;
	private String currentVectorName;

	@Override
	public void processLine(String value) {
		if (value.startsWith("file:")) {
			pushVectorTable();

			currentVectorName = value.split("\t")[1];
			currentVectorTable = new Hashtable<String, DocumentFeature>();

		} else if (!"".equals(value)) {

			String[] components = value.split("\t");

			Word word = new Word(components[0]);
			double strength = Double.parseDouble(components[1]);

			currentVectorTable.put(word.getKey(), new DocumentFeature(word, strength));

		}

	}

	private void pushVectorTable() {
		if (currentVectorTable != null) {
			vectors.add(new DocumentVector(currentVectorTable, currentVectorName));
			currentVectorTable = null;
		}
	}

	public List<DocumentVector> getVectors() {
		pushVectorTable();
		return new ArrayList<DocumentVector>(vectors);
	}

}

class KMeansRunner {
	private int minWrongClassifications = Integer.MAX_VALUE;
	private KMeansAlgorithm bestAlgorithm;
	private GoldStandard goldStandard;
	private Collection<DocumentVector> vectors;
	private HashSet<String> vocabulary;

	public KMeansRunner(GoldStandard goldStandard, HashSet<String> vocabulary,
			Collection<DocumentVector> vectors) {
		super();
		this.goldStandard = goldStandard;
		this.vectors = vectors;
		this.vocabulary = vocabulary;
	}

	public void run(int clusters, int iterations) {

		for (int iteration = 0; iteration < iterations; iteration++) {
			// Repeat algorithm several times with different seeds
			runIteration(goldStandard, new KMeansAlgorithm(vectors, vocabulary, clusters));
		}
	}

	private void runIteration(GoldStandard standard, KMeansAlgorithm clusteringAlg) {
		while (!clusteringAlg.isStopCriteriaReached()) {

			clusteringAlg.runIteration();

		}

		KMeansAlgorithm.Cluster[] clusters = clusteringAlg.getClusters();

		int wrongClassifications = 0;
		for (KMeansAlgorithm.Cluster cluster : clusters) {

			int linguisticCount = getNumOfLinguisticMembers(standard, cluster.getVectors());

			wrongClassifications += Math.min(linguisticCount, cluster.getVectors().size()
					- linguisticCount);

			double percentLinguistic = (double) linguisticCount
					/ (double) cluster.getVectors().size();

			// System.out.print(Singletons.PercentageFormatter.format(percentLinguistic)
			// + " ");

		}

		if (wrongClassifications < minWrongClassifications) {
			minWrongClassifications = wrongClassifications;
			bestAlgorithm = clusteringAlg;

		}

		// System.out.println("misfitted: " + wrongClassifications);

	}

	private static int getNumOfLinguisticMembers(GoldStandard tags,
			Collection<KMeansAlgorithm.NamedVector> vectors) {

		int linguisticCount = 0;
		for (KMeansAlgorithm.NamedVector vector : vectors) {

			if (tags.isLinguistic(vector.getName().split("\\.")[0])) {
				linguisticCount++;
			}
		}
		return linguisticCount;

	}

	private static boolean isClusterLinguistic(GoldStandard tags,
			Collection<KMeansAlgorithm.NamedVector> vectors) {
		if (vectors.size() == 0) {
			throw new IllegalArgumentException();
		}
		int linguisticCount = getNumOfLinguisticMembers(tags, vectors);

		return linguisticCount >= (vectors.size() - linguisticCount);
	}

	public void printResults() {
		System.out.println("Wrong classifications: " + minWrongClassifications);

		// Cluster[] clusters = bestAlgorithm.getClusters();
		// for (int i = 0; i < clusters.length; i++) {
		// KMeansAlgorithm.Cluster cluster = clusters[i];
		//
		// boolean isLinguistic = isClusterLinguistic(goldStandard,
		// cluster.getVectors());
		//
		// System.out.println(isLinguistic ? "L\tLinguistic cluster"
		// : "NL\tNon-linguistic cluster");
		//
		// bestAlgorithm.printCentroid(i);
		//
		// }

	}
}
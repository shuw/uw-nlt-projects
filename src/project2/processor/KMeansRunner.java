package project2.processor;

import java.util.Collection;

import project2.data.Vocabulary;
import edu.nlt.algorithm.KMeansAlgorithm;
import edu.nlt.algorithm.KMeansAlgorithm.Cluster;
import edu.nlt.shallow.data.vector.DocumentVector;

public class KMeansRunner {
	private static int getNumOfLinguisticMembers(GoldStandard tags,
			Collection<KMeansAlgorithm.NamedVector> vectors) {

		int linguisticCount = 0;
		for (KMeansAlgorithm.NamedVector vector : vectors) {

			if (tags.isLinguistic(vector.getName())) {
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

	private KMeansAlgorithm bestAlgorithm;
	private GoldStandard goldStandard;
	private int minWrongClassifications;
	private double averageWrongClassifications;

	private Collection<DocumentVector> vectors;

	private Vocabulary vocabulary;

	public KMeansRunner(GoldStandard goldStandard, Vocabulary vocabulary,
			Collection<DocumentVector> vectors) {
		super();
		this.goldStandard = goldStandard;
		this.vectors = vectors;
		this.vocabulary = vocabulary;
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

			// double percentLinguistic = (double) linguisticCount
			// / (double) cluster.getVectors().size();
			// System.out.print(Singletons.PercentageFormatter.format(percentLinguistic)
			// + " ");

		}

		System.out.print(wrongClassifications + " ");

		averageWrongClassifications += wrongClassifications;
		if (wrongClassifications < minWrongClassifications) {
			minWrongClassifications = wrongClassifications;
			bestAlgorithm = clusteringAlg;

		}

		// System.out.println("misfitted: " + wrongClassifications);

	}

	public int getMinWrongClassifications() {
		return minWrongClassifications;
	}

	public double getAverageWrongClassifications() {
		return averageWrongClassifications;
	}

	public void printClusters() {

		Cluster[] clusters = bestAlgorithm.getClusters();
		for (int i = 0; i < clusters.length; i++) {
			KMeansAlgorithm.Cluster cluster = clusters[i];

			boolean isLinguistic = isClusterLinguistic(goldStandard, cluster.getVectors());

			System.out.println("Cluster: "
					+ (isLinguistic ? "L\tLinguistic cluster" : "NL\tNon-linguistic cluster"));

			bestAlgorithm.printCentroid(i);

		}

	}

	public void run(int clusters, int iterations) {
		minWrongClassifications = Integer.MAX_VALUE;
		averageWrongClassifications = 0;
		bestAlgorithm = null;

		for (int iteration = 0; iteration < iterations; iteration++) {

			// Repeat algorithm several times with different seeds
			runIteration(goldStandard, new KMeansAlgorithm(vectors, vocabulary, clusters));

		}

		averageWrongClassifications /= iterations;
	}
}

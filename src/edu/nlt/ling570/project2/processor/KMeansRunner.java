package edu.nlt.ling570.project2.processor;

import java.util.Collection;

import edu.nlt.algorithm.KMeansAlgorithm;
import edu.nlt.algorithm.KMeansAlgorithm.Cluster;
import edu.nlt.algorithm.KMeansAlgorithm.NamedVector;
import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.shallow.classifier.BinaryClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;
import edu.nlt.shallow.data.Vocabulary;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.Formatters;
import edu.nlt.util.Globals;
import edu.nlt.util.MathUtil;

public class KMeansRunner {
	private static int getNumOfLinguisticMembers(BinaryClassifier tags,
			Collection<KMeansAlgorithm.NamedVector> vectors) {

		int linguisticCount = 0;
		for (KMeansAlgorithm.NamedVector vector : vectors) {

			try {
				if (tags.isPositive(vector.getName())) {
					linguisticCount++;
				}
			} catch (NotClassifiedException e) {
				e.printStackTrace(System.err);
			}
		}
		return linguisticCount;

	}

	private static boolean isClusterLinguistic(BinaryClassifier tags,
			Collection<KMeansAlgorithm.NamedVector> vectors) {
		if (vectors.size() == 0) {
			throw new IllegalArgumentException();
		}
		int linguisticCount = getNumOfLinguisticMembers(tags, vectors);

		return linguisticCount >= (vectors.size() - linguisticCount);
	}

	private KMeansAlgorithm bestAlgorithm;
	private ClassifierGoldStandard goldStandard;
	private double maxFScore;
	private double averageFScoreClassifications;

	private Collection<DocumentVector> vectors;

	private Vocabulary vocabulary;

	public KMeansRunner(ClassifierGoldStandard goldStandard, Vocabulary vocabulary,
			Collection<DocumentVector> vectors) {
		super();
		this.goldStandard = goldStandard;
		this.vectors = vectors;
		this.vocabulary = vocabulary;
	}

	private void runIteration(ClassifierGoldStandard standard, KMeansAlgorithm clusteringAlg) {
		while (!clusteringAlg.isStopCriteriaReached()) {

			clusteringAlg.runIteration();

		}

		KMeansAlgorithm.Cluster[] clusters = clusteringAlg.getClusters();

		int truePositives = 0;
		int relevantResults = 0;
		int resultsRetrieved = 0;
		int emptyClusters = 0;
		for (KMeansAlgorithm.Cluster cluster : clusters) {

			Collection<NamedVector> vectors = cluster.getVectors();

			if (vectors.size() > 0) {
				boolean isClusterPositive = isClusterLinguistic(standard, vectors);

				for (KMeansAlgorithm.NamedVector vector : vectors) {

					try {
						boolean isPositive = standard.isPositive(vector.getName());
						if (isPositive) {
							relevantResults++;
						}

						if (isClusterPositive) {
							resultsRetrieved++;
						}

						if (isPositive && isClusterPositive) {
							truePositives++;
						}
					} catch (NotClassifiedException e) {
						e.printStackTrace(System.err);
					}
				}
			} else {
				emptyClusters++;
			}

		}
		double fScore = MathUtil.getFScore(truePositives, resultsRetrieved, relevantResults);

		if (Globals.IsDebugEnabled) {
			System.out.print(Formatters.FractionFormatter.format(fScore));

			if (emptyClusters > 0) {
				System.out.print("-EmptyClusters:" + emptyClusters + " ");
			}
			System.out.print(" ");
		}

		averageFScoreClassifications += fScore;
		if (fScore > maxFScore) {
			maxFScore = fScore;
			bestAlgorithm = clusteringAlg;

		}

		// System.out.println("misfitted: " + wrongClassifications);

	}

	public double getMaxFScore() {
		return maxFScore;
	}

	public double getAverageFScore() {
		return averageFScoreClassifications;
	}

	public void printClusters() {

		Cluster[] clusters = bestAlgorithm.getClusters();

		for (int i = 0; i < clusters.length; i++) {
			KMeansAlgorithm.Cluster cluster = clusters[i];

			if (cluster.getVectors().size() > 0) {
				boolean isLinguistic = isClusterLinguistic(goldStandard, cluster.getVectors());

				System.out.println("Cluster: "
						+ (isLinguistic ? "L\tLinguistic cluster" : "NL\tNon-linguistic cluster"));

				bestAlgorithm.printCentroid(i);
			}

		}

	}

	public void run(int clusters, int iterations) {
		maxFScore = Double.MIN_VALUE;
		averageFScoreClassifications = 0;
		bestAlgorithm = null;

		for (int iteration = 0; iteration < iterations; iteration++) {

			// Repeat algorithm several times with different seeds
			runIteration(goldStandard, new KMeansAlgorithm(vectors, vocabulary, clusters,
					goldStandard));

		}

		averageFScoreClassifications /= iterations;
	}
}

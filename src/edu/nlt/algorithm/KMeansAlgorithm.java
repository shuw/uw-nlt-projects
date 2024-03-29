package edu.nlt.algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Random;

import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.shallow.classifier.NotClassifiedException;
import edu.nlt.shallow.data.Vocabulary;
import edu.nlt.shallow.data.WordMagnitude;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.VectorUtil;

/**
 * Implementation based on from Schutze and Manning (p.516)
 * 
 */
public class KMeansAlgorithm {

	private static Cluster[] getClusters(NamedVector[] vectorsNormalized,
			Vector[] clusterCentroidsNormalized) {

		// Assign all vectors to a cluster
		Cluster[] clusters = new Cluster[clusterCentroidsNormalized.length];

		for (int i = 0; i < clusters.length; i++) {
			clusters[i] = new Cluster();
		}

		for (int vectorIndex = 0; vectorIndex < vectorsNormalized.length; vectorIndex++) {
			NamedVector namedVector = vectorsNormalized[vectorIndex];

			double[] vector = namedVector.getValue();

			double maxSimilarity = Double.NEGATIVE_INFINITY;
			int closestCentroid = Integer.MIN_VALUE;

			// find the closest centroid
			for (int centroidIndex = 0; centroidIndex < clusterCentroidsNormalized.length; centroidIndex++) {
				double[] centroid = clusterCentroidsNormalized[centroidIndex].getValue();

				// This is equivalent to the cosine similarity since the vectors
				// are normalized
				double similarity = VectorUtil.dotProduct(vector, centroid);

				if (similarity > maxSimilarity) {
					maxSimilarity = similarity;
					closestCentroid = centroidIndex;
				}
			}

			clusters[closestCentroid].addVector(namedVector);

		}

		return clusters;

	}

	private Vector[] clusterCentroidsNormalized;

	private Cluster[] clusters;

	private Cluster[] prevClusters;

	private String[] indexToWord;

	private int vectorSize;

	private NamedVector[] vectorsNormalized;

	private static final boolean seedClusters = true;

	private ClassifierGoldStandard goldStandard;

	public KMeansAlgorithm(Collection<DocumentVector> vectors, Vocabulary vocabulary, int clusters,
			ClassifierGoldStandard goldStandard) {
		super();

		this.vectorSize = vocabulary.size();
		this.goldStandard = goldStandard;
		initVectors(vectors, vocabulary);

		initClusters(clusters);
	}

	private void initClusters(int clusters) {

		clusterCentroidsNormalized = new Vector[clusters];
		Random random = new Random();

		if (seedClusters) {

			ArrayList<NamedVector> linguisticVectors = new ArrayList<NamedVector>();
			ArrayList<NamedVector> nonLinguisticVectors = new ArrayList<NamedVector>();

			for (NamedVector vector : vectorsNormalized) {

				try {
					if (goldStandard.isPositive(vector.getName())) {
						linguisticVectors.add(vector);
					} else {
						nonLinguisticVectors.add(vector);
					}
				} catch (NotClassifiedException e) {
					e.printStackTrace(System.err);
				}
			}

			boolean randomBoolean = random.nextDouble() < 0.5d;

			// distribute clusters evenly

			for (int i = 0; i < clusterCentroidsNormalized.length; i++) {
				// alternate between ling / non-ling clusters
				boolean linguisticCluster = i % 2 != 0;

				// randomize starting cluster
				if (randomBoolean) {
					linguisticCluster = !linguisticCluster;
				}

				ArrayList<NamedVector> chosenVectors = linguisticCluster ? linguisticVectors
						: nonLinguisticVectors;

				Cluster cluster = new Cluster();

				// divide the remainder evengly between ling / non-ling
				int remainingClusters = (clusterCentroidsNormalized.length - i - 1) / 2;

				int vectorsToChose = chosenVectors.size() / (remainingClusters + 1);

				for (int j = 0; j < vectorsToChose; j++) {

					int randomIndex = (int) (random.nextDouble() * (chosenVectors.size() - 1));
					cluster.addVector(chosenVectors.remove(randomIndex));
				}

				clusterCentroidsNormalized[i] = new Vector(cluster.getCentroidNormalized());
			}

		} else {
			// Initialize random values
			for (int i = 0; i < clusterCentroidsNormalized.length; i++) {
				double[] centroid = new double[vectorSize];

				for (int vectorIndex = 0; vectorIndex < vectorSize; vectorIndex++) {

					centroid[vectorIndex] = random.nextDouble();

				}

				VectorUtil.normalize(centroid);

				clusterCentroidsNormalized[i] = new Vector(centroid);

			}
		}

	}

	public void runIteration() {

		prevClusters = clusters;

		clusters = getClusters(vectorsNormalized, clusterCentroidsNormalized);

		// Assign new centroids
		//
		for (int i = 0; i < clusterCentroidsNormalized.length; i++) {

			Cluster cluster = clusters[i];

			if (cluster.getVectors().size() > 0) {

				clusterCentroidsNormalized[i] = new Vector(cluster.getCentroidNormalized());
			}
		}
	}

	/**
	 * Create more efficient representation fo vocabulary
	 */
	private void initVectors(Collection<DocumentVector> docVectors, Vocabulary vocabulary) {

		// Map each word to an array index
		indexToWord = new String[vocabulary.size()];
		Hashtable<String, Integer> wordToIndex = new Hashtable<String, Integer>(vectorSize);
		{
			int index = 0;
			for (String wordStr : vocabulary.values()) {
				wordToIndex.put(wordStr, index);
				indexToWord[index] = wordStr;
				index++;
			}
		}
		vectorsNormalized = new NamedVector[docVectors.size()];

		int count = 0;
		// Convert document vectors to more efficient representation
		for (DocumentVector docVector : docVectors) {

			double[] vector = new double[vectorSize];
			for (DocumentFeature feature : docVector.values()) {

				Integer index = wordToIndex.get(feature.getWord().getKey());

				if (index != null) {

					vector[index] = feature.getMagnitude();

				}
			}

			VectorUtil.normalize(vector);
			vectorsNormalized[count++] = new NamedVector(vector, docVector.getVectorName());

		}

	}

	public void printCentroid(int centroidIndex) {

		double[] centroid = clusterCentroidsNormalized[centroidIndex].getValue();

		WordMagnitude[] words = new WordMagnitude[vectorSize];

		for (int i = 0; i < vectorSize; i++) {

			Word word = new Word(indexToWord[i]);

			words[i] = new WordMagnitude(word, centroid[i]);
		}

		Arrays.sort(words, new Comparator<WordMagnitude>() {
			@Override
			public int compare(WordMagnitude o1, WordMagnitude o2) {
				return Double.compare(o2.getMagnitude(), o1.getMagnitude());
			}
		});

		int count = 0;
		for (WordMagnitude wordMagnitude : words) {
			System.out.println(wordMagnitude.getWord() + "\t" + wordMagnitude.getMagnitude());

			// if (count++ >= 10) {
			// break;
			// }
		}

		System.out.println("\n\n");

	}

	public Cluster[] getClusters() {
		return clusters;
	}

	public boolean isStopCriteriaReached() {

		if (prevClusters == null)
			return false;

		int errorCount = 0;
		for (int i = 0; i < clusters.length; i++) {

			Cluster cluster = clusters[i];
			Cluster prevCluster = prevClusters[i];

			for (NamedVector vector : cluster.getVectors()) {
				if (!prevCluster.containsVector(vector.getName())) {
					errorCount++;
				}

			}

		}

		// System.out.println("Iteration errorCount = " + errorCount);
		return errorCount == 0;
	}

	public static class Cluster {
		private Hashtable<String, NamedVector> vectorTable = new Hashtable<String, NamedVector>();

		public void addVector(NamedVector vector) {
			vectorTable.put(vector.getName(), vector);
		}

		public Collection<NamedVector> getVectors() {
			return vectorTable.values();
		}

		public Collection<double[]> getVectorsRaw() {
			ArrayList<double[]> vectorsRaw = new ArrayList<double[]>(vectorTable.size());

			for (NamedVector namedVector : vectorTable.values()) {
				vectorsRaw.add(namedVector.getValue());
			}

			return vectorsRaw;
		}

		public double[] getCentroidNormalized() {
			double[] centroid = VectorUtil.getCentroid(getVectorsRaw());

			VectorUtil.normalize(centroid);

			return centroid;

		}

		public boolean containsVector(String vectorName) {
			return vectorTable.containsKey(vectorName);
		}

	}

	public static class NamedVector extends Vector {

		private String name;

		public NamedVector(double[] value, String name) {
			super(value);
			this.name = name;
		}

		public String getName() {
			return name;
		}

	}

	public static class Vector {

		private double[] value;

		public Vector(double[] value) {
			super();
			this.value = value;
		}

		public double[] getValue() {
			return value;
		}

	}

}

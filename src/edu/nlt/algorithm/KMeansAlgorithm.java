package edu.nlt.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Random;

import project2.data.Vocabulary;

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

	public KMeansAlgorithm(Collection<DocumentVector> vectors, Vocabulary vocabulary, int clusters) {
		super();

		this.vectorSize = vocabulary.size();

		initVectors(vectors, vocabulary);

		initClusters(clusters);
	}

	private void initClusters(int clusters) {

		clusterCentroidsNormalized = new Vector[clusters];

		Random random = new Random();

		// Initialize random values
		for (int cIndex = 0; cIndex < clusters; cIndex++) {
			double[] centroid = new double[vectorSize];

			for (int vectorIndex = 0; vectorIndex < vectorSize; vectorIndex++) {

				centroid[vectorIndex] = random.nextDouble();

			}

			VectorUtil.normalize(centroid);

			clusterCentroidsNormalized[cIndex] = new Vector(centroid);

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

	public void runIteration() {

		prevClusters = clusters;

		clusters = getClusters(vectorsNormalized, clusterCentroidsNormalized);

		// Assign new centroids
		//
		for (int i = 0; i < clusterCentroidsNormalized.length; i++) {

			Cluster cluster = clusters[i];

			if (cluster.getVectors().size() > 0) {
				double[] centroid = VectorUtil.getCentroid(cluster.getVectorsRaw());

				VectorUtil.normalize(centroid);

				clusterCentroidsNormalized[i] = new Vector(centroid);
			}
		}
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

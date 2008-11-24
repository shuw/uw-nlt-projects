package edu.nlt.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import edu.nlt.shallow.data.vector.DocumentFeature;

public class VectorUtil {

	public static void normalize(double[] vector) {

		double length = getLength(vector);

		for (int dimension = 0; dimension < vector.length; dimension++) {

			vector[dimension] /= length;

		}
	}

	public static double[] getNormalizedVector(Collection<DocumentFeature> features,
			HashSet<String> vocabulary) {

		int vectorSize = vocabulary.size();

		// Map each word to an array index
		Hashtable<String, Integer> wordToIndex = new Hashtable<String, Integer>(vectorSize);

		{
			int index = 0;
			for (String wordStr : vocabulary) {
				wordToIndex.put(wordStr, index);

				index++;
			}
		}

		double[] vector = new double[vectorSize];

		for (DocumentFeature feature : features) {

			Integer index = wordToIndex.get(feature.getWord().getKey());

			if (index != null) {

				vector[index] = feature.getMagnitude();

			} else {
				(new Exception("Could not find vector in vocabulary")).printStackTrace(System.err);
			}
		}

		VectorUtil.normalize(vector);
		return vector;

	}

	public static double[] getCentroid(Collection<double[]> vectors) {

		double[] centroid = null;

		for (double[] vector : vectors) {

			if (centroid == null) {
				centroid = new double[vector.length];
			}

			add(centroid, vector);
		}

		multiply(centroid, 1d / (double) vectors.size());

		return centroid;

	}

	public static void multiply(double[] value, double parameter) {

		for (int dimension = 0; dimension < value.length; dimension++) {
			value[dimension] *= parameter;
		}

	}

	public static void add(double[] value, double[] parameter) {

		for (int dimension = 0; dimension < parameter.length; dimension++) {
			value[dimension] += parameter[dimension];
		}

	}

	public static double getLength(double[] vector) {
		double length = 0;
		{
			for (double magnitude : vector) {
				length += magnitude * magnitude;
			}

			length = Math.sqrt(length);
		}

		return length;
	}

	public static double dotProduct(double[] vector1, double[] vector2) {

		double rtnValue = 0;

		if (vector1.length != vector2.length) {
			throw new IllegalArgumentException("vector lengths are not equal " + vector1.length
					+ " " + vector2.length);
		}

		for (int dimension = 0; dimension < vector1.length; dimension++) {

			rtnValue += vector1[dimension] * vector2[dimension];

		}
		return rtnValue;

	}

	public static void main(String[] args) {

		double[] vector = new double[] { 0.23, 0.44, 213123, 23, 52534, 23.55, 6566 };

		normalize(vector);

		System.out.println("Length of vector should be 1: " + getLength(vector));

	}
}

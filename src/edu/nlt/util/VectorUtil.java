package edu.nlt.util;

import java.util.Collection;

public class VectorUtil {

	public static void normalize(double[] vector) {

		double length = getLength(vector);

		for (int dimension = 0; dimension < vector.length; dimension++) {

			vector[dimension] /= length;

		}
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

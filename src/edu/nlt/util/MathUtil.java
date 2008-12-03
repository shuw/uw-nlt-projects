package edu.nlt.util;

public class MathUtil {
	public static double getLogBase2(double value) {
		return value != 0 ? (Math.log(value) / Math.log(2)) : Double.NEGATIVE_INFINITY;

	}

	public static double getFScore(int truePositives, int documentsRetrieved, int relevantDocuments) {
		double precision = (double) truePositives / (double) documentsRetrieved;
		double recall = (double) truePositives / (double) relevantDocuments;

		return (2d * recall * precision) / (recall + precision);
	}
}

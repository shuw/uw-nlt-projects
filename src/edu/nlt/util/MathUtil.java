package edu.nlt.util;

public class MathUtil {
	public static double getLogBase2(double value) {
		return value != 0 ? (Math.log(value) / Math.log(2)) : Double.NEGATIVE_INFINITY;

	}
}

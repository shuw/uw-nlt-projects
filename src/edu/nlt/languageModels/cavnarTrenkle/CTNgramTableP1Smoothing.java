package edu.nlt.languageModels.cavnarTrenkle;

import java.util.Hashtable;

public class CTNgramTableP1Smoothing extends CTNgramTable {
	private long vocabularySize;
	private double lamda;

	public CTNgramTableP1Smoothing(Hashtable<String, Integer> table, int n, long vocabularySize, double lamda) {
		super(table, n);
		this.vocabularySize = vocabularySize;
		this.lamda = lamda;
	}

	@Override
	public double getTokenProb(String value) {
		Integer count = getTokenCount(value);

		double prob = ((double) count + lamda) / (double) (getTotalCount() + vocabularySize);

		return prob;

	}

}

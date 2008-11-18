package edu.nlt.languageModels.cavnarTrenkle;

import java.util.Collection;
import java.util.Hashtable;

import edu.nlt.util.MathUtil;

public abstract class CTNgramTable {
	private int paramN;
	private Hashtable<String, Integer> table;
	private long totalCount;

	public CTNgramTable(Hashtable<String, Integer> table, int paramN) {
		super();
		this.paramN = paramN;
		this.table = table;
		init();
	}

	public double calculateEntropy() {
		long totalCount = getTotalCount();

		double entropy = 0;

		// Iterate over probability space
		for (String token : getTokens()) {
			double prob = (double) getTokenCount(token) / (double) totalCount;

			entropy += -1 * prob * MathUtil.getLogBase2(prob);
		}
		return entropy;
	}

	/**
	 * D(P|Q) = Sum i (P(i) log P(i) / Q(i)
	 * 
	 * P - this table
	 * 
	 * @param table
	 *            table presenting Q probabilities
	 * @return Kullback-Leibler divergence
	 */
	public double calculateKLDivergence(CTNgramTable table) {
		double accumulativeProb = 0;

		for (String token : getTokens()) {

			double pProb = getTokenProb(token);
			double qProb = table.getTokenProb(token);

			accumulativeProb += pProb * MathUtil.getLogBase2(pProb / qProb);
		}

		return accumulativeProb;

	}

	public double calculatePerplexity() {

		return Math.pow(2, calculateEntropy());
	}

	public int getParamN() {
		return paramN;
	}

	public int getTokenCount(String value) {
		Integer count = table.get(value);
		return count != null ? count : 0;
	}

	public abstract double getTokenProb(String value);

	protected Collection<String> getTokens() {
		return table.keySet();
	}

	public long getTotalCount() {
		return totalCount;
	}

	public long getVocabularyCount() {
		return table.size();
	}

	private void init() {
		long totalCount = 0;
		for (Integer count : table.values()) {
			totalCount += count;
		}
		this.totalCount = totalCount;
	}
}
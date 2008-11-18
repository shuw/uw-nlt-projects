package edu.nlt.languageModels.cavnarTrenkle;

import java.util.Hashtable;

public class CTNgramGoodTuringSmoothing extends CTNgramTable {
	private int paramK;
	private int totalVocabSize;

	public CTNgramGoodTuringSmoothing(Hashtable<String, Integer> table, int paramN, int vocabularySize, int paramK) {
		super(table, paramN);
		this.paramK = paramK;
		this.totalVocabSize = vocabularySize;

		calculateNcTable();
	}

	// map c -> c*
	private double updatedFrequencies[];
	private Hashtable<Integer, Long> ncTable;

	/**
	 * See Jurafsky & Martin, p. 101
	 */
	private void calculateNcTable() {

		// Calculate frequency of frequency table, N[c]
		//
		ncTable = new Hashtable<Integer, Long>();

		for (String token : getTokens()) {

			int frequency = getTokenCount(token);

			long frequencyOfFrequency = ncTable.containsKey(frequency) ? ncTable.get(frequency) : 0;

			frequencyOfFrequency++;

			ncTable.put(frequency, frequencyOfFrequency);
		}

		// assume frequency of unknown tokens is unigram ^ paramN - total seen
		long unseenCount = totalVocabSize - getVocabularyCount();
		if (unseenCount < 0) {
			(new Exception("Unexpected error")).printStackTrace(System.err);
		}
		ncTable.put(0, unseenCount);

		// map c -> c*
		updatedFrequencies = new double[paramK + 1];

		double ffKPlus1 = ncTable.get(paramK + 1);
		double ff1 = ncTable.get(1);

		for (int frequency = 0; frequency <= paramK; frequency++) {

			double ff = ncTable.get(frequency);
			double ffPlus1 = ncTable.containsKey(frequency + 1) ? ncTable.get(frequency + 1) : 0d;
			if (ffPlus1 != 0) {
				//
				// double cNumerator = (((double) frequency + 1) * (ffPlus1 /
				// ff))
				// - ((double) frequency * (((paramK + 1) * ffKPlus1) / ff1));
				//
				// double cDenominator = 1d - (((paramK + 1) * ffKPlus1) / ff1);
				//
				// updatedFrequencies[frequency] = cNumerator / cDenominator;

				updatedFrequencies[frequency] = (((double) frequency + 1) * (ffPlus1 / ff));
			}

		}

	}

	public long getFrequencyOfFrequency(int frequency) {
		return ncTable.containsKey(frequency) ? ncTable.get(frequency) : 0;
	}

	public double getSmoothedFrequency(int frequency) {
		return updatedFrequencies[frequency];
	}

	@Override
	public double getTokenProb(String value) {

		double c = getTokenCount(value);
		if (c <= paramK) {
			c = updatedFrequencies[(int) c];
		}

		if (c == 0) {
			(new Exception("Unexpected error")).printStackTrace(System.err);
			return 1 / getTotalCount();
		}

		return c / (double) getTotalCount();

	}

}

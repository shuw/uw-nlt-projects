package edu.nlt.languageModels.cavnarTrenkle;

import java.util.Hashtable;

public class CTLanguageModelBuilder {
	public static final int maxNGramToBuild = 3;
	private Hashtable<Integer, Hashtable<String, Integer>> tables = new Hashtable<Integer, Hashtable<String, Integer>>();

	public void addValue(String value, int count) {
		int ngram = value.length();

		if (ngram > maxNGramToBuild) {
			return;
		}

		Hashtable<String, Integer> ngramModel = tables.get(ngram);
		if (ngramModel == null) {
			ngramModel = new Hashtable<String, Integer>();
			tables.put(ngram, ngramModel);
		}
		if (!ngramModel.contains(value)) {
			ngramModel.put(value, count);
		} else {
			(new Exception("Duplicate value")).printStackTrace(System.err);
		}

	}

	public CTLanguageModel buildModel() {

		return new CTLanguageModel(tables);
	}

}

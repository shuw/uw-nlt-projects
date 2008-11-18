package edu.nlt.languageModels.cavnarTrenkle;

import java.security.InvalidParameterException;
import java.util.Hashtable;

public class CTLanguageModel {
	private Hashtable<Integer, Hashtable<String, Integer>> tables = new Hashtable<Integer, Hashtable<String, Integer>>();

	public CTLanguageModel(Hashtable<Integer, Hashtable<String, Integer>> tables) {
		super();
		this.tables = tables;
	}

	public enum SmoothingMethod {
		None, Plus1, GoodTuring
	}

	public CTNgramTable getNgramTable(int paramN, SmoothingMethod smoothingMethod) {
		Hashtable<String, Integer> table = tables.get(paramN);

		if (table == null) {
			table = new Hashtable<String, Integer>();
			(new Exception("No table found")).printStackTrace(System.err);
		}

		if (smoothingMethod == SmoothingMethod.Plus1) {
			int unigramCount = tables.get(1).size();
			int vocabSize = (int) Math.pow((double) unigramCount, (double) paramN);

			return new CTNgramTableP1Smoothing(table, paramN, vocabSize, 1d);
		} else if (smoothingMethod == SmoothingMethod.GoodTuring) {
			return getNgramSmoothingTable(paramN, 5);
		} else if (smoothingMethod == SmoothingMethod.None) {
			return new CTNgramModelNS(table, paramN);
		} else {
			throw new InvalidParameterException();
		}
	}

	public CTNgramGoodTuringSmoothing getNgramSmoothingTable(int paramN, int paramK) {
		Hashtable<String, Integer> table = tables.get(paramN);

		if (table == null) {
			table = new Hashtable<String, Integer>();
			(new Exception("No table found")).printStackTrace(System.err);
		}

		int unigramCount = tables.get(1).size();
		int vocabSize = (int) Math.pow((double) unigramCount, (double) paramN);

		return new CTNgramGoodTuringSmoothing(table, paramN, vocabSize, paramK);
	}

}

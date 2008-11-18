package edu.nlt.languageModels.cavnarTrenkle;

import java.util.Hashtable;

/**
 * No smoothing table
 * 
 * @author shuwu
 * 
 */
public class CTNgramModelNS extends CTNgramTable {

	public CTNgramModelNS(Hashtable<String, Integer> table, int paramN) {
		super(table, paramN);
	}

	@Override
	public double getTokenProb(String value) {
		return (double) getTokenCount(value) / (double) getTotalCount();
	}

}

package edu.nlt.shallow.data.table;

import java.util.Collection;
import java.util.Hashtable;

import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.tags.Word;

public class IDFTable {

	private Hashtable<String, WordIDF> table;

	private double smoothingNullValue;

	public IDFTable(Hashtable<String, WordIDF> table, double smoothingNullValue) {
		super();
		this.table = table;
		this.smoothingNullValue = smoothingNullValue;
	}

	public double getIDF(Word word) {
		WordIDF result = table.get(word.getKey());
		if (result != null) {
			return result.getIdf();
		} else {
			return smoothingNullValue;
		}

	}

	public double getSmoothingNullValue() {
		return smoothingNullValue;
	}

	public Collection<WordIDF> values() {
		return table.values();
	}
}

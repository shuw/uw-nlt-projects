package edu.nlt.shallow.data;

import edu.nlt.shallow.data.tags.Word;

public class IDFResult {

	private Word word;

	private double idf;

	public IDFResult(Word word, double idf) {
		super();
		this.word = word;
		this.idf = idf;
	}

	public Word getWord() {
		return word;
	}

	public double getIdf() {
		return idf;
	}

}

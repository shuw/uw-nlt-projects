package edu.nlt.shallow.data;

import edu.nlt.shallow.data.tags.Word;

public class WordIDF {

	private Word word;

	private double idf;

	public WordIDF(Word word, double idf) {
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

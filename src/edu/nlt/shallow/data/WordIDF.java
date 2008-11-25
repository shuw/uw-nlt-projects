package edu.nlt.shallow.data;

import edu.nlt.shallow.data.tags.Word;

public class WordIDF {

	private Word word;

	private double idf;

	private int documentCount;
	
	

	public WordIDF(Word word, double idf, int documentCount) {
		super();
		this.word = word;
		this.idf = idf;
		this.documentCount = documentCount;
	}

	public Word getWord() {
		return word;
	}

	public double getIDF() {
		return idf;
	}
	
	public int getDocumentCount() {
		return documentCount;
	}

}

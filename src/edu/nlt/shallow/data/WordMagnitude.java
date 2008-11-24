package edu.nlt.shallow.data;

import edu.nlt.shallow.data.tags.Word;

public class WordMagnitude {

	Word word;
	double magnitude;

	public WordMagnitude(Word word, double magnitude) {
		super();
		this.word = word;
		this.magnitude = magnitude;
	}

	public double getMagnitude() {
		return magnitude;
	}

	public Word getWord() {
		return word;
	}
}

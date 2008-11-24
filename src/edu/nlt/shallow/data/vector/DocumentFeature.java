package edu.nlt.shallow.data.vector;

import edu.nlt.shallow.data.tags.Word;

public class DocumentFeature {

	private Word word;

	private double strength;

	public DocumentFeature(Word word, double strength) {
		super();
		this.word = word;
		this.strength = strength;
	}

	public Word getWord() {
		return word;
	}

	public double getStrength() {
		return strength;
	}

}

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

	public double getMagnitude() {
		return strength;
	}

	public void addMagnitude(double value) {
		strength += value;
	}

	public void setMagnitude(double value) {
		strength = value;
	}
}

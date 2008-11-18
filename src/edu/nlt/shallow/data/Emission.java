package edu.nlt.shallow.data;

public class Emission<T extends Keyable> {

	private Keyable word;
	private double probability;

	public Emission(Keyable word, double probability) {
		super();
		this.word = word;
		this.probability = probability;
	}

	public double getProbability() {
		return probability;
	}

	public Keyable getWord() {
		return word;
	}

}

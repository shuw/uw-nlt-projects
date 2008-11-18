package edu.nlt.shallow.data;

import edu.nlt.shallow.data.tags.LinkedTag;

public class Transition<T extends LinkedTag> {

	private T linkedTag;

	private double probability;

	public Transition(T linkedTag, double probability) {
		super();
		this.linkedTag = linkedTag;
		this.probability = probability;
	}

	public T getLinkedTag() {
		return linkedTag;
	}

	public double getProbability() {
		return probability;
	}

}

package edu.nlt.shallow.data.tags;

import edu.nlt.shallow.data.Keyable;

public class Word implements Keyable {
	private String word;

	public Word(String word) {
		super();
		this.word = word;
	}

	@Override
	public String getKey() {
		return toString();
	}

	public String toString() {
		return word;
	}

}

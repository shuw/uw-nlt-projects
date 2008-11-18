package edu.nlt.shallow.data.tags;

import edu.nlt.shallow.data.Keyable;

public class Morpheme implements Keyable {
	private String Morpheme;

	public Morpheme(String Morpheme) {
		super();
		this.Morpheme = Morpheme;
	}

	@Override
	public String getKey() {
		return toString().toLowerCase();
	}

	public String toString() {
		return Morpheme;
	}

}

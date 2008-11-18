package edu.nlt.tools;

import edu.nlt.shallow.data.tags.Tag;

public class TagCompareResult {

	private Tag tag;
	private boolean isCorrect;

	public TagCompareResult(Tag tag, boolean isCorrect) {
		super();
		this.tag = tag;
		this.isCorrect = isCorrect;
	}

	public Tag getTag() {
		return tag;
	}

	public boolean isCorrect() {
		return isCorrect;
	}

}

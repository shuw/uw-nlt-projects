package edu.nlt.shallow.data.tags;

public class Bigram extends LinkedTag {

	public Bigram(Tag tag1, Tag tag2) {
		super(tag1, tag2);

	}

	@Override
	public String getTagKey() {
		return getKey();
	}

	public Tag getTag1() {
		return this;
	}

	public Tag getTag2() {
		return next();
	}

}
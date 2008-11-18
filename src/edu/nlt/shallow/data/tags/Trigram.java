package edu.nlt.shallow.data.tags;

public class Trigram extends LinkedTag {

	public Trigram(Tag tag1, Tag tag2, Tag tag3) {
		super(tag1, new LinkedTag(tag2, tag3));
	}

	public Tag getTag1() {
		return this;
	}

	public Tag getTag2() {
		return next();
	}

	@Override
	public String getTagKey() {
		return getKey();
	}

	public Tag getTag3() {
		return ((LinkedTag) next()).next();

	}

}

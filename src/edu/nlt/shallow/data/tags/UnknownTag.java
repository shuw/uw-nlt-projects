package edu.nlt.shallow.data.tags;

public class UnknownTag implements Tag {

	@Override
	public String getTagKey() {
		return "Unknown";
	}

	@Override
	public TagType getType() {
		return TagType.UNKNOWN;
	}

	@Override
	public String getKey() {
		return getTagKey();
	}

	@Override
	public String toString() {
		return getTagKey();
	}

	@Override
	public Object getComponent() {
		return null;
	}
}

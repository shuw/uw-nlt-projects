package edu.nlt.shallow.data.tags;

public class DefaultTag implements Tag {

	private String tag;

	public DefaultTag(String tag) {
		super();
		this.tag = tag.toUpperCase();
	}

	@Override
	public String getKey() {
		return getTagKey();
	}

	public String getTag() {
		return toString();
	}

	@Override
	public String getTagKey() {
		return toString();
	}

	@Override
	public TagType getType() {
		return TagType.NORMAL;
	}

	public String toString() {
		return tag;
	}

	@Override
	public Object getComponent() {
		return null;
	}

}

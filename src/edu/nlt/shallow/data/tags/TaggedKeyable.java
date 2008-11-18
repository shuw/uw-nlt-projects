package edu.nlt.shallow.data.tags;

import edu.nlt.shallow.data.Keyable;

public class TaggedKeyable<T extends Keyable> implements Tag {
	private T word;
	private Tag tag;

	public TaggedKeyable(Tag tag, T keyable) {
		this.tag = tag;
		this.word = keyable;

	}

	@Override
	public String getKey() {
		return getKeyable().getKey() + "/" + getTag().getTagKey();
	}

	public Keyable getKeyable() {
		return word;
	}

	public Tag getTag() {
		return tag;
	}

	@Override
	public String getTagKey() {
		return getTag().getTagKey();
	}

	@Override
	public String toString() {
		return getTagKey();
	}

	@Override
	public TagType getType() {
		return getTag().getType();
	}

	@Override
	public Object getComponent() {
		return getKeyable();
	}

}
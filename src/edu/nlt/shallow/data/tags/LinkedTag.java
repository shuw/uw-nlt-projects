package edu.nlt.shallow.data.tags;

public class LinkedTag implements Tag {

	private Tag current;
	private Tag next;

	public LinkedTag(Tag current, Tag next) {
		super();
		this.current = current;
		this.next = next;
	}

	@Override
	public String getKey() {
		return current.getTagKey() + next().getTagKey();

	}

	@Override
	public String getTagKey() {
		return current.getKey();

	}

	@Override
	public TagType getType() {
		return current.getType();
	}

	public Tag next() {
		return next;
	}

	@Override
	public Object getComponent() {
		return null;
	}

}

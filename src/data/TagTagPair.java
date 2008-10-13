package data;

public class TagTagPair implements Keyable {
	private Tag tag1;
	private Tag tag2;

	public TagTagPair(Tag tag1, Tag tag2) {
		super();
		this.tag1 = tag1;
		this.tag2 = tag2;
	}

	public Tag getTag1() {
		return tag1;
	}

	@Override
	public String getKey() {
		return getTag1().toString().toUpperCase() + " "
				+ getTag2().toString().toUpperCase();
	}

	public Tag getTag2() {
		return tag2;
	}

}
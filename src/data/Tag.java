package data;

public class Tag implements Keyable {

	private String tag;

	public Tag(String tag) {
		super();
		this.tag = tag.toUpperCase();
	}

	@Override
	public String getKey() {
		return toString();
	}

	public String toString() {
		return tag;
	}

}

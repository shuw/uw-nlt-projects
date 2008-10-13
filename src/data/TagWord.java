package data;

public class TagWord implements Keyable {
	private Word word;
	private Tag tag;

	public TagWord(Tag tag, Word word) {
		this.tag = tag;
		this.word = word;

	}

	public Tag getTag() {
		return tag;
	}

	public Word getWord() {
		return word;
	}

	@Override
	public String getKey() {
		return getWord().getKey() + "/" + getTag().getKey();
	}

}
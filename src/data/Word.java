package data;

public class Word implements Keyable {
	private String word;

	public String toString() {
		return word;
	}

	public Word(String word) {
		super();
		this.word = word;
	}

	@Override
	public String getKey() {
		return toString().toLowerCase();
	}

}

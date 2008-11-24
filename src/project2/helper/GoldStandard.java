package project2.helper;

import java.util.Hashtable;

public class GoldStandard {
	private Hashtable<String, Boolean> data = new Hashtable<String, Boolean>();

	public GoldStandard(Hashtable<String, Boolean> data) {
		super();
		this.data = data;
	}

	public boolean isLinguistic(String fileName) {
		if (!isCategorized(fileName)) {
			throw new IllegalArgumentException("uncategorized file: " + fileName);
		}

		Boolean result = data.get(fileName.toLowerCase());

		return result != null ? result : false;
	}

	public boolean isCategorized(String fileName) {
		return data.containsKey(fileName.toLowerCase());
	}
}

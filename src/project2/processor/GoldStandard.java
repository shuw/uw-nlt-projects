package project2.processor;

import java.util.Hashtable;

public class GoldStandard {
	private Hashtable<String, Boolean> data = new Hashtable<String, Boolean>();

	public GoldStandard(Hashtable<String, Boolean> data) {
		super();
		this.data = data;
	}

	public boolean isLinguistic(String fileName) {
		String name = canonizeName(fileName);
		if (!isCategorized(name)) {
			throw new IllegalArgumentException("uncategorized file: " + fileName);
		}

		Boolean result = data.get(name);

		return result != null ? result : false;
	}

	private static String canonizeName(String fileName) {
		String name;
		if (fileName.contains(".")) {
			name = fileName.split("\\.")[0];
		} else {
			name = fileName;
		}

		return name.toLowerCase();
	}

	public boolean isCategorized(String fileName) {
		String name = canonizeName(fileName);

		return data.containsKey(name);
	}
}

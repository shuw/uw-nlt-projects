package edu.nlt.ling570.project2.data;

import java.util.Hashtable;

import edu.nlt.shallow.classifier.BinaryClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;

public class ClassifierGoldStandard implements BinaryClassifier {
	private Hashtable<String, Boolean> data = new Hashtable<String, Boolean>();

	public ClassifierGoldStandard(Hashtable<String, Boolean> data) {
		super();
		this.data = data;
	}

	public boolean isPositive(String fileName) throws NotClassifiedException {
		String name = canonizeName(fileName);
		if (!isClassified(name)) {
			throw new NotClassifiedException();
		}

		Boolean result = data.get(name);

		return result != null ? result : false;
	}

	public static String canonizeName(String fileName) {
		String name;
		if (fileName.endsWith(".txt")) {

			name = fileName.substring(0, fileName.length() - 4);
		} else {
			name = fileName;
		}

		return name.toLowerCase();
	}

	public boolean isClassified(String fileName) {
		String name = canonizeName(fileName);

		return data.containsKey(name);
	}
}

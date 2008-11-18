package project2.helper;

import java.util.HashSet;

import edu.nlt.shallow.data.KeyCounterTable;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.MathUtil;

public class IDFTable {

	private KeyCounterTable<Keyable> documentFrequency = new KeyCounterTable<Keyable>();
	private int numOfDocuments = 0;

	/**
	 * @param bagOfWords
	 *            Unique words
	 */
	public void addDocument(HashSet<String> bagOfWords) {
		numOfDocuments++;
		for (String word : bagOfWords) {
			documentFrequency.add(new Word(word));
		}

	}

	public double getIDF(String word) {
		

		int count = documentFrequency.getCount(new Word(word));

		if (count == 0) {
			count = 1;
			System.err.println("Unique word found: " + word);

		}

		return MathUtil.getLogBase2((double) numOfDocuments / (double) count);

	}

}

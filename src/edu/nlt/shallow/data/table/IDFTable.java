package edu.nlt.shallow.data.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.IDFResult;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.MathUtil;

public class IDFTable {

	private KeyCounterTable<Word> documentFrequency = new KeyCounterTable<Word>();
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

	public double getIDF(Word word) {

		int count = documentFrequency.getCount(word);

		if (count == 0) {
			count = 1;
			System.err.println("Unique word found: " + word);

		}

		return MathUtil.getLogBase2((double) numOfDocuments / (double) count);

	}

	public Collection<IDFResult> getIDFResults() {

		Collection<CountHolder<Word>> wordCounts = documentFrequency.values();

		ArrayList<IDFResult> results = new ArrayList<IDFResult>(wordCounts.size());

		for (CountHolder<Word> wordCount : wordCounts) {

			results.add(new IDFResult(wordCount.getComponent(), getIDF(wordCount.getComponent())));

		}
		return results;
	}

}

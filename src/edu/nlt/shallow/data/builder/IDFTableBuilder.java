package edu.nlt.shallow.data.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.MathUtil;

public class IDFTableBuilder {

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

	private double getIDF(Word word) {

		int count = documentFrequency.getCount(word);

		if (count == 0) {
			count = 1;
			System.err.println("Unique word found: " + word);

		}
		return getIDF(numOfDocuments, count);

	}

	public static double getIDF(int documents, int occurances) {
		return MathUtil.getLogBase2((double) documents / (double) occurances);
	}

	public IDFTable build() {

		Hashtable<String, WordIDF> idfTable = new Hashtable<String, WordIDF>();

		Collection<CountHolder<Word>> wordCounts = documentFrequency.values();

		for (CountHolder<Word> wordCount : wordCounts) {

			Word word = wordCount.getComponent();

			WordIDF value = new WordIDF(word, getIDF(wordCount.getComponent()));
			idfTable.put(word.getKey(), value);

		}

		return new IDFTable(idfTable, getIDF(numOfDocuments, 1));
	}

}

package edu.nlt.shallow.data;

import java.util.HashSet;

import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.tags.Word;

public class Vocabulary {
	private IDFTable idfTable;
	private HashSet<String> vocabulary;

	public int size() {
		return vocabulary.size();
	}

	public WordIDF getWordIDF(Word word) {
		return idfTable.getWordIDF(word);
	}

	public HashSet<String> values() {
		return vocabulary;
	}

	public Vocabulary(IDFTable idfTable, HashSet<String> vocabulary) {
		super();
		if (idfTable.size() != vocabulary.size()) {
			throw new IllegalArgumentException();
		}

		this.idfTable = idfTable;
		this.vocabulary = vocabulary;
	}

	public boolean contains(Word word) {
		return vocabulary.contains(word.getKey());
	}

}

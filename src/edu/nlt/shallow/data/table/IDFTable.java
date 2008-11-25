package edu.nlt.shallow.data.table;

import java.util.Collection;
import java.util.Hashtable;

import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.Globals;

public class IDFTable {

	private Hashtable<String, WordIDF> table;

	public IDFTable(Hashtable<String, WordIDF> table) {
		super();
		this.table = table;

	}

	public int size() {
		return table.size();
	}

	public WordIDF getWordIDF(Word word) {
		WordIDF result = table.get(word.getKey());
		if (result != null) {
			return result;
		} else {
			if (Globals.IsDebugEnabled) {
				return null;
			} else {
				return new WordIDF(word, 0, 0);
			}
		}

	}

	public Collection<WordIDF> values() {
		return table.values();
	}
}

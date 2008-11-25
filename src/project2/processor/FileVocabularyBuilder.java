package project2.processor;

import java.util.HashSet;
import java.util.Hashtable;

import project2.data.Vocabulary;
import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.processor.LineProcessor;

public class FileVocabularyBuilder implements LineProcessor {
	private Hashtable<String, WordIDF> table = new Hashtable<String, WordIDF>();
	private HashSet<String> vocabulary = new HashSet<String>();

	private int maxVocabularySize = -1;

	public FileVocabularyBuilder(int maxVocabularySize) {
		super();
		this.maxVocabularySize = maxVocabularySize;
	}

	@Override
	public void processLine(String value) {
		if (maxVocabularySize != -1 && vocabulary.size() >= maxVocabularySize) {
			return;
		}

		String[] components = value.split("\t");
		if (components.length >= 2) {
			Word word = new Word(components[0]);

			double idfValue = Double.parseDouble(components[1]);

			vocabulary.add(word.getKey());
			table.put(word.getKey(), new WordIDF(word, idfValue, 0));
		} else if (!"".equals(value)) {
			(new Exception("Bad input:  " + value)).printStackTrace(System.err);
		}

	}

	public Vocabulary build() {
		return new Vocabulary(new IDFTable(table), vocabulary);
	}
}

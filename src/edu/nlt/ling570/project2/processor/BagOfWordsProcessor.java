package edu.nlt.ling570.project2.processor;

import java.util.HashSet;

import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.processor.WordProcessor;

/**
 * Creates bag of word
 * 
 * @author shu
 * 
 */
public class BagOfWordsProcessor implements WordProcessor {

	private HashSet<String> data = new HashSet<String>();

	@Override
	public void processWord(Word word) {

		data.add(word.toString());

	}

	public HashSet<String> getWords() {
		return data;
	}

}

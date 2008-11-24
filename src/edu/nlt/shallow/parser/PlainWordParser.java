package edu.nlt.shallow.parser;

import java.util.ArrayList;
import java.util.Collection;

import edu.nlt.shallow.data.tags.Word;

public class PlainWordParser implements WordParser {

	@Override
	public Collection<Word> getWords(String value) throws ParserException {
		value = value.replaceAll("[^\\w\\s]+", ""); // remove punctuation

		String[] wordStrs = value.split("\\s");

		ArrayList<Word> words = new ArrayList<Word>(wordStrs.length);
		for (String word : wordStrs) {

			if (!"".equals(word)) {
				words.add(new Word(word));
			}
		}
		return words;

	}

}

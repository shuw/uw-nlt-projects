package edu.nlt.shallow.parser;

import java.util.ArrayList;
import java.util.Collection;

import edu.nlt.external.PorterStemmer;
import edu.nlt.shallow.data.tags.Word;

public class PlainWordParser implements WordParser {
	private PorterStemmer stemmer = new PorterStemmer();

	private static final boolean useStemmer = false;

	private static final boolean removeNumbersAndSingleLetters = false;

	@Override
	public Collection<Word> getWords(String value) throws ParserException {
		value = value.replaceAll("[^\\w\\s]+", ""); // remove punctuation

		String[] wordStrs = value.split("\\s");

		ArrayList<Word> words = new ArrayList<Word>(wordStrs.length);
		for (String word : wordStrs) {

			if ((removeNumbersAndSingleLetters && word.length() > 1 && !word.matches("[0-9]+"))
					|| (!removeNumbersAndSingleLetters && word.length() > 0)) {

				if (useStemmer) {
					word = stemmer.stripAffixes(word);
				}

				words.add(new Word(word));
			}

		}
		return words;

	}

}

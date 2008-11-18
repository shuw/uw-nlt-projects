package edu.nlt.shallow.parser;

import java.util.Collection;

import edu.nlt.shallow.data.tags.Word;

public interface WordParser {

	public Collection<Word> getWords(String value) throws ParserException;
}

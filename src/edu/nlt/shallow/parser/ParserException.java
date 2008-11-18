package edu.nlt.shallow.parser;

public class ParserException extends Exception {

	/**
	 * Initial version
	 */
	private static final long serialVersionUID = 1L;

	public ParserException(String value) {
		super("Could not parse: " + value);
	}

}

package edu.nlt.shallow.parser;

import java.util.Collection;

import edu.nlt.shallow.data.tags.Tag;

/**
 * 
 * Gets Tags from Line
 * 
 * @author shu
 * 
 */
public interface TagParser {

	/**
	 * @param value
	 *            Line of input text
	 * @return Colleciton of Tags
	 */
	public Collection<Tag> getTags(String value) throws ParserException;
}

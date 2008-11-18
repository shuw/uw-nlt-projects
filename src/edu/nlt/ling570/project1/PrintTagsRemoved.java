package edu.nlt.ling570.project1;

import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;

import edu.nlt.formatter.LDC2004T03Formatter;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.LDC2004T03Parser;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;

public class PrintTagsRemoved {

	/**
	 * Removes Tag Type information from Standard input
	 * 
	 * @param args
	 *            args[0] - Training file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		// Get gold tags from Input
		//
		Collection<Tag> goldTags = getTags(System.in);

		// Get tags using viterbi
		//
		printTagsRemoved(goldTags);

	}

	/**
	 * Prints Tags without TagType information
	 */
	public static void printTagsRemoved(Collection<Tag> inputTags) {

		LDC2004T03Formatter formatter = new LDC2004T03Formatter();

		for (Tag tag : inputTags) {

			// Converts tag to string without Tag information
			//
			String detaggedString = formatter.print(tag, true, true);

			System.out.print(detaggedString);

		}
	}

	/**
	 * Gets tags from file
	 */
	public static Collection<Tag> getTags(InputStream in) {
		final LinkedList<Tag> tags = new LinkedList<Tag>();
		InputUtil.process(in, new LineProcessor() {
			private TagParser parser1 = new LDC2004T03Parser();

			@Override
			public void processLine(String value1) {
				try {
					Collection<Tag> tags1 = parser1.getTags(value1);
					for (Tag tag : tags1) {
						tags.add(tag);
					}

				} catch (ParserException e) {
					e.printStackTrace();
				}
			}

		});
		return tags;
	}
}

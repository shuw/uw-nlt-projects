package edu.nlt.ling570.project1;

import java.util.Collection;

import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;

/**
 * Prints average Morphemes per Word
 * 
 * @author shu
 * 
 */
public class PrintMorphemePW {

	static int morphemeCount = 0;
	static int wordCount = 0;

	/**
	 * Train on StandardInput
	 */
	public static void main(String[] args) {

		final TagParser parser = new LDC2004T03Parser();

		InputUtil.process(System.in, new LineProcessor() {

			@Override
			public void processLine(String value) {

				try {

					Collection<Tag> tags = parser.getTags(value);

					// Test
					for (Tag tag : tags) {

						switch (tag.getType()) {
						case NORMAL:
							morphemeCount++;
							break;
						case END_OF_WORD:
							wordCount++;
							break;

						}
					}

				} catch (ParserException e) {
					e.printStackTrace();
				}
			}

		});

		System.out.println("Average morphemes / word: " + (double) morphemeCount
				/ (double) wordCount);
	}

}

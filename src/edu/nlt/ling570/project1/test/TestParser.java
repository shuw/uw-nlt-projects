package edu.nlt.ling570.project1.test;

import java.util.Collection;

import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.LDC2004T03Parser;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;

/**
 * Tests LDC2004T03Parser praser
 * 
 * @author shu
 * 
 */
public class TestParser {
	public static void main(String[] args) {

		final TagParser parser = new LDC2004T03Parser();
		InputUtil.process(System.in, new LineProcessor() {

			@Override
			public void processLine(String value) {

				try {
					Collection<Tag> tags = parser.getTags(value);

					// Test
					for (Tag tag : tags) {
						System.out.println(tag.getKey());
					}

				} catch (ParserException e) {
					e.printStackTrace();
				}
			}

		});
	}
}

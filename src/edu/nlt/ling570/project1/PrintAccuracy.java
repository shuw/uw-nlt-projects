package edu.nlt.ling570.project1;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.tools.CompareResult;
import edu.nlt.tools.TagComparer;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineCompareProcessor;
import edu.nlt.util.Singletons;

public class PrintAccuracy {

	/**
	 * @param args
	 *            args[0] Test output
	 * 
	 *            args[1] Gold standard
	 */
	public static void main(String[] args) {

		File testOutput = new File(args[0]);
		File goldStandard = new File(args[1]);

		final LinkedList<Tag> testTags = new LinkedList<Tag>();
		final LinkedList<Tag> goldTags = new LinkedList<Tag>();

		InputUtil.compareFiles(testOutput, goldStandard, new LineCompareProcessor() {
			private TagParser parser1 = new LDC2004T03Parser();
			private TagParser parser2 = new LDC2004T03Parser();

			@Override
			public void processLine(String value1, String value2) {
				try {
					Collection<Tag> tags1 = parser1.getTags(value1);
					for (Tag tag : tags1) {
						testTags.add(tag);
					}

					Collection<Tag> tags2 = parser2.getTags(value2);
					for (Tag tag : tags2) {
						goldTags.add(tag);
					}

				} catch (ParserException e) {
					e.printStackTrace();
				}
			}

		});

		if (testTags.size() != goldTags.size()) {
			System.out.println("Size mismatch: " + testTags.size() + " " + goldTags.size());
		}

		CompareResult result = TagComparer.compare(testTags, goldTags);

		System.out.println();
		System.out.println("Total # of morphemes evaluated:\t" + result.getMorphemeCount());
		System.out.println("Accuracy:\t"
				+ Singletons.PercentageFormatter.format(result.getAccuracy()));

	}
}

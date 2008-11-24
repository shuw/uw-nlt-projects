package edu.nlt.ling570.project1;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

import edu.nlt.formatter.LDC2004T03Formatter;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.LDC2004T03Parser;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.tools.CompareResult;
import edu.nlt.tools.TagCompareResult;
import edu.nlt.tools.TagComparer;
import edu.nlt.util.InputUtil;
import edu.nlt.util.Singletons;
import edu.nlt.util.processor.LineProcessor;

public class EvaluateOriginal {

	/**
	 * Prints tags for Standard Input
	 * 
	 * @param args
	 *            args[0] - Training file
	 * 
	 *            args[1] - File to tag
	 * 
	 *            args[2] - Gold standard tagged file
	 */
	public static void main(String[] args) {

		File trainingFile = new File(args[0]);

		File inputFile = new File(args[1]);

		File taggedFile = new File(args[2]);

		// tags from Viterbi algorithm
		Collection<Tag> tags = ViterbiHelper.getTags(trainingFile, inputFile);

		// tags from tagged file
		Collection<Tag> goldTags = getTags(taggedFile);

		// compares tags
		CompareResult compareResult = TagComparer.compare(tags, goldTags);

		printOutput(System.out, compareResult);
	}

	public static void printOutput(PrintStream out, CompareResult compareResult) {

		LDC2004T03Formatter formatter = new LDC2004T03Formatter();

		for (TagCompareResult tag : compareResult.getTagResults()) {
			System.out.print(formatter.print(tag));
		}

		System.out.println();
		System.out.println("Total # of morphemes evaluated:\t" + compareResult.getMorphemeCount());
		System.out.println("Accuracy:\t"
				+ Singletons.PercentageFormatter.format(compareResult.getAccuracy()));

	}

	/**
	 * Gets tags from file
	 */
	public static Collection<Tag> getTags(File taggedFile) {
		final LinkedList<Tag> tags = new LinkedList<Tag>();
		InputUtil.process(taggedFile, new LineProcessor() {
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

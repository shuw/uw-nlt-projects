package edu.nlt.ling570.project1;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;

import edu.nlt.algorithm.Tagger;
import edu.nlt.formatter.LDC2004T03Formatter;
import edu.nlt.ling570.project1.processor.TaggerProcessor;
import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.tools.CompareResult;
import edu.nlt.tools.TagCompareResult;
import edu.nlt.tools.TagComparer;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;
import edu.nlt.util.Singletons;

public class Evaluate {

	/**
	 * Evaluates standard input
	 * 
	 * @param args
	 *            args[0] - Training file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		File trainingFile = new File(args[0]);

		// Get gold tags from Input
		//
		Collection<Tag> goldTags = getTags(System.in);

		// Construct model and viterbi tagger from training file
		//
		Tagger viterbiTagger = ViterbiHelper.getViterbiTagger(trainingFile);

		// Get tags using viterbi
		//
		Collection<Tag> viterbiTags = getTags(viterbiTagger, goldTags);

		// Compare results
		//
		CompareResult compareResult = TagComparer.compare(viterbiTags, goldTags);

		printOutput(System.out, compareResult);
	}

	/**
	 * Strips input tags of tag information and run their components through
	 * Tagger
	 * 
	 * @param tagger
	 *            Tagger to use
	 */
	public static Collection<Tag> getTags(Tagger tagger, Collection<Tag> inputTags) {
		TaggerProcessor taggerProcessor = new TaggerProcessor(tagger);
		LDC2004T03Formatter formatter = new LDC2004T03Formatter();

		// Used to hold a line worth of text
		StringBuilder lineBuilder = new StringBuilder();

		for (Tag tag : inputTags) {

			// Removes tag type information, into string
			String detaggedString = formatter.print(tag, true, true);

			if (detaggedString.equals(LDC2004T03Formatter.newline)) {

				// Process line of text
				taggerProcessor.processLine(lineBuilder.toString());
				lineBuilder = new StringBuilder();
			} else {
				lineBuilder.append(detaggedString);
			}

		}

		return taggerProcessor.getTotalTags();
	}

	public static void printOutput(PrintStream out, CompareResult compareResult) {

		LDC2004T03Formatter formatter = new LDC2004T03Formatter();

		for (TagCompareResult tag : compareResult.getTagResults()) {
//			System.out.print(formatter.print(tag));
		}

		System.out.println();
		System.out.println("Total # of morphemes evaluated:\t" + compareResult.getMorphemeCount());
		System.out.println("Accuracy:\t"
				+ Singletons.PercentageFormatter.format(compareResult.getAccuracy()));

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

package hw2;

import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.List;

import util.PTBParser;
import util.StringProcessor;
import util.Util;
import data.CounterTable;
import data.EmissionTable;
import data.Tag;
import data.TagTagPair;
import data.TagWord;
import data.Word;
import data.WordTableDecorator;

public class hw2b {
	/**
	 * @param args
	 *            Path of input files
	 */
	public static void main(String[] args) {

		final CounterTable<TagTagPair> transitionTable = new CounterTable<TagTagPair>();
		final CounterTable<Tag> tagTable = new CounterTable<Tag>();
		final CounterTable<Word> wordTable = new CounterTable<Word>();

		final EmissionTable emissionTable = new EmissionTable();

		Util.processFiles(args[0], new StringProcessor() {

			private Tag prevTag;

			@Override
			public void processLine(String value) {
				Collection<TagWord> tags = PTBParser.parseTags(value);
				for (TagWord tagWord : tags) {
					emissionTable.Add(tagWord);

					Tag tag = tagWord.getTag();
					tagTable.add(tag);
					wordTable.add(tagWord.getWord());

					if (prevTag != null) {
						TagTagPair pair = new TagTagPair(prevTag, tag);
						transitionTable.add(pair);
					}

					prevTag = tag;
				}
			}

		});
		transitionTable.printEntries(System.out, 20);

		System.out.println();
		printMarkoxTransitionTable(System.out, tagTable.getTop(10, true),
				transitionTable);

		System.out.println();
		printEmissionMatrix(System.out, wordTable.getTop(20, false),
				emissionTable);
	}

	private static DecimalFormat formatter = new DecimalFormat("0.000000");

	private static void printEmissionMatrix(PrintStream out, List<Word> words,
			EmissionTable table) {

		// print first row
		for (Tag tag : table.getTags()) {
			out.print(tag.toString() + "\t");
		}
		out.println();

		Collection<Tag> tags = table.getTags();

		for (Word word : words) {
			out.print(word.getKey());

			for (Tag tag : tags) {

				WordTableDecorator<Tag> wordTable = table.getWordTable(tag);

				double prob = (double) wordTable.getCount(word)
						/ (double) wordTable.getTotalCount();

				double probBase2 = Util.toLogBase2(prob);

				out.print("\t"
						+ (probBase2 != Double.NEGATIVE_INFINITY ? formatter
								.format(probBase2) : "-Inf"));

			}
			out.println();
		}

	}

	private static void printMarkoxTransitionTable(PrintStream out,
			List<Tag> tags, CounterTable<TagTagPair> transitionTable) {

		// print first row
		out.print("\t");
		for (Tag tag : tags) {
			out.print(" " + tag.toString() + "        ");
		}
		out.println();

		for (Tag tag1 : tags) {

			out.print(tag1.toString() + "\t");

			int total = 0;
			for (Tag tag2 : tags) {
				total += transitionTable.getCount(new TagTagPair(tag1, tag2));
			}

			for (Tag tag2 : tags) {

				double prob = (double) transitionTable.getCount(new TagTagPair(
						tag1, tag2))
						/ (double) total;

				double probBase2 = Util.toLogBase2(prob);

				out.print(" "
						+ (probBase2 != Double.NEGATIVE_INFINITY ? formatter
								.format(probBase2) : "-Inf      "));
			}

			out.println();
		}

	}
}

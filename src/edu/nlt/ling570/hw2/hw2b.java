package edu.nlt.ling570.hw2;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

import edu.nlt.shallow.data.EmissionTable;
import edu.nlt.shallow.data.EmissionTableBuilder;
import edu.nlt.shallow.data.KeyCounterTable;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.TransitionTable;
import edu.nlt.shallow.data.TransitionTableBuilder;
import edu.nlt.shallow.data.tags.Bigram;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;
import edu.nlt.util.MathUtil;
import edu.nlt.util.PTBParser;
import edu.nlt.util.Singletons;

public class hw2b {
	private static void printEmissionMatrix(PrintStream out, List<Keyable> words,
			EmissionTableBuilder table) {

		// print first row
		for (Tag tag : table.getTags()) {
			out.print(tag.toString() + "\t");
		}
		out.println();

		Collection<Tag> tags = table.getTags();

		for (Keyable word : words) {
			out.print(word.getKey());

			for (Tag tag : tags) {

				EmissionTable wordTable = table.getEmissionTable();

				double prob = wordTable.getEmission(tag, word).getProbability();

				double probBase2 = MathUtil.getLogBase2(prob);

				out.print("\t"
						+ (probBase2 != Double.NEGATIVE_INFINITY ? Singletons.FractionFormatter
								.format(probBase2) : "-Inf"));

			}
			out.println();
		}

	}

	private static void printMarkoxTransitionTable(PrintStream out, List<Tag> tags,
			TransitionTable transitionTable) {

		// print first row
		out.print("\t");
		for (Tag tag : tags) {
			out.print(" " + tag.toString() + "        ");
		}
		out.println();

		for (Tag tag1 : tags) {

			out.print(tag1.toString() + "\t");

			for (Tag tag2 : tags) {

				double prob = transitionTable.getTransition(new Bigram(tag1, tag2))
						.getProbability();

				double probBase2 = MathUtil.getLogBase2(prob);

				out.print(" "
						+ (probBase2 != Double.NEGATIVE_INFINITY ? Singletons.FractionFormatter
								.format(probBase2) : "-Inf      "));
			}

			out.println();
		}

	}

	/**
	 * @param args
	 *            Path of input files
	 */
	public static void main(String[] args) {

		final TransitionTableBuilder transitionTable = new TransitionTableBuilder(null);
		final KeyCounterTable<Keyable> wordTable = new KeyCounterTable<Keyable>();

		final EmissionTableBuilder emissionTable = new EmissionTableBuilder();

		InputUtil.processFiles(args[0], new LineProcessor() {

			@Override
			public void processLine(String value) {
				Collection<TaggedKeyable<Word>> tags = PTBParser.parseTags(value);
				for (TaggedKeyable<Word> tagWord : tags) {
					emissionTable.Add(tagWord);

					wordTable.add(tagWord.getKeyable());

					transitionTable.addNext(tagWord.getTag());

				}
			}

		});

		System.out.println();
		printMarkoxTransitionTable(System.out, transitionTable.getTagCounters().getTop(10, true),
				transitionTable.getBigramTable());

		System.out.println();
		printEmissionMatrix(System.out, wordTable.getTop(20, false), emissionTable);
	}
}

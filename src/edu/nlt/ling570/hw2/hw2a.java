package edu.nlt.ling570.hw2;

import java.util.Collection;

import edu.nlt.shallow.data.KeyCounterTable;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;
import edu.nlt.util.PTBParser;

public class hw2a {

	/**
	 * @param args
	 *            Path of input files
	 */
	public static void main(String[] args) {

		final KeyCounterTable<TaggedKeyable<Word>> table = new KeyCounterTable<TaggedKeyable<Word>>();

		InputUtil.processFiles(args[0], new LineProcessor() {

			@Override
			public void processLine(String value) {
				Collection<TaggedKeyable<Word>> tags = PTBParser.parseTags(value);
				for (TaggedKeyable<Word> tag : tags) {
					table.add(tag);
				}
			}

		});
		table.printEntries(System.out, 20);

	}

}

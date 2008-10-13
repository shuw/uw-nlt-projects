package hw2;

import java.util.Collection;

import util.PTBParser;
import util.StringProcessor;
import util.Util;
import data.CounterTable;
import data.TagWord;

public class hw2a {

	/**
	 * @param args
	 *            Path of input files
	 */
	public static void main(String[] args) {

		final CounterTable<TagWord> table = new CounterTable<TagWord>();

		Util.processFiles(args[0], new StringProcessor() {

			@Override
			public void processLine(String value) {
				Collection<TagWord> tags = PTBParser.parseTags(value);
				for (TagWord tag : tags) {
					table.add(tag);
				}
			}

		});
		table.printEntries(System.out, 20);

	}

}

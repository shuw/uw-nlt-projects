package project2.helper;

import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.LineProcessor;

public class WordCountProcessor implements LineProcessor {
	private KeyCounterTable<Word> counter = new KeyCounterTable<Word>();

	private PlainWordParser wordParser = new PlainWordParser();

	@Override
	public void processLine(String value) {

		try {
			for (Word word : wordParser.getWords(value)) {
				counter.add(word);
			}
		} catch (ParserException e) {
			e.printStackTrace(System.err);
		}
	}

	public KeyCounterTable<Word> getCounter() {
		return counter;
	}

}

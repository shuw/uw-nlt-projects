package project2.helper;

import edu.nlt.shallow.data.KeyCounterTable;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.LineProcessor;

public class WordCountProcessor implements LineProcessor {
	private KeyCounterTable<Keyable> counter = new KeyCounterTable<Keyable>();

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

	public KeyCounterTable<Keyable> getCounter() {
		return counter;
	}

}

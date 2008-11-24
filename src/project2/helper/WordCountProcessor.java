package project2.helper;

import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.processor.WordProcessor;

public class WordCountProcessor implements WordProcessor {
	private KeyCounterTable<Word> counter = new KeyCounterTable<Word>();

	@Override
	public void processWord(Word value) {
		counter.add(value);
	}

	public KeyCounterTable<Word> getCounter() {
		return counter;
	}

}

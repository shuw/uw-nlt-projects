package edu.nlt.ling570.project2.processor;

import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.parser.WordTokenizer;
import edu.nlt.util.processor.LineProcessor;
import edu.nlt.util.processor.WordProcessor;

public class PlainWordProcessor implements LineProcessor {
	private WordProcessor wordProcessor;
	private WordTokenizer parser = new WordTokenizer(false);

	public PlainWordProcessor(WordProcessor wordProcessor) {
		super();
		this.wordProcessor = wordProcessor;
	}

	@Override
	public void processLine(String value) {

		for (Word word : parser.getWords(value)) {
			wordProcessor.processWord(word);
		}

	}

}

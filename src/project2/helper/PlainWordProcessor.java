package project2.helper;

import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.processor.LineProcessor;
import edu.nlt.util.processor.WordProcessor;

public class PlainWordProcessor implements LineProcessor {
	private WordProcessor wordProcessor;
	private PlainWordParser parser = new PlainWordParser();

	public PlainWordProcessor(WordProcessor wordProcessor) {
		super();
		this.wordProcessor = wordProcessor;
	}

	@Override
	public void processLine(String value) {

		try {
			for (Word word : parser.getWords(value)) {
				wordProcessor.processWord(word);
			}
		} catch (ParserException e) {
			e.printStackTrace(System.err);
		}

	}

}

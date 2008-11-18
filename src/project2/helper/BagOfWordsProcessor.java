package project2.helper;

import java.util.HashSet;

import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.LineProcessor;

/**
 * Creates bag of word
 * 
 * @author shu
 * 
 */
public class BagOfWordsProcessor implements LineProcessor {

	private HashSet<String> data = new HashSet<String>();
	private PlainWordParser wordParser = new PlainWordParser();

	@Override
	public void processLine(String value) {

		try {
			for (Word word : wordParser.getWords(value)) {
				data.add(word.toString());
			}
		} catch (ParserException e) {
			e.printStackTrace(System.err);
		}
	}

	public HashSet<String> getWords() {
		return data;
	}

}

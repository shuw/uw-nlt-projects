package project2.processor;

import java.util.Hashtable;

import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.processor.LineProcessor;

public class FileIDFBuilder implements LineProcessor {
	private Hashtable<String, WordIDF> table = new Hashtable<String, WordIDF>();
	private double smoothingNullValue = Double.NEGATIVE_INFINITY;

	@Override
	public void processLine(String value) {

		if (smoothingNullValue == Double.NEGATIVE_INFINITY) {
			smoothingNullValue = Double.parseDouble(value);
		} else {
			String[] components = value.split("\t");
			if (components.length == 2) {
				Word word = new Word(components[0]);

				double idfValue = Double.parseDouble(components[1]);

				table.put(word.getKey(), new WordIDF(word, idfValue));
			} else if (!"".equals(value)) {
				(new Exception("Bad input:  " + value)).printStackTrace(System.err);
			}
		}
	}

	public IDFTable build() {
		return new IDFTable(table, smoothingNullValue);
	}
}

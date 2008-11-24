package project2;

import java.io.File;
import java.util.Hashtable;

import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;

public class PrintVector {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] IDFTable
	 * 
	 *            args[1] Path to file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		IDFTable idfTable = readIDFTable(new File(args[0]));
		
		
		

	}

	private static IDFTable readIDFTable(File file) {
		FileIDFBuilder builder = new FileIDFBuilder();
		InputUtil.process(file, builder);
		return builder.build();

	}
}

class FileIDFBuilder implements LineProcessor {
	private Hashtable<String, WordIDF> table = new Hashtable<String, WordIDF>();
	private double smoothingNullValue = Double.NaN;

	@Override
	public void processLine(String value) {

		if (smoothingNullValue == Double.NaN) {
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

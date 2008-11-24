package project2;

import java.io.File;
import java.util.Collection;

import project2.helper.BagOfWordsProcessor;
import edu.nlt.shallow.data.IDFResult;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.util.FileUtil;
import edu.nlt.util.InputUtil;

public class PrintIDFTable {

	/**
	 * @param args
	 * 
	 *            args[0] Location of files
	 */
	public static void main(String[] args) {

		IDFTable idfTable = getIDFTable(FileUtil.getFiles(args[0], true));

		printIDFTable(idfTable);
	}

	private static void printIDFTable(IDFTable table) {

		
		
		for (IDFResult word : table.getIDFResults()) {

		}
	}

	private static IDFTable getIDFTable(Collection<File> files) {

		IDFTable idfTable = new IDFTable();

		for (File file : files) {
			BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
			InputUtil.process(file, bagOfWordsProcessor);
			idfTable.addDocument(bagOfWordsProcessor.getWords());
		}

		return idfTable;
	}
}

package project2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import project2.helper.BagOfWordsProcessor;
import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.builder.IDFTableBuilder;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;

public class PrintIDFTable {

	/**
	 * Print sorted IDF Table
	 * 
	 * @param args
	 * 
	 *            args[0] Location of files
	 */
	public static void main(String[] args) {

		IDFProcessor processor = new IDFProcessor();
		InputUtil.processFiles(args[0], processor);

		processor.printResults(-1);
	}

}

class IDFProcessor implements FileProcessor {
	private IDFTableBuilder idfTableBuilder = new IDFTableBuilder();

	@Override
	public void processFile(File file) {
		BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
		InputUtil.process(file, bagOfWordsProcessor);
		idfTableBuilder.addDocument(bagOfWordsProcessor.getWords());

	}

	public void printResults(int maxNumOfResults) {

		IDFTable idfTable = idfTableBuilder.build();

		List<WordIDF> list = new ArrayList<WordIDF>(idfTable.values());
		Collections.sort(list, new Comparator<WordIDF>() {

			@Override
			public int compare(WordIDF o1, WordIDF o2) {

				return Double.compare(o1.getIdf(), o2.getIdf());
			}

		});

		System.out.println(idfTable.getSmoothingNullValue());

		int count = 0;
		for (WordIDF word : list) {
			if (maxNumOfResults != -1 && ++count > maxNumOfResults) {
				break;
			}

			System.out.println(word.getWord() + "\t" + word.getIdf());
		}
	}
}
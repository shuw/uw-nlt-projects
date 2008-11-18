package project2;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import project2.helper.BagOfWordsProcessor;
import project2.helper.GoldStandard;
import project2.helper.GoldStandardProcessor;
import project2.helper.IDFTable;
import project2.helper.WordCountProcessor;
import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.KeyCounterTable;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.FileUtil;
import edu.nlt.util.Globals;
import edu.nlt.util.InputUtil;

public class PrintVocabulary {
	/**
	 * @param args
	 * 
	 *            args[0] Path to Input files
	 * 
	 *            args[1] GoldStandard file
	 */
	public static void main(String[] args) {

		GoldStandardProcessor processor = new GoldStandardProcessor();

		// Parse GoldStandardFile
		//
		InputUtil.process(new File(args[1]), processor);

		String inputFilesPath = args[0];

		processFiles(FileUtil.getFiles(inputFilesPath, true), processor.getGoldStandard());
	}

	private static void processFiles(Collection<File> files, GoldStandard standard) {

		IDFTable idfTable = new IDFTable();

		KeyCounterTable<Keyable> lingCounter;
		KeyCounterTable<Keyable> nonLingCounter;
		{
			// Get word counts
			//
			WordCountProcessor lingProcessor = new WordCountProcessor();
			WordCountProcessor nonLingProcessor = new WordCountProcessor();

			int count = 0;
			for (File file : files) {

				// remove file suffix
				String canonicalName = file.getName().replaceFirst(".txt*", "");
				if (Globals.IsDebugEnabled) {
					System.out.println("processing file: " + file.getName());

					// Only process 100 files to speed up development
					if (count > 100)
						break;

				}

				if (standard.isCategorized(canonicalName)) {
					BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
					InputUtil.process(file, bagOfWordsProcessor);
					idfTable.addDocument(bagOfWordsProcessor.getWords());

					if (standard.isLinguistic(canonicalName)) {

						InputUtil.process(file, lingProcessor);
					} else {
						InputUtil.process(file, nonLingProcessor);
					}

				} else {
					System.err.println("Uncategorized file: " + canonicalName);
				}
				count++;
			}

			lingCounter = lingProcessor.getCounter();
			nonLingCounter = nonLingProcessor.getCounter();
		}

		Hashtable<String, Double> lingTfIdfTable;
		Hashtable<String, Double> nonlingTfIdfTable;
		{
			// calculate global TFIDF
			//

			lingTfIdfTable = getGlobalTfIdf(idfTable, lingCounter);
			nonlingTfIdfTable = getGlobalTfIdf(idfTable, nonLingCounter);
		}

		ArrayList<WordDoublePair> list = new ArrayList<WordDoublePair>(lingTfIdfTable.size());
		{
			for (String word : lingTfIdfTable.keySet()) {

				double lingTfIdf = lingTfIdfTable.get(word);

				Double nonLingTfIdf = nonlingTfIdfTable.get(word);
				if (nonLingTfIdf != null) {

					WordDoublePair x = new WordDoublePair(word, Math.abs(lingTfIdf - nonLingTfIdf));
					list.add(x);

				}

			}
		}

		Collections.sort(list, new Comparator<WordDoublePair>() {

			@Override
			public int compare(WordDoublePair o1, WordDoublePair o2) {
				return Double.compare(o2.getValue(), o1.getValue());
			}

		});

		int count = 0;
		for (WordDoublePair wordValue : list) {

			String word = wordValue.getWord();
			System.out.println();
			System.out.print(word);

			System.out.print("\t\t" + lingCounter.getCount(new Word(word)));
			System.out.print("\t\t" + nonLingCounter.getCount(new Word(word)));

			if (++count > 500) {
				break;
			}
		}

		// System.out.println("\n\nTop linguistic words");
		// // Print top linguistic words
		// for (Keyable word : lingProcessor.getCounter().getTop(200, false)) {
		// System.out.println(word);
		// }
		//
		// System.out.println("\n\nTop non-linguistic words");
		// // Print top linguistic words
		// for (Keyable word : nonLingProcessor.getCounter().getTop(200, false))
		// {
		// System.out.println(word);
		// }

	}

	private static Hashtable<String, Double> getGlobalTfIdf(IDFTable idfTable,
			KeyCounterTable<Keyable> counterTable) {
		Hashtable<String, Double> TfIdfTable = new Hashtable<String, Double>();

		// int count = 0;
		for (CountHolder<Keyable> wordCounter : counterTable.getReverseSorted()) {

			String word = wordCounter.getComponent().toString();
			double tf = (double) wordCounter.getCount() / (double) counterTable.getTotalCount();

			double tfIdf = tf * idfTable.getIDF(word);

			// if (count++ < 500) {
			// System.out.println(word + " " + tfIdf);
			// }

			TfIdfTable.put(word, tfIdf);
		}

		return TfIdfTable;
	}

	private static class WordDoublePair {

		String word;
		double value;

		public WordDoublePair(String word, double value) {
			super();
			this.word = word;
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public String getWord() {
			return word;
		}
	}
}

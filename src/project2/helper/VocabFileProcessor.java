package project2.helper;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.Globals;
import edu.nlt.util.InputUtil;
import edu.nlt.util.Singletons;

public class VocabFileProcessor implements FileProcessor {
	private GoldStandard goldStandard;
	private IDFTable idfTable = new IDFTable();

	private WordCountProcessor lingProcessor = new WordCountProcessor();
	private WordCountProcessor nonLingProcessor = new WordCountProcessor();

	private int fileProcessedCount;

	public VocabFileProcessor(GoldStandard goldStandard) {
		super();
		this.goldStandard = goldStandard;
	}

	@Override
	public void processFile(File file) {
		// remove file suffix
		String canonicalName = file.getName().replaceFirst(".txt*", "");

		// if (Globals.IsDebugEnabled) {
		// Only process 100 files to speed up development
		// if (fileProcessedCount > 100)
		// break;
		// }

		if (goldStandard.isCategorized(canonicalName)) {
			BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
			InputUtil.process(file, bagOfWordsProcessor);
			idfTable.addDocument(bagOfWordsProcessor.getWords());

			if (goldStandard.isLinguistic(canonicalName)) {

				InputUtil.process(file, lingProcessor);
			} else {
				InputUtil.process(file, nonLingProcessor);
			}

		} else {
			System.err.println("Uncategorized file: " + canonicalName);
		}
		fileProcessedCount++;

	}

	public void printResult(PrintStream out, int maxSizeOfVocab) {

		KeyCounterTable<Word> lingCounter = lingProcessor.getCounter();
		KeyCounterTable<Word> nonLingCounter = nonLingProcessor.getCounter();

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

					WordDoublePair x = new WordDoublePair(new Word(word), Math.abs(lingTfIdf
							- nonLingTfIdf));
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

			Word word = wordValue.getWord();
			System.out.println();
			System.out.print(word);

			double IDF = idfTable.getIDF(word);
			{
				int lingWordCount = lingCounter.getCount(word);

				System.out.print("\t\t" + lingWordCount);
				System.out.print("/"
						+ Singletons.FractionFormatter.format((double) lingWordCount * IDF));
			}

			{
				int nonLingWordCount = nonLingCounter.getCount(word);
				System.out.print("\t\t" + nonLingWordCount);
				System.out.print("/"
						+ Singletons.FractionFormatter.format((double) nonLingWordCount * IDF));
			}

			if (++count > maxSizeOfVocab) {
				break;
			}
		}
	}

	private static Hashtable<String, Double> getGlobalTfIdf(IDFTable idfTable,
			KeyCounterTable<Word> counterTable) {
		Hashtable<String, Double> TfIdfTable = new Hashtable<String, Double>();

		// int count = 0;
		for (CountHolder<Word> wordCounter : counterTable.getReverseSorted()) {

			Word word = wordCounter.getComponent();
			double tf = (double) wordCounter.getCount() / (double) counterTable.getTotalCount();

			double tfIdf = tf * idfTable.getIDF(word);

			// if (count++ < 500) {
			// System.out.println(word + " " + tfIdf);
			// }

			TfIdfTable.put(word.getKey(), tfIdf);
		}

		return TfIdfTable;
	}

	private static class WordDoublePair {

		Word word;
		double value;

		public WordDoublePair(Word word, double value) {
			super();
			this.word = word;
			this.value = value;
		}

		public double getValue() {
			return value;
		}

		public Word getWord() {
			return word;
		}
	}

}
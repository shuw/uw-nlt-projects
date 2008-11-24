package project2.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.WordMagnitude;
import edu.nlt.shallow.data.builder.IDFTableBuilder;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;
import edu.nlt.util.Singletons;

public class VocabFileProcessor implements FileProcessor {
	private GoldStandard goldStandard;
	private IDFTableBuilder idfTableBuilder = new IDFTableBuilder();

	private WordCountProcessor lingProcessor = new WordCountProcessor();
	private WordCountProcessor nonLingProcessor = new WordCountProcessor();

	public VocabFileProcessor(GoldStandard goldStandard) {
		super();
		this.goldStandard = goldStandard;
	}

	@Override
	public void processFile(File file) {
		// remove file suffix
		String fileName = file.getName();

		if (goldStandard.isCategorized(fileName)) {
			BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
			InputUtil.process(file, new PlainWordProcessor(bagOfWordsProcessor));
			idfTableBuilder.addDocument(bagOfWordsProcessor.getWords());

			if (goldStandard.isLinguistic(fileName)) {

				InputUtil.process(file, new PlainWordProcessor(lingProcessor));
			} else {
				InputUtil.process(file, new PlainWordProcessor(nonLingProcessor));
			}

		} else {
			System.err.println("Uncategorized file: " + fileName);
		}

	}

	public void printResult(int maxSizeOfVocab) {
		IDFTable idfTable = idfTableBuilder.build();
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

		ArrayList<WordMagnitude> list = new ArrayList<WordMagnitude>(lingTfIdfTable.size());
		{
			for (String word : lingTfIdfTable.keySet()) {

				double lingTfIdf = lingTfIdfTable.get(word);

				Double nonLingTfIdf = nonlingTfIdfTable.get(word);
				if (nonLingTfIdf != null) {

					WordMagnitude x = new WordMagnitude(new Word(word), Math.abs(lingTfIdf
							- nonLingTfIdf));
					list.add(x);

				}

			}
		}

		Collections.sort(list, new Comparator<WordMagnitude>() {

			@Override
			public int compare(WordMagnitude o1, WordMagnitude o2) {
				return Double.compare(o2.getMagnitude(), o1.getMagnitude());
			}

		});

		int count = 0;
		for (WordMagnitude wordValue : list) {

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

}
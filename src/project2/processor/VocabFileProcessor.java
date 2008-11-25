package project2.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.WordMagnitude;
import edu.nlt.shallow.data.builder.IDFTableBuilder;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;

public class VocabFileProcessor implements FileProcessor {
	private GoldStandard goldStandard;
	private IDFTableBuilder globalIdfTableBuilder = new IDFTableBuilder();
	private IDFTableBuilder LidfTableBuilder = new IDFTableBuilder();
	private IDFTableBuilder NLidfTableBuilder = new IDFTableBuilder();

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

			globalIdfTableBuilder.addDocument(bagOfWordsProcessor.getWords());
			if (goldStandard.isLinguistic(fileName)) {
				LidfTableBuilder.addDocument(bagOfWordsProcessor.getWords());

				InputUtil.process(file, new PlainWordProcessor(lingProcessor));
			} else {
				NLidfTableBuilder.addDocument(bagOfWordsProcessor.getWords());
				InputUtil.process(file, new PlainWordProcessor(nonLingProcessor));
			}

		} else {
			System.err.println("Uncategorized file: " + fileName);
		}

	}

	private static final boolean weightTermFrequency = true;

	public void printResult(int maxSizeOfVocab) {
		IDFTable LIdfTable = LidfTableBuilder.build();
		IDFTable NLIdfTable = NLidfTableBuilder.build();
		IDFTable globalIdfTable = globalIdfTableBuilder.build();

		KeyCounterTable<Word> lingCounter = lingProcessor.getCounter();
		KeyCounterTable<Word> nonLingCounter = nonLingProcessor.getCounter();

		ArrayList<WordMagnitude> list;
		if (weightTermFrequency) {

			Hashtable<String, Double> lingTfIdfTable;
			Hashtable<String, Double> nonlingTfIdfTable;
			{
				// calculate global TFIDF
				//

				lingTfIdfTable = getGlobalTfIdf(globalIdfTable, lingCounter);
				nonlingTfIdfTable = getGlobalTfIdf(globalIdfTable, nonLingCounter);
			}
			list = new ArrayList<WordMagnitude>(lingTfIdfTable.size());

			for (String word : lingTfIdfTable.keySet()) {

				double lingTfIdf = lingTfIdfTable.get(word);

				Double nonLingTfidf = nonlingTfIdfTable.get(word);
				if (nonLingTfidf != null) {

					WordMagnitude x = new WordMagnitude(new Word(word), Math.abs(lingTfIdf
							- nonLingTfidf));
					list.add(x);

				}

			}

		} else {
			list = new ArrayList<WordMagnitude>(LIdfTable.values().size());
			{
				for (WordIDF wordIDF : LIdfTable.values()) {
					Word word = wordIDF.getWord();

					// double lingTfIdf = lingTfIdfTable.get(word);

					WordIDF NLidf = NLIdfTable.getWordIDF(word);
					if (NLidf != null) {

						WordMagnitude x = new WordMagnitude(word, Math.abs(NLidf.getIDF()
								- wordIDF.getIDF()));
						list.add(x);

					}

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

			double IDF = globalIdfTable.getWordIDF(word).getIDF();
			System.out.print(word + "\t" + IDF);

			{

				int lingWordCount = lingCounter.getCount(word);
				double lingTfIdf = (double) lingWordCount / (double) lingCounter.getTotalCount()
						* IDF;

				System.out.print("\t\t" + lingWordCount);

				int docFrequency = LIdfTable.getWordIDF(word).getDocumentCount();
				System.out.print("\t" + docFrequency);

				// System.out.print("/" +
				// Singletons.FractionFormatter.format(lingTfIdf));
			}

			{
				int nonLingWordCount = nonLingCounter.getCount(word);

				double nonLingTfIdf = (double) nonLingWordCount
						/ (double) nonLingCounter.getTotalCount() * IDF;

				System.out.print("\t\t" + nonLingWordCount);
				// System.out.print("/" +
				// Singletons.FractionFormatter.format(nonLingTfIdf));

				int docFrequency = NLIdfTable.getWordIDF(word).getDocumentCount();
				System.out.print("\t" + docFrequency);

			}

			if (++count > maxSizeOfVocab) {
				break;
			}
			System.out.println();
		}
	}

	private static Hashtable<String, Double> getGlobalTfIdf(IDFTable idfTable,
			KeyCounterTable<Word> counterTable) {
		Hashtable<String, Double> TfIdfTable = new Hashtable<String, Double>();

		// int count = 0;
		for (CountHolder<Word> wordCounter : counterTable.getReverseSorted()) {

			Word word = wordCounter.getComponent();
			double tf = (double) wordCounter.getCount() / (double) counterTable.getTotalCount();

			double tfIdf = tf * idfTable.getWordIDF(word).getIDF();

			// if (count++ < 500) {
			// System.out.println(word + " " + tfIdf);
			// }

			TfIdfTable.put(word.getKey(), tfIdf);
		}

		return TfIdfTable;
	}

}
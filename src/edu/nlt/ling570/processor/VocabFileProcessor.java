package edu.nlt.ling570.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;

import edu.nlt.ling570.project2.processor.BagOfWordsProcessor;
import edu.nlt.ling570.project2.processor.PlainWordProcessor;
import edu.nlt.ling570.project2.processor.WordCountProcessor;
import edu.nlt.shallow.classifier.BinaryFileClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;
import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.WordIDF;
import edu.nlt.shallow.data.WordMagnitude;
import edu.nlt.shallow.data.builder.IDFTableBuilder;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.InputUtil;

public class VocabFileProcessor implements FileProcessor {
	private BinaryFileClassifier goldStandard;
	private IDFTableBuilder globalIdfTableBuilder = new IDFTableBuilder();
	private IDFTableBuilder LidfTableBuilder = new IDFTableBuilder();
	private IDFTableBuilder NLidfTableBuilder = new IDFTableBuilder();

	private WordCountProcessor lingProcessor = new WordCountProcessor();
	private WordCountProcessor nonLingProcessor = new WordCountProcessor();

	public VocabFileProcessor(BinaryFileClassifier goldStandard) {
		super();
		this.goldStandard = goldStandard;
	}

	private Hashtable<String, DocumentFeature> L_TFFeatures = new Hashtable<String, DocumentFeature>();
	private Hashtable<String, DocumentFeature> NL_TFFeatures = new Hashtable<String, DocumentFeature>();

	public static final boolean DocumentNormalizedTermFrequency = true;

	@Override
	public void processFile(File file) {
		try {

			BagOfWordsProcessor bagOfWordsProcessor = new BagOfWordsProcessor();
			InputUtil.process(file, new PlainWordProcessor(bagOfWordsProcessor));

			globalIdfTableBuilder.addDocument(bagOfWordsProcessor.getWords());
			if (goldStandard.isPositive(file)) {
				LidfTableBuilder.addDocument(bagOfWordsProcessor.getWords());

				InputUtil.process(file, new PlainWordProcessor(lingProcessor));
			} else {
				NLidfTableBuilder.addDocument(bagOfWordsProcessor.getWords());
				InputUtil.process(file, new PlainWordProcessor(nonLingProcessor));
			}

			if (DocumentNormalizedTermFrequency) {
				KeyCounterTable<Word> counter;
				{
					WordCountProcessor processor = new WordCountProcessor();
					InputUtil.process(file, new PlainWordProcessor(processor));
					counter = processor.getCounter();
				}

				for (String word : bagOfWordsProcessor.getWords()) {

					Hashtable<String, DocumentFeature> TFFeatures = goldStandard.isPositive(file) ? L_TFFeatures
							: NL_TFFeatures;

					DocumentFeature feature = TFFeatures.get(word);

					if (feature == null) {
						feature = new DocumentFeature(new Word(word), 0);
						TFFeatures.put(word, feature);
					}
					double tf = (double) counter.getCount(feature.getWord())
							/ (double) counter.getTotalCount();
					feature.addMagnitude(tf);

				}

			}

		} catch (NotClassifiedException e) {
			e.printStackTrace(System.err);
		}
	}

	private static final boolean UseWeightTermFrequency = true;

	private void normalizeTermFrequencies() {

		double normalizeDocumentsSize = (double) LidfTableBuilder.getNumOfDocuments()
				/ (double) NLidfTableBuilder.getNumOfDocuments();

		for (DocumentFeature feature : NL_TFFeatures.values()) {

			feature.setMagnitude(feature.getMagnitude() * normalizeDocumentsSize);

		}

	}

	public void printResult(int maxSizeOfVocab) {
		IDFTable LIdfTable = LidfTableBuilder.build();
		IDFTable NLIdfTable = NLidfTableBuilder.build();
		IDFTable globalIdfTable = globalIdfTableBuilder.build();

		KeyCounterTable<Word> lingCounter = lingProcessor.getCounter();
		KeyCounterTable<Word> nonLingCounter = nonLingProcessor.getCounter();

		ArrayList<WordMagnitude> list;
		if (UseWeightTermFrequency) {
			normalizeTermFrequencies();

			Hashtable<String, Double> lingTfIdfTable;
			Hashtable<String, Double> nonlingTfIdfTable;
			{

				if (DocumentNormalizedTermFrequency) {

					lingTfIdfTable = getTfIdfTable(globalIdfTable, lingCounter, L_TFFeatures);
					nonlingTfIdfTable = getTfIdfTable(globalIdfTable, nonLingCounter, NL_TFFeatures);
				} else {
					lingTfIdfTable = getTfIdfTable(globalIdfTable, lingCounter, null);
					nonlingTfIdfTable = getTfIdfTable(globalIdfTable, nonLingCounter, null);
				}

			}
			list = new ArrayList<WordMagnitude>(lingTfIdfTable.size());

			HashSet<String> allWords = new HashSet<String>(lingTfIdfTable.size()
					+ nonlingTfIdfTable.size());

			for (String word : lingTfIdfTable.keySet()) {
				allWords.add(word);
			}

			for (String word : nonlingTfIdfTable.keySet()) {
				allWords.add(word);
			}

			for (String word : allWords) {

				Double lingTfIdf = lingTfIdfTable.get(word);
				if (lingTfIdf == null) {
					lingTfIdf = 0d;
				}

				Double nonLingTfidf = nonlingTfIdfTable.get(word);
				if (nonLingTfidf == null) {

					nonLingTfidf = 0d;
				}

				WordMagnitude x = new WordMagnitude(new Word(word), Math.abs(lingTfIdf
						- nonLingTfidf));
				list.add(x);

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

				int docFrequency = (LIdfTable.getWordIDF(word) != null) ? LIdfTable
						.getWordIDF(word).getDocumentCount() : 0;
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

				int docFrequency = (NLIdfTable.getWordIDF(word) != null) ? NLIdfTable.getWordIDF(
						word).getDocumentCount() : 0;
				System.out.print("\t" + docFrequency);

			}

			if (++count > maxSizeOfVocab) {
				break;
			}
			System.out.println();
		}
	}

	private static Hashtable<String, Double> getTfIdfTable(IDFTable idfTable,
			KeyCounterTable<Word> counterTable, Hashtable<String, DocumentFeature> tfFeatures) {
		Hashtable<String, Double> TfIdfTable = new Hashtable<String, Double>();

		// int count = 0;
		for (CountHolder<Word> wordCounter : counterTable.getReverseSorted()) {

			Word word = wordCounter.getComponent();

			double tf;
			if (tfFeatures != null) {
				// Used stored term frequencies which are normalized per
				// document
				//

				tf = tfFeatures.get(word.getKey()).getMagnitude();
			} else {
				// Calculate term frequencies based on global count
				//

				tf = (double) wordCounter.getCount() / (double) counterTable.getTotalCount();
			}

			double tfIdf = tf * idfTable.getWordIDF(word).getIDF();

			TfIdfTable.put(word.getKey(), tfIdf);
		}

		return TfIdfTable;
	}

}
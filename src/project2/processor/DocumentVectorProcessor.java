package project2.processor;

import java.util.Hashtable;

import project2.data.Vocabulary;
import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.processor.WordProcessor;

public class DocumentVectorProcessor implements WordProcessor {

	private Vocabulary vocabulary;
	private KeyCounterTable<Word> counter = new KeyCounterTable<Word>();

	public DocumentVectorProcessor(Vocabulary vocabulary) {
		super();

		this.vocabulary = vocabulary;
	}

	@Override
	public void processWord(Word word) {
		if (vocabulary.contains(word)) {
			counter.add(word);
		}
	}

	public DocumentVector getDocumentVector() {

		Hashtable<String, DocumentFeature> vectorTable = new Hashtable<String, DocumentFeature>();

		for (CountHolder<Word> wordCount : counter.values()) {

			Word word = wordCount.getComponent();
			double tfIdf = (double) wordCount.getCount() * vocabulary.getWordIDF(word).getIDF();

			DocumentFeature feature = new DocumentFeature(word, tfIdf);

			vectorTable.put(word.getKey(), feature);

		}

		return new DocumentVector(vectorTable);

	}
}

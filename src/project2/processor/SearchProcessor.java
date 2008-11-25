package project2.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import project2.data.DocumentResult;
import project2.data.Vocabulary;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.Singletons;
import edu.nlt.util.VectorUtil;
import edu.nlt.util.processor.LineProcessor;

public class SearchProcessor implements LineProcessor {

	private Hashtable<DocumentVector, double[]> vectorTable = new Hashtable<DocumentVector, double[]>();

	private Vocabulary vocabulary;

	private PlainWordParser wordParser = new PlainWordParser();

	public SearchProcessor(Vocabulary vocabulary, Collection<DocumentVector> documents) {
		super();
		this.vocabulary = vocabulary;

		initVectors(documents);
	}

	private List<DocumentResult> getResults(double[] searchQueryNormalized) {

		ArrayList<DocumentResult> results = new ArrayList<DocumentResult>(vectorTable.size());
		for (DocumentVector document : vectorTable.keySet()) {

			double[] documentVectorNormalized = vectorTable.get(document);

			double score = VectorUtil.dotProduct(documentVectorNormalized, searchQueryNormalized);

			results.add(new DocumentResult(document, score));

		}

		// Reverse sort results by cosine similarity score
		Collections.sort(results, new Comparator<DocumentResult>() {
			@Override
			public int compare(DocumentResult o1, DocumentResult o2) {
				return Double.compare(o2.getScore(), o1.getScore());
			}
		});

		return results;
	}

	private double[] getSearchVector(String value) {
		try {
			ArrayList<DocumentFeature> features = new ArrayList<DocumentFeature>();

			for (Word word : wordParser.getWords(value)) {
				features.add(new DocumentFeature(word, 1));
			}
			return VectorUtil.getNormalizedVector(features, vocabulary);

		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void initVectors(Collection<DocumentVector> documents) {

		for (DocumentVector document : documents) {

			double[] vector = VectorUtil.getNormalizedVector(document.values(), vocabulary);

			vectorTable.put(document, vector);

		}

	}

	private void printResults(List<DocumentResult> results) {

		int resultsToShow = 20;
		for (DocumentResult result : results) {
			if (resultsToShow-- == 0 || result.getScore() == 0) {
				break;
			}

			System.out.println(result.getDocument().getVectorName() + "\t"
					+ Singletons.FractionFormatter.format(result.getScore()));

		}

	}

	@Override
	public void processLine(String value) {
		double[] searchQuery = getSearchVector(value);

		if (searchQuery != null) {
			List<DocumentResult> results = getResults(searchQuery);

			printResults(results);
		}
	}

}

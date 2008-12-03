package project2.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import project2.data.DocumentResult;
import project2.data.Vocabulary;
import edu.nlt.external.PorterStemmer;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.PlainWordParser;
import edu.nlt.util.Formatters;
import edu.nlt.util.Globals;
import edu.nlt.util.VectorUtil;
import edu.nlt.util.processor.LineProcessor;

public class SearchProcessor implements LineProcessor {

	private static final boolean useStemming = false;

	private PorterStemmer stemmer = new PorterStemmer();

	/**
	 * Map stemmed words to their original in the vocabulary
	 */
	private Hashtable<String, HashSet<String>> stemmedVocabTable = new Hashtable<String, HashSet<String>>();

	private Hashtable<DocumentVector, double[]> vectorTable = new Hashtable<DocumentVector, double[]>();

	private Vocabulary vocabulary;

	private PlainWordParser wordParser = new PlainWordParser();

	public SearchProcessor(Vocabulary vocabulary, Collection<DocumentVector> documents) {
		super();
		this.vocabulary = vocabulary;

		initStemTable();
		init(documents);
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

				double idf = vocabulary.getWordIDF(word).getIDF();

				if (useStemming) {
					// Expand query word to all it's unstemmed versions in the
					// vocabulary
					String stemmedWord = stemmer.stripAffixes(word.getKey());

					HashSet<String> expandedWords = stemmedVocabTable.get(stemmedWord);

					if (expandedWords != null) {
						for (String expandedWord : expandedWords) {
							features.add(new DocumentFeature(new Word(expandedWord), idf));
						}

					} else {
						features.add(new DocumentFeature(word, idf));
					}

				} else {
					features.add(new DocumentFeature(word, idf));
				}

			}

			for (DocumentFeature feature : features) {
				System.out.print(feature.getWord() + " ");
			}
			System.out.println();

			return VectorUtil.getNormalizedVector(features, vocabulary);

		} catch (ParserException e) {
			e.printStackTrace();
		}
		return null;

	}

	private void init(Collection<DocumentVector> documents) {

		for (DocumentVector document : documents) {

			double[] vector = VectorUtil.getNormalizedVector(document.values(), vocabulary);

			vectorTable.put(document, vector);

		}

	}

	private void initStemTable() {

		for (String word : vocabulary.values()) {

			String stemmedWord = stemmer.stripAffixes(word);

			HashSet<String> originalWords = stemmedVocabTable.get(stemmedWord);

			if (originalWords == null) {
				originalWords = new HashSet<String>();
				stemmedVocabTable.put(stemmedWord, originalWords);
			}

			originalWords.add(word);

		}

	}

	private void printResults(List<DocumentResult> results) {

		int resultCount = 1;
		double topScore = results.get(0).getScore();

		double accumulatedScore = 0;
		for (DocumentResult result : results) {

			// Always how top 3 results
			if (resultCount > 3 || result.getScore() > 0.2d) {

				double averageScore = accumulatedScore / resultCount;

				boolean cutoffReached = false;
				// Stop if the score if below half of average
				if (result.getScore() < averageScore / 2d) {
					cutoffReached = true;
				}

				// Stop if the score drops below 1/5th of top result
				if (result.getScore() < topScore / 3d) {
					cutoffReached = true;
				}

				// Stop really low scores
				if (result.getScore() < 0.00001) {
					break;
				}

				if (cutoffReached) {

					if (Globals.IsDebugEnabled) {
						System.out.println("Document after cutoff:"
								+ result.getDocument().getVectorName() + "\t"
								+ Formatters.FractionFormatter.format(result.getScore()));
					}
					break;
				}
			}

			System.out.println(result.getDocument().getVectorName() + "\t"
					+ Formatters.FractionFormatter.format(result.getScore()));
			accumulatedScore += result.getScore();

			resultCount++;
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

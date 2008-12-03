package project2;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import project2.data.LinguisticCluster;
import project2.data.Vocabulary;
import project2.processor.SearchProcessor;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.FileProcessor;
import edu.nlt.util.Globals;
import edu.nlt.util.InputUtil;

public class Search {

	/**
	 * @param args
	 * 
	 *            args[0] - vocabulary for vector space
	 * 
	 *            args[1] - clusters
	 * 
	 *            args[2] - path to documents
	 * 
	 */
	public static void main(String[] args) {

		final Vocabulary vocabulary = Util.getVocabulary(new File(args[0]), -1);

		final Collection<LinguisticCluster> clusters = Util.getClusters(new File(args[1]),
				vocabulary);

		if (Globals.IsDebugEnabled) {
			System.out.println("loading vector space and classifying files... please be patient");
		}
		final LinkedList<DocumentVector> linguisticDocuments = new LinkedList<DocumentVector>();
		{
			final LinkedList<DocumentVector> allDocuments = new LinkedList<DocumentVector>();
			{
				// Get document vectors
				//
				InputUtil.processFiles(args[2], new FileProcessor() {
					@Override
					public void processFile(File file) {
						allDocuments.add(Util.getDocumentVector(file, vocabulary));
					}
				});
			}

			// Filter to linguistic documents
			for (DocumentVector document : allDocuments) {
				if (Classify.isLinguistic(clusters, document)) {
					linguisticDocuments.add(document);
				}
			}
		}
		if (Globals.IsDebugEnabled) {
			System.out.println("Ready!");
		}
		SearchProcessor searchProcessor = new SearchProcessor(vocabulary, linguisticDocuments);
		InputUtil.process(System.in, searchProcessor);
	}
}

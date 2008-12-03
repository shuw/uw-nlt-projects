package edu.nlt.ling570.project2;

import java.io.File;
import java.util.Collection;

import edu.nlt.ling570.project2.processor.SearchProcessor;
import edu.nlt.ling570.project2.processor.VectorProcessor;
import edu.nlt.shallow.data.Vocabulary;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;

public class SearchVectors {

	/**
	 * @param args
	 * 
	 *            args[0] - vocabulary for vector space
	 * 
	 *            args[1] - vector space
	 * 
	 */
	public static void main(String[] args) {

		Vocabulary vocabulary = Util.getVocabulary(new File(args[0]), -1);

		Collection<DocumentVector> documents;
		{
			VectorProcessor processor = new VectorProcessor();
			InputUtil.process(new File(args[1]), processor);
			documents = processor.getVectors();
		}

		SearchProcessor searchProcessor = new SearchProcessor(vocabulary, documents);
		InputUtil.process(System.in, searchProcessor);
	}
}

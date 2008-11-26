package project2;

import java.io.File;
import java.util.Collection;

import project2.data.LinguisticCluster;
import project2.data.Vocabulary;
import project2.processor.ClustersProcessor;
import project2.processor.DocumentVectorProcessor;
import project2.processor.FileVocabularyBuilder;
import project2.processor.GoldStandard;
import project2.processor.GoldStandardProcessor;
import project2.processor.PlainWordProcessor;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;

public class Util {
	public static DocumentVector getDocumentVector(File file, Vocabulary vocab) {

		DocumentVectorProcessor processor = new DocumentVectorProcessor(vocab, file.getName());

		InputUtil.process(file, new PlainWordProcessor(processor));

		return processor.getDocumentVector();

	}

	public static DocumentVector getDocumentVector(Vocabulary vocabulary, File file) {
		DocumentVectorProcessor processor = new DocumentVectorProcessor(vocabulary, file.getName());
		InputUtil.process(System.in, new PlainWordProcessor(processor));
		return processor.getDocumentVector();

	}

	public static Collection<LinguisticCluster> getClusters(File file, Vocabulary vocabulary) {

		ClustersProcessor processor = new ClustersProcessor(vocabulary);
		InputUtil.process(file, processor);

		return processor.getVectors();
	}

	public static Vocabulary getVocabulary(File file, int maxVocabSize) {

		FileVocabularyBuilder builder = new FileVocabularyBuilder(maxVocabSize);
		InputUtil.process(file, builder);

		return builder.build();
	}

	public static GoldStandard getGoldStandard(File file) {
		GoldStandardProcessor processor = new GoldStandardProcessor();

		// Parse GoldStandardFile
		//
		InputUtil.process(file, processor);
		return processor.getGoldStandard();
	}

}

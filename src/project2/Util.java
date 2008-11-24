package project2;

import java.io.File;
import java.util.HashSet;

import project2.helper.DocumentVectorProcessor;
import project2.helper.FileIDFBuilder;
import project2.helper.GoldStandard;
import project2.helper.GoldStandardProcessor;
import project2.helper.PlainWordProcessor;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;
import edu.nlt.util.processor.LineProcessor;

public class Util {
	public static DocumentVector getDocumentVector(File file, IDFTable idfTable,
			HashSet<String> vocab) {

		DocumentVectorProcessor processor = new DocumentVectorProcessor(idfTable, vocab);

		InputUtil.process(file, new PlainWordProcessor(processor));

		return processor.getDocumentVector();

	}

	public static HashSet<String> getVocabulary(File file) {
		final HashSet<String> vocabulary = new HashSet<String>();

		InputUtil.process(file, new LineProcessor() {

			@Override
			public void processLine(String value) {
				vocabulary.add(value.split("\t")[0]);

			}
		});

		return vocabulary;
	}

	public static GoldStandard getGoldStandard(File file) {
		GoldStandardProcessor processor = new GoldStandardProcessor();

		// Parse GoldStandardFile
		//
		InputUtil.process(file, processor);
		return processor.getGoldStandard();
	}

	public static IDFTable getIDFTable(File file) {
		FileIDFBuilder builder = new FileIDFBuilder();
		InputUtil.process(file, builder);
		return builder.build();
	}
}

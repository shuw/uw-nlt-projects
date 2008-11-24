package project2;

import java.io.File;
import java.util.HashSet;

import project2.helper.DocumentVectorProcessor;
import project2.helper.FileIDFBuilder;
import project2.helper.PlainWordProcessor;
import edu.nlt.shallow.data.table.IDFTable;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.InputUtil;
import edu.nlt.util.processor.LineProcessor;

public class PrintVector {

	/**
	 * Prints Vector Space for file
	 * 
	 * @param args
	 * 
	 *            args[0] IDFTable
	 * 
	 *            args[1] Vocabulary
	 * 
	 *            args[2] Path to file
	 * 
	 * 
	 */
	public static void main(String[] args) {

		IDFTable idfTable = readIDFTable(new File(args[0]));

		HashSet<String> vocabulary = readVocabulary(new File(args[1]));

		DocumentVector documentVector = createDocumentVector(new File(args[2]), idfTable,
				vocabulary);

		documentVector.print();
	}

	private static DocumentVector createDocumentVector(File file, IDFTable idfTable,
			HashSet<String> vocab) {

		DocumentVectorProcessor processor = new DocumentVectorProcessor(idfTable, vocab);

		InputUtil.process(file, new PlainWordProcessor(processor));

		return processor.getDocumentVector();

	}

	private static HashSet<String> readVocabulary(File file) {
		final HashSet<String> vocabulary = new HashSet<String>();

		InputUtil.process(file, new LineProcessor() {

			@Override
			public void processLine(String value) {
				vocabulary.add(value.split("\t")[0]);

			}
		});

		return vocabulary;
	}

	private static IDFTable readIDFTable(File file) {
		FileIDFBuilder builder = new FileIDFBuilder();
		InputUtil.process(file, builder);
		return builder.build();

	}
}

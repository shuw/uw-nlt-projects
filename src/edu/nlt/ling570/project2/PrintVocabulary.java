package edu.nlt.ling570.project2;

import java.io.File;

import edu.nlt.ling570.processor.VocabFileProcessor;
import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.ling570.project2.data.FileClassifierAdapter;
import edu.nlt.util.InputUtil;

public class PrintVocabulary {
	/**
	 * @param args
	 * 
	 *            args[0] Path to Input files
	 * 
	 *            args[1] GoldStandard file
	 */
	public static void main(String[] args) {

		// Parse gold standard
		//
		final ClassifierGoldStandard goldStandard = Util.getGoldStandard(new File(args[1]));

		// Print vocabulary
		//
		{
			String inputFilesPath = args[0];

			VocabFileProcessor processor = new VocabFileProcessor(new FileClassifierAdapter(
					goldStandard));
			InputUtil.processFiles(inputFilesPath, processor);

			processor.printResult(10000);
		}
	}
}

package project2;

import java.io.File;

import project2.helper.GoldStandard;
import project2.helper.GoldStandardProcessor;
import project2.helper.VocabFileProcessor;
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
		GoldStandard goldStandard = Util.getGoldStandard(new File(args[1]));

		// Print vocabulary
		//
		{
			String inputFilesPath = args[0];

			VocabFileProcessor processor = new VocabFileProcessor(goldStandard);
			InputUtil.processFiles(inputFilesPath, processor);

			processor.printResult(2000);
		}
	}

}

package edu.nlt.ling570.hw5;

import java.io.File;
import java.util.LinkedList;

import edu.nlt.ling570.processor.VocabFileProcessor;
import edu.nlt.ling570.project2.data.ClassifierGoldStandard;
import edu.nlt.shallow.classifier.BinaryFileClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;
import edu.nlt.util.FileUtil;

public class PrintGoldStandard {

	/**
	 * Prepares gold-standard file, expected by project2 programs
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String inputFilesPath = args[0];

		LinkedList<File> files = new LinkedList<File>();
		{
			for (File file : FileUtil.getFiles(inputFilesPath + "/"
					+ TrainingDataClassifier.EmailFolderName, true)) {
				files.add(file);
			}
			for (File file : FileUtil.getFiles(inputFilesPath + "/"
					+ TrainingDataClassifier.SpamFolderName, true)) {
				files.add(file);
			}
		}

		// Print gold standard
		TrainingDataClassifier classifier = new TrainingDataClassifier();
		for (File file : files) {

			String fileName = ClassifierGoldStandard.canonizeName(file.getName());
			try {
				System.out.println(fileName + "\t" + (classifier.isPositive(file) ? "X" : ""));
			} catch (NotClassifiedException e) {
				e.printStackTrace(System.err);
			}
		}

	}
}

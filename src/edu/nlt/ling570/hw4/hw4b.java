package edu.nlt.ling570.hw4;

import java.io.File;
import java.text.DecimalFormat;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.languageModels.cavnarTrenkle.CTNgramGoodTuringSmoothing;
import edu.nlt.ling570.hw3.CTProcessor;
import edu.nlt.util.InputUtil;

public class hw4b {
	/**
	 * @param args
	 * 
	 * args[0] Model to use
	 * 
	 * args[1] label
	 * 
	 */
	public static void main(String[] args) {

		CTProcessor myProcessor = new CTProcessor();
		InputUtil.process(new File(args[0]), myProcessor);

		CTLanguageModel myModel = myProcessor.getModel();

		printResults(myModel, args[1], 5);
	}

	public static DecimalFormat FractionFormatter = new DecimalFormat("0.00");

	private static void printResults(CTLanguageModel myModel, String label, int paramK) {

		CTNgramGoodTuringSmoothing ngramModel = myModel.getNgramSmoothingTable(2, paramK);

		System.out.println(label + " - Good Turing\n");
		System.out.println("c\tNc\tc*");
		for (int c = 0; c <= paramK; c++) {
			System.out.print(c + "\t");
			System.out.print(ngramModel.getFrequencyOfFrequency(c) + "\t");
			System.out.print(FractionFormatter.format(ngramModel.getSmoothedFrequency(c)) + "\t");
			System.out.println();
		}

		System.out.println();
	}
}

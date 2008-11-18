package edu.nlt.ling570.hw3;

import java.io.File;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.util.InputUtil;

public class hw3b {
	/**
	 * @param args
	 *            args[0] Portuguese model to use
	 * 
	 * args[1] CT model
	 * 
	 * args[2] optional label for result table
	 */
	public static void main(String[] args) {

		CTProcessor myProcessor = new CTProcessor();
		InputUtil.process(new File(args[0]), myProcessor);

		CTProcessor ctProcessor = new CTProcessor();
		InputUtil.process(new File(args[1]), ctProcessor);

		CTLanguageModel myModel = myProcessor.getModel();
		CTLanguageModel ctModel = ctProcessor.getModel();

		printResults(myModel, ctModel, args[2]);
	}

	private static void printResults(CTLanguageModel myModel, CTLanguageModel ctModel, String dataLabel) {
		System.out.print("                                 \t");
		for (int i = 1; i <= 3; i++) {
			System.out.print(i + "-gram\t\t");
		}
		System.out.println();

		System.out.print((dataLabel != null ? dataLabel + " " : "") + "Portuguese model divergence");
		for (int i = 1; i <= 3; i++) {

			double klDivergence = myModel.getNgramTable(i, CTLanguageModel.SmoothingMethod.Plus1)
					.calculateKLDivergence(ctModel.getNgramTable(i, CTLanguageModel.SmoothingMethod.Plus1));

			System.out.print("\t" + hw3a.FractionFormatter.format(klDivergence) + "\t");
		}

		System.out.println();

		System.out.print("Perplexity                          ");

		for (int i = 1; i <= 3; i++) {
			System.out.print("\t"
					+ hw3a.FractionFormatter.format(myModel.getNgramTable(i, CTLanguageModel.SmoothingMethod.Plus1)
							.calculatePerplexity()) + "\t");
		}

	}
}

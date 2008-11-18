package edu.nlt.ling570.hw4;

import java.io.File;
import java.text.DecimalFormat;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.ling570.hw3.CTProcessor;
import edu.nlt.util.InputUtil;

public class PrintCP {
	/**
	 * Prints Cross-perplexity
	 * 
	 * @param args
	 * 
	 * args[0] Model to use
	 * 
	 * args[1] Portceli model
	 * 
	 * args[2] if equals to "gt", then Good-turing smoothing is used
	 * 
	 */
	public static void main(String[] args) {

		CTProcessor myProcessor = new CTProcessor();
		InputUtil.process(new File(args[0]), myProcessor);

		CTProcessor ctProcessor = new CTProcessor();
		InputUtil.process(new File(args[1]), ctProcessor);

		CTLanguageModel myModel = myProcessor.getModel();
		CTLanguageModel portceliModel = ctProcessor.getModel();

		CTLanguageModel.SmoothingMethod smoothingMethod;
		if (args[2].equals("gt")) {
			smoothingMethod = CTLanguageModel.SmoothingMethod.GoodTuring;
		} else {
			smoothingMethod = CTLanguageModel.SmoothingMethod.Plus1;
		}

		printResults(myModel, portceliModel, smoothingMethod);
	}

	public static DecimalFormat FractionFormatter = new DecimalFormat("0.00");

	private static void printResults(CTLanguageModel myModel, CTLanguageModel portceliModel,
			CTLanguageModel.SmoothingMethod smoothingMethod) {

		// use +1 smoothing
		double klDivergence = portceliModel.getNgramTable(2, CTLanguageModel.SmoothingMethod.None)
				.calculateKLDivergence(myModel.getNgramTable(2, smoothingMethod));

		double entropy = portceliModel.getNgramTable(2, smoothingMethod).calculateEntropy();
		System.out.println("Entropy: " + entropy);
		double crossEntropy = entropy + klDivergence;
		System.out.println("KL Divergence: " + klDivergence);

		System.out.println("Perplexity: " + FractionFormatter.format(Math.pow(2, crossEntropy)));

	}
}

package edu.nlt.ling570.hw4;

import java.io.File;
import java.text.DecimalFormat;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.ling570.hw3.CTProcessor;
import edu.nlt.util.InputUtil;

public class hw4a {
	/**
	 * 
	 * @param args
	 * 
	 * args[0] 96 model file
	 * 
	 * args[1] all model file
	 * 
	 * args[2] portceli model file
	 */
	public static void main(String[] args) {

		CTProcessor _96Processor = new CTProcessor();
		InputUtil.process(new File(args[0]), _96Processor);

		CTProcessor allProcessor = new CTProcessor();
		InputUtil.process(new File(args[1]), allProcessor);

		CTProcessor portceliProcessor = new CTProcessor();
		InputUtil.process(new File(args[2]), portceliProcessor);

		CTLanguageModel _96Model = _96Processor.getModel();
		CTLanguageModel allModel = allProcessor.getModel();
		CTLanguageModel portceliModel = portceliProcessor.getModel();

		System.out.println("Model 1 - 96, Add 1\t"
				+ FractionFormatter.format(caclculatePerplexity(_96Model, portceliModel,
						CTLanguageModel.SmoothingMethod.Plus1)));

		System.out.println("Model 2 - all, Add 1\t"
				+ FractionFormatter.format(caclculatePerplexity(allModel, portceliModel,
						CTLanguageModel.SmoothingMethod.Plus1)));

		System.out.println("Model 3 - 96, Good turing\t"
				+ FractionFormatter.format(caclculatePerplexity(_96Model, portceliModel,
						CTLanguageModel.SmoothingMethod.GoodTuring)));

		System.out.println("Model 4 - all, Good turing\t"
				+ FractionFormatter.format(caclculatePerplexity(allModel, portceliModel,
						CTLanguageModel.SmoothingMethod.GoodTuring)));

	}

	public static DecimalFormat FractionFormatter = new DecimalFormat("0.00");

	private static double caclculatePerplexity(CTLanguageModel myModel, CTLanguageModel portceliModel,
			CTLanguageModel.SmoothingMethod smoothingMethod) {

		// use +1 smoothing
		double klDivergence = portceliModel.getNgramTable(2, smoothingMethod).calculateKLDivergence(
				myModel.getNgramTable(2, smoothingMethod));

		double entropy = portceliModel.getNgramTable(2, smoothingMethod).calculateEntropy();
		double crossEntropy = entropy + klDivergence;

		return Math.pow(2, crossEntropy);

	}
}

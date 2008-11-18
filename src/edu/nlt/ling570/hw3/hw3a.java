package edu.nlt.ling570.hw3;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.languageModels.cavnarTrenkle.CTNgramTable;
import edu.nlt.util.InputUtil;

public class hw3a {

	public static void main(String[] args) {
		final String[] supportedLanguages = new String[] { "English   ", "German   ", "Portuguese", "Spanish   " };

		final String languageModelPath = args[0];

		ArrayList<CTLanguageModel> models = new ArrayList<CTLanguageModel>(supportedLanguages.length);
		for (String language : supportedLanguages) {
			models.add(getLanguageModel(languageModelPath, language));
		}

		for (int nGram = 1; nGram <= 3; nGram++) {
			printResult(models, supportedLanguages, nGram);
			System.out.println();
		}
	}

	public static DecimalFormat FractionFormatter = new DecimalFormat("0.00");

	private static void printResult(ArrayList<CTLanguageModel> models, String[] modelNames, int nGram) {

		System.out.print("\t\t");
		for (String language : modelNames) {
			System.out.print("\t" + language);
		}

		for (int i = 0; i < models.size(); i++) {
			System.out.println();
			System.out.print(modelNames[i]);

			CTNgramTable pModel = models.get(i).getNgramTable(nGram, CTLanguageModel.SmoothingMethod.Plus1);

			for (int j = 0; j < models.size(); j++) {
				if (i == j) {
					System.out.print("\t\t" + FractionFormatter.format(0));
				} else {
					CTNgramTable qModel = models.get(j).getNgramTable(nGram, CTLanguageModel.SmoothingMethod.Plus1);

					double klDivergence = pModel.calculateKLDivergence(qModel);

					System.out.print("\t\t" + FractionFormatter.format(klDivergence));
				}

			}

		}
		System.out.println();

	}

	private static CTLanguageModel getLanguageModel(String path, String name) {
		File englishModel = new File(path, name.trim().toLowerCase() + ".lm");

		CTProcessor processor = new CTProcessor();
		InputUtil.process(englishModel, processor);

		return processor.getModel();
	}
}

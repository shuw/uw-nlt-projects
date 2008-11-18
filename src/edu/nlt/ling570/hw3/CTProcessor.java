package edu.nlt.ling570.hw3;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModel;
import edu.nlt.languageModels.cavnarTrenkle.CTLanguageModelBuilder;
import edu.nlt.util.LineProcessor;

/**
 * Processes language model output by Cavnar and Trenkle
 * 
 * @author shu
 * 
 */
public class CTProcessor implements LineProcessor {

	private static final Pattern pattern = Pattern.compile("([^\\s]+)[\\s]+([0-9]+)");

	private CTLanguageModelBuilder modelBuilder = new CTLanguageModelBuilder();

	@Override
	public void processLine(String inputLine) {
		Matcher matcher = pattern.matcher(inputLine);

		if (matcher.find()) {
			try {
				String value = matcher.group(1);
				int count = Integer.parseInt(matcher.group(2));

				modelBuilder.addValue(value, count);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

//			System.out.println(matcher.group(1) + " - " + matcher.group(2));
		} else {
			System.err.println("Could not parse: " + inputLine);
		}

	}

	public CTLanguageModel getModel() {
		return modelBuilder.buildModel();
	}
}
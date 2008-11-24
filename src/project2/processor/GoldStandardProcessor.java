package project2.processor;

import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.nlt.util.processor.LineProcessor;

public class GoldStandardProcessor implements LineProcessor {
	private final Pattern pattern = Pattern.compile("([0-9]+)[\\s]+(.*)[\\s]*");
	private Hashtable<String, Boolean> data = new Hashtable<String, Boolean>();

	@Override
	public void processLine(String value) {
		Matcher matcher = pattern.matcher(value);

		if (matcher.find()) {
			String fileName = matcher.group(1);
			boolean matched = matcher.group(2).equalsIgnoreCase("x");

			data.put(fileName.toLowerCase(), matched);

			// System.out.println(fileName + " ismatched == " + matched);

		} else {
			System.err.println("Could not parse: " + value);
		}

	}

	public GoldStandard getGoldStandard() {
		return new GoldStandard(data);
	}
}

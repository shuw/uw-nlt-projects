package edu.nlt.ling570.hw3;

import edu.nlt.util.processor.LineProcessor;

public class SGMTextProcessor implements LineProcessor {
	private static final String c_startTextTag = "<text";
	private static final String c_endTextTag = "</text";

	boolean isInStartTag = false;

	@Override
	public void processLine(String value) {

		if (value.toLowerCase().matches("^" + c_startTextTag + "[^>]+>")) {
			isInStartTag = true;
		} else if (value.toLowerCase().matches("^" + c_endTextTag + "[^>]+>")) {
			isInStartTag = false;
		} else if (value.startsWith("<")) {
			// ignore tags
		} else if (isInStartTag) {
			System.out.println(value);
			return;
		}
		

	}
}

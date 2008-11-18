package edu.nlt.ling570.project1;

import java.io.File;
import java.util.Collection;

import edu.nlt.formatter.LDC2004T03Formatter;
import edu.nlt.shallow.data.tags.Tag;

public class PrintViterbi {

	/**
	 * Prints tags for Standard Input
	 * 
	 * @param args
	 *            Argument[0] - Training file
	 * 
	 *            Argument[1] - File to run viterbi on
	 */
	public static void main(String[] args) {

		File trainingFile = new File(args[0]);

		File inputFile = new File(args[1]);

		// Run Viterbi
		//
		Collection<Tag> tags = ViterbiHelper.getTags(trainingFile, inputFile);

		// Print to Standard output
		//
		LDC2004T03Formatter formatter = new LDC2004T03Formatter();
		for (Tag tag : tags) {
			System.out.print(formatter.print(tag));
		}

	}

}

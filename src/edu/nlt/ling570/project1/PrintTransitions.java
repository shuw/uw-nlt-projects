package edu.nlt.ling570.project1;

import edu.nlt.ling570.project1.processor.TransitionProcessor;
import edu.nlt.util.InputUtil;

/**
 * Prints transision matrix
 * 
 * @author shu
 * 
 */
public class PrintTransitions {
	private static final boolean IgnoreEndOfWord = true;

	public static void main(String[] args) {

		TransitionProcessor processor = new TransitionProcessor(IgnoreEndOfWord);
		InputUtil.process(System.in, processor);

		processor.print(System.out);

	}
}


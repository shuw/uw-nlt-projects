package edu.nlt.ling570.project1;

import edu.nlt.ling570.project1.processor.EmissionProcessor;
import edu.nlt.util.InputUtil;

/**
 * Prints emission matrix
 * 
 * @author shu
 * 
 */
public class PrintEmissions {

	public static void main(String[] args) {

		EmissionProcessor processor = new EmissionProcessor();
		InputUtil.process(System.in, processor);

		processor.print(System.out);
	}

}


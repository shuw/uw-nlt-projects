package edu.nlt.ling570.project1;

import edu.nlt.ling570.project1.processor.TagTableProcessor;
import edu.nlt.util.InputUtil;

/**
 * Print Tags
 * 
 * @author shu
 * 
 */
public class PrintTags {

	public static void main(String[] args) {

		TagTableProcessor processor = new TagTableProcessor();
		InputUtil.process(System.in, processor);

		processor.print(System.out);
	}

}



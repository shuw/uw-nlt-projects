package project2;

import java.io.File;

import project2.processor.GoldStandard;

public class PrintTest {

	/**
	 * @param args
	 * 
	 *            args[1] - Files to classify
	 * 
	 *            args[0] - GoldStandard
	 * 
	 */
	public static void main(String[] args) {
		GoldStandard goldStandard = Util.getGoldStandard(new File(args[0]));

	}
}

package edu.nlt.ling570.hw3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Aggregates files and removes tags
 * 
 * @author shu
 * 
 */
public class PrepareText {
	/**
	 * @param args
	 *            args[0] Path to Portuguese Newswire corpus
	 * 
	 *            args[1..n] Year of data to use
	 */
	public static void main(String[] args) {

		int[] years = new int[args.length - 1];
		for (int i = 1; i < args.length; i++) {
			years[i - 1] = Integer.parseInt(args[i]);
		}

		processFiles(args[0], years);

	}

	private static SGMTextProcessor processor = new SGMTextProcessor();

	public static void processFiles(String path, int[] years) {

		for (int year : years) {

			File folder = new File(path);

			File[] files = folder.listFiles();

			for (File file : files) {

				if (file.getName().startsWith("afp" + year)) {
					procesFile(file);
				}
			}
		}
	}

	private static void procesFile(File file) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if (reader != null) {

			String line;
			try {
				do {

					line = reader.readLine();

					if (line != null) {
						processor.processLine(line);
					}

				} while (line != null);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

public class Util {

	public static double toLogBase2(double value) {
		return value != 0 ? (Math.log(value) / Math.log(2))
				: Double.NEGATIVE_INFINITY;

	}

	public static void processFiles(String path, StringProcessor processor) {
		File folder = new File(path);

		File[] files = folder.listFiles();

		// Process files alphabetically
		Arrays.sort(files, new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});

		for (File file : files) {

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

}

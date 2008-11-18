package edu.nlt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;

public class InputUtil {

	public static void processFiles(String path, LineProcessor processor) {
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

	public static void process(File file, LineProcessor processor) {

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
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

	public static void process(InputStream input, LineProcessor processor) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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

	public static void compareFiles(File file1, File file2, LineCompareProcessor processor) {

		BufferedReader reader1 = null;
		BufferedReader reader2 = null;
		try {
			reader1 = new BufferedReader(new FileReader(file1));
			reader2 = new BufferedReader(new FileReader(file2));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		if (reader1 != null && reader2 != null) {

			String line1, line2;

			try {
				do {

					line1 = reader1.readLine();
					line2 = reader2.readLine();

					if (line1 != null && line2 != null) {

						processor.processLine(line1, line2);
					}

				} while (line1 != null && line2 != null);

				if (line1 != null) {
					System.out.println("File1 finished prematurely");
				}
				if (line2 != null) {
					System.out.println("File2 finished prematurely");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}

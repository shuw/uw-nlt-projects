package edu.nlt.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import edu.nlt.util.processor.LineProcessor;

public class InputUtil {

	public static void processFiles(String path, FileProcessor processor) {

		Collection<File> files = FileUtil.getFiles(path, true);

		// int fileProcessedCount = 0;
		for (File file : files) {

			if (Globals.IsDebugEnabled) {
				// Only process 100 files to speed up development
				// if (fileProcessedCount++ > 100)
				// break;
			}

			if (Globals.IsDebugEnabled) {
				// System.out.println("processing: " + file.getName());
			}

			processor.processFile(file);
		}
	}

	public static void processFiles(String path, final LineProcessor processor) {

		processFiles(path, new FileProcessor() {

			@Override
			public void processFile(File file) {
				process(file, processor);

			}
		});

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

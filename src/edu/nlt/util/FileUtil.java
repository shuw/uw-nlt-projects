package edu.nlt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
	public static List<File> getFiles(String path, boolean sort) {
		List<File> files = Arrays.asList(new File(path).listFiles());

		if (sort) {

			ArrayList<NumberedFile> numberedFiles = new ArrayList<NumberedFile>();
			ArrayList<File> otherFiles = new ArrayList<File>();
			{

				for (File file : files) {

					String name = file.getName();

					if (name.contains(".")) {
						name = name.split("\\.")[0];
					}

					if (name.matches("^[0-9]+$")) {
						numberedFiles.add(new NumberedFile(file, Integer.parseInt(name)));
					} else {
						otherFiles.add(file);
					}

				}
			}

			// Process files numerically
			Collections.sort(numberedFiles, new Comparator<NumberedFile>() {

				@Override
				public int compare(NumberedFile o1, NumberedFile o2) {
					return o1.getOrder() - o2.getOrder();
				}
			});

			// Process files alphabetically
			Collections.sort(otherFiles, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});

			ArrayList<File> allFiles = new ArrayList<File>(numberedFiles.size() + otherFiles.size());

			for (NumberedFile file : numberedFiles) {
				allFiles.add(file.getFile());
			}

			for (File file : otherFiles) {
				allFiles.add(file);
			}

			files = allFiles;
		}

		return files;
	}
}

class NumberedFile {
	private File file;
	private int order;

	public NumberedFile(File file, int order) {
		super();
		this.file = file;
		this.order = order;
	}

	public File getFile() {
		return file;
	}

	public int getOrder() {
		return order;
	}

}

package edu.nlt.util;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FileUtil {
	public static List<File> getFiles(String path, boolean sort) {
		File[] files = new File(path).listFiles();

		if (sort) {
			// Process files alphabetically
			Arrays.sort(files, new Comparator<File>() {

				@Override
				public int compare(File o1, File o2) {
					return o1.getName().compareTo(o2.getName());
				}
			});
		}

		return Arrays.asList(files);
	}
}

package edu.nlt.ling570.hw5;

import java.io.File;

import edu.nlt.shallow.classifier.BinaryFileClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;

public class TrainingDataClassifier implements BinaryFileClassifier {

	public static final String EmailFolderName = "email";
	public static final String SpamFolderName = "spam";

	@Override
	public boolean isPositive(File file) throws NotClassifiedException {
		String folderName = file.getParentFile().getName();

		if (folderName.equals(EmailFolderName)) {
			return true;
		} else if (folderName.equals(SpamFolderName)) {
			return false;
		} else {
			throw new NotClassifiedException();
		}

	}

}
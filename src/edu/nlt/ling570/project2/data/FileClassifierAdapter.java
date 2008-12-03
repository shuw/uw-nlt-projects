package edu.nlt.ling570.project2.data;

import java.io.File;

import edu.nlt.shallow.classifier.BinaryClassifier;
import edu.nlt.shallow.classifier.BinaryFileClassifier;
import edu.nlt.shallow.classifier.NotClassifiedException;

public class FileClassifierAdapter implements BinaryFileClassifier {

	private BinaryClassifier binaryClassifier;

	public FileClassifierAdapter(BinaryClassifier binaryClassifier) {
		super();
		this.binaryClassifier = binaryClassifier;
	}

	@Override
	public boolean isPositive(File file) throws NotClassifiedException {
		return binaryClassifier.isPositive(file.getName());
	}

}

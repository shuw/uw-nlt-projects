package edu.nlt.shallow.classifier;

import java.io.File;

public interface BinaryFileClassifier {

	public boolean isPositive(File file) throws NotClassifiedException;

}

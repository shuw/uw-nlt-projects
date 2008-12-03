package edu.nlt.shallow.classifier;

public interface BinaryClassifier {
	public boolean isPositive(String objectName) throws NotClassifiedException;

}

package edu.nlt.tools;

import java.util.Collection;

public class CompareResult {

	private Collection<TagCompareResult> tagResults;

	private double accuracy;

	private int morphemeCount;

	public CompareResult(Collection<TagCompareResult> tagResults, double accuracy, int morphemeCount) {
		super();
		this.tagResults = tagResults;
		this.accuracy = accuracy;
		this.morphemeCount = morphemeCount;
	}

	public int getMorphemeCount() {
		return morphemeCount;
	}

	public double getAccuracy() {
		return accuracy;
	}

	public Collection<TagCompareResult> getTagResults() {
		return tagResults;
	}

}

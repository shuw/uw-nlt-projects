package edu.nlt.ling570.project2.data;

import edu.nlt.shallow.data.vector.DocumentVector;

public class DocumentResult {

	private DocumentVector document;
	private double score;

	public DocumentResult(DocumentVector document, double score) {
		super();
		this.document = document;
		this.score = score;
	}

	public DocumentVector getDocument() {
		return document;
	}

	public double getScore() {
		return score;
	}

}

package edu.nlt.shallow.data.vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class DocumentVector {

	private Hashtable<String, DocumentFeature> table;
	private String vectorName;

	public DocumentVector(Hashtable<String, DocumentFeature> table) {
		super();
		this.table = table;
	}

	public String getVectorName() {
		return vectorName;
	}

	public DocumentVector(Hashtable<String, DocumentFeature> table, String vectorName) {
		super();
		this.table = table;
		this.vectorName = vectorName;
	}

	public Collection<DocumentFeature> values() {
		return table.values();
	}

	public void print() {

		List<DocumentFeature> features = new ArrayList<DocumentFeature>(values());

		Collections.sort(features, new Comparator<DocumentFeature>() {

			@Override
			public int compare(DocumentFeature o1, DocumentFeature o2) {
				return Double.compare(o2.getMagnitude(), o1.getMagnitude());
			}

		});

		for (DocumentFeature feature : features) {
			System.out.println(feature.getWord() + "\t" + feature.getMagnitude());
		}

	}

}

package edu.nlt.ling570.project2.processor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.processor.LineProcessor;

public class VectorProcessor implements LineProcessor {

	private LinkedList<DocumentVector> vectors = new LinkedList<DocumentVector>();
	private Hashtable<String, DocumentFeature> currentVectorTable;
	private String currentVectorName;

	@Override
	public void processLine(String value) {
		if (value.startsWith("file:")) {
			pushVectorTable();

			currentVectorName = value.split("\t")[1];
			currentVectorTable = new Hashtable<String, DocumentFeature>();

		} else if (!value.startsWith("processing:") && value.length() > 0) {

			String[] components = value.split("\t");

			Word word = new Word(components[0]);
			double strength = Double.parseDouble(components[1]);

			currentVectorTable.put(word.getKey(), new DocumentFeature(word, strength));

		}
	}

	private void pushVectorTable() {
		if (currentVectorTable != null && currentVectorTable.size() > 0) {
			vectors.add(new DocumentVector(currentVectorTable, currentVectorName));
			currentVectorTable = null;
		}
	}

	public List<DocumentVector> getVectors() {
		pushVectorTable();
		return new ArrayList<DocumentVector>(vectors);
	}

}

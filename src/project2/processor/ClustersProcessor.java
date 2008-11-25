package project2.processor;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import project2.data.LinguisticCluster;
import project2.data.Vocabulary;
import edu.nlt.shallow.data.tags.Word;
import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.util.processor.LineProcessor;

public class ClustersProcessor implements LineProcessor {

	private LinkedList<LinguisticCluster> vectors = new LinkedList<LinguisticCluster>();

	private Vocabulary vocabulary;

	public ClustersProcessor(Vocabulary vocabulary) {
		super();
		this.vocabulary = vocabulary;
	}

	private Hashtable<String, DocumentFeature> currentVectorTable;
	private boolean currentVectorIsLinguistic;

	@Override
	public void processLine(String value) {
		if (value.startsWith("Cluster:")) {

			pushVectorTable();
			currentVectorTable = new Hashtable<String, DocumentFeature>();

			String clusterType = value.split("\\s")[1];

			if ("L".equals(clusterType)) {
				currentVectorIsLinguistic = true;
			} else if ("NL".equals(clusterType)) {
				currentVectorIsLinguistic = false;
			} else {
				(new Exception("Invalid cluster header")).printStackTrace(System.err);
			}

		} else if (!"".equals(value)) {
			String[] components = value.split("\t");

			Word word = new Word(components[0]);
			double strength = Double.parseDouble(components[1]);

			currentVectorTable.put(word.getKey(), new DocumentFeature(word, strength));

		}
	}

	private void pushVectorTable() {
		if (currentVectorTable != null) {
			vectors.add(new LinguisticCluster(currentVectorTable, vocabulary,
					currentVectorIsLinguistic));
			currentVectorTable = null;
		}
	}

	public List<LinguisticCluster> getVectors() {
		pushVectorTable();
		return new ArrayList<LinguisticCluster>(vectors);
	}
}
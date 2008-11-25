package project2.data;

import java.util.HashSet;
import java.util.Hashtable;

import edu.nlt.shallow.data.vector.DocumentFeature;
import edu.nlt.shallow.data.vector.DocumentVector;
import edu.nlt.util.VectorUtil;

public class LinguisticCluster {

	private Hashtable<String, DocumentFeature> table;

	private boolean isLinguistic;
	private Vocabulary vocabulary;

	private double[] vector;

	public LinguisticCluster(Hashtable<String, DocumentFeature> table, Vocabulary vocabulary,
			boolean isLinguistic) {
		super();
		this.table = table;
		this.isLinguistic = isLinguistic;
		this.vocabulary = vocabulary;

	}

	public double getCosineSimilarity(DocumentVector document) {

		if (vector == null) {
			vector = VectorUtil.getNormalizedVector(table.values(), vocabulary);
		}

		double[] documentVector = VectorUtil.getNormalizedVector(document.values(), vocabulary);

		return VectorUtil.dotProduct(vector, documentVector);

	}

	public boolean isLinguistic() {
		return isLinguistic;
	}

}

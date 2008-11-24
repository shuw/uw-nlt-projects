package edu.nlt.shallow.data.table;

import java.util.Hashtable;

import edu.nlt.shallow.data.Emission;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.tags.Tag;

public class EmissionTable {

	private Hashtable<String, Hashtable<String, Emission<Keyable>>> table;

	/**
	 * Smoothed zeroProbability
	 */
	private double zeroProbability;

	public EmissionTable(Hashtable<String, Hashtable<String, Emission<Keyable>>> table,
			double zeroProbability) {
		super();
		this.table = table;
		this.zeroProbability = zeroProbability;
	}

	public Emission<Keyable> getEmission(Tag tag, Keyable word) {

		Hashtable<String, Emission<Keyable>> emissionTable = table.get(tag.getTagKey());
		if (emissionTable != null) {
			Emission<Keyable> emission = emissionTable.get(word.getKey());
			if (emission != null) {
				return emission;
			}
		}

		// Return smoothed zero-probability
		//
		return new Emission<Keyable>(word, zeroProbability);

	}
}

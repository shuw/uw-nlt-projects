package edu.nlt.shallow.data.table;

import java.util.Hashtable;

import edu.nlt.shallow.data.Transition;
import edu.nlt.shallow.data.tags.LinkedTag;

public class TransitionTable<T extends LinkedTag> {
	private Hashtable<String, Transition<T>> table;
	private double zeroProbability;

	public TransitionTable(Hashtable<String, Transition<T>> table, double zeroProbability) {
		super();
		this.table = table;
		this.zeroProbability = zeroProbability;
	}

	public Transition<T> getTransition(T linkedTag) {
		Transition<T> rtnValue = table.get(linkedTag.getKey());

		if (rtnValue == null) {
			return new Transition<T>(linkedTag, zeroProbability);
		}
		return rtnValue;
	}

}

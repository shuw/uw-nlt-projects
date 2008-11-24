package edu.nlt.shallow.data.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.Transition;
import edu.nlt.shallow.data.table.CounterTable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.table.TagCounterTable;
import edu.nlt.shallow.data.table.TransitionTable;
import edu.nlt.shallow.data.tags.Bigram;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.Trigram;

public class TransitionTableBuilder {
	private final Hashtable<String, KeyCounterTable<Bigram>> bigramTable = new Hashtable<String, KeyCounterTable<Bigram>>();
	private final Hashtable<String, KeyCounterTable<Trigram>> trigramTable = new Hashtable<String, KeyCounterTable<Trigram>>();

	private final TagCounterTable<Tag> tagTable = new TagCounterTable<Tag>();

	private Tag prevTag;
	private Tag prevPrevTag;
	private int totalBigramCount = 0;
	private int totalTrigramCount = 0;

	public TransitionTableBuilder(Tag initialState) {
		super();
		this.prevTag = initialState;
	}

	public void addNext(Tag tag) {
		tagTable.add(tag);

		if (prevTag != null) {
			{
				KeyCounterTable<Bigram> counterTable = bigramTable.get(prevTag.getTagKey());

				if (counterTable == null) {
					counterTable = new KeyCounterTable<Bigram>();

					bigramTable.put(prevTag.getTagKey(), counterTable);
				}

				totalBigramCount++;
				counterTable.add(new Bigram(prevTag, tag));
			}

			if (prevPrevTag != null) {

				Bigram prevBigram = new Bigram(prevPrevTag, prevTag);
				KeyCounterTable<Trigram> counterTable = trigramTable.get(prevBigram.getTagKey());

				if (counterTable == null) {
					counterTable = new KeyCounterTable<Trigram>();

					trigramTable.put(prevBigram.getTagKey(), counterTable);
				}

				totalTrigramCount++;
				counterTable.add(new Trigram(prevTag, prevPrevTag, tag));

			}
		}

		prevPrevTag = prevTag;
		prevTag = tag;
	}

	public CounterTable<Tag> getTagCounters() {
		return tagTable;
	}

	public Collection<Tag> getTags() {
		ArrayList<Tag> tags = new ArrayList<Tag>(tagTable.values().size());

		for (CountHolder<Tag> tagCounter : tagTable.values()) {
			tags.add(tagCounter.getComponent());
		}
		return tags;

	}

	public Hashtable<String, Transition<Bigram>> getBigramTransitions() {
		Hashtable<String, Transition<Bigram>> transitions = new Hashtable<String, Transition<Bigram>>();

		for (KeyCounterTable<Bigram> bigramCounterTable : bigramTable.values()) {
			long totalCount = bigramCounterTable.getTotalCount();
			for (CountHolder<Bigram> bigramCounter : bigramCounterTable.values()) {
				double probability = (double) bigramCounter.getCount() / (double) totalCount;

				Bigram bigram = bigramCounter.getComponent();

				transitions.put(bigram.getKey(), new Transition<Bigram>(bigram, probability));

			}

		}
		return transitions;
	}

	public Hashtable<String, Transition<Trigram>> getTrigramTransitions() {
		Hashtable<String, Transition<Trigram>> transitions = new Hashtable<String, Transition<Trigram>>();

		for (KeyCounterTable<Trigram> counterTable : trigramTable.values()) {
			long totalCount = counterTable.getTotalCount();

			for (CountHolder<Trigram> counter : counterTable.values()) {
				double probability = (double) counter.getCount() / (double) totalCount;

				Trigram trigram = counter.getComponent();

				transitions.put(trigram.getKey(), new Transition<Trigram>(trigram, probability));

			}

		}
		return transitions;
	}

	public TransitionTable<Bigram> getBigramTable() {
		// Naive smoothing
		double zeroProbability = 1d / Math.pow(tagTable.getTotalCount(), 2);

		return new TransitionTable<Bigram>(getBigramTransitions(), zeroProbability);
	}

	public TransitionTable<Trigram> getTrigramTable() {
		// Naive smoothing
		double zeroProbability = 1d / Math.pow(tagTable.getTotalCount(), 3);

		return new TransitionTable<Trigram>(getTrigramTransitions(), zeroProbability);
	}

}

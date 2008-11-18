package edu.nlt.shallow.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.TaggedKeyable;

/**
 * Build Emissions Table with plus-one smoothing
 * 
 * TODO: Implement smoothing
 * 
 * @author shu
 * 
 */
public class EmissionTableBuilder {

	private final Hashtable<String, WordTable<Tag>> table = new Hashtable<String, WordTable<Tag>>();
	private final KeyCounterTable<Keyable> vocabulary = new KeyCounterTable<Keyable>();

	public void Add(TaggedKeyable<? extends Keyable> tagWord) {

		WordTable<Tag> tagWords = table.get(tagWord.getTag().getTagKey());

		if (tagWords == null) {
			tagWords = new WordTable<Tag>(tagWord.getTag());
			table.put(tagWord.getTag().getTagKey(), tagWords);
		}
		tagWords.Add(tagWord.getKeyable());

		vocabulary.add(tagWord.getKeyable());

	}

	public Hashtable<String, Emission<Keyable>> getEmissions(String tagKey) {

		WordTable<Tag> wordTable = table.get(tagKey);
		long totalCount = wordTable.getTotalCount();

		Collection<CountHolder<Keyable>> wordCounters = wordTable.values();
		Hashtable<String, Emission<Keyable>> emissions = new Hashtable<String, Emission<Keyable>>(
				wordCounters.size());

		for (CountHolder<Keyable> wordCounter : wordCounters) {
			double prob = (double) wordCounter.getCount() / (double) totalCount;

			emissions.put(wordCounter.getComponent().getKey(), new Emission<Keyable>(wordCounter
					.getComponent(), prob));

		}

		return emissions;
	}

	public EmissionTable getEmissionTable() {

		Hashtable<String, Hashtable<String, Emission<Keyable>>> emissionTableCore = new Hashtable<String, Hashtable<String, Emission<Keyable>>>();

		for (String tagKey : table.keySet()) {
			emissionTableCore.put(tagKey, getEmissions(tagKey));
		}

		return new EmissionTable(emissionTableCore, 1.0d / (double) vocabulary.getTotalCount());
	}

	public List<Tag> getTags() {
		ArrayList<Tag> list = new ArrayList<Tag>(table.size());

		for (WordTable<Tag> value : table.values()) {
			list.add(value.getComponent());

		}

		Collections.sort(list, new Comparator<Tag>() {

			@Override
			public int compare(Tag o1, Tag o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});

		return list;
	}

}

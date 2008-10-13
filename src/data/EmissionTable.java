package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class EmissionTable {

	final Hashtable<String, WordTableDecorator<Tag>> table = new Hashtable<String, WordTableDecorator<Tag>>();

	public void Add(TagWord tagWord) {
		WordTableDecorator<Tag> tagWords = table.get(tagWord.getTag().getKey());

		if (tagWords == null) {
			tagWords = new WordTableDecorator<Tag>(tagWord.getTag());
			table.put(tagWord.getTag().getKey(), tagWords);
		}
		tagWords.Add(tagWord.getWord());

	}

	public WordTableDecorator<Tag> getWordTable(Tag tag) {
		return table.get(tag.getKey());
	}

	public List<Tag> getTags() {
		ArrayList<Tag> list = new ArrayList<Tag>(table.size());

		for (WordTableDecorator<Tag> value : table.values()) {
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

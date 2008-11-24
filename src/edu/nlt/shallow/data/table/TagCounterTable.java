package edu.nlt.shallow.data.table;

import edu.nlt.shallow.data.tags.Tag;

public class TagCounterTable<T extends Tag> extends CounterTable<T> {

	@Override
	protected String getKey(T t) {
		return t.getTagKey();
	}
}

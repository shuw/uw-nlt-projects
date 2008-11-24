package edu.nlt.shallow.data.table;

import edu.nlt.shallow.data.Keyable;

public class KeyCounterTable<T extends Keyable> extends CounterTable<T> {

	@Override
	protected String getKey(T t) {
		return t.getKey();
	}

}

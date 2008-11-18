package edu.nlt.shallow.data;

public class KeyCounterTable<T extends Keyable> extends CounterTable<T> {

	@Override
	protected String getKey(T t) {
		return t.getKey();
	}

}

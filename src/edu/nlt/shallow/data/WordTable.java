package edu.nlt.shallow.data;

import java.util.Collection;

public class WordTable<T extends Keyable> {

	private KeyCounterTable<Keyable> table = new KeyCounterTable<Keyable>();
	private T keyable;

	public WordTable(T keyable) {
		this.keyable = keyable;
	}

	public void Add(Keyable word) {

		table.add(word);
	}

	public T getComponent() {
		return keyable;
	}

	public int getCount(Keyable word) {
		return table.getCount(word);
	}

	public long getTotalCount() {
		return table.getTotalCount();
	}

	public Collection<CountHolder<Keyable>> values() {
		return table.values();
	}

}

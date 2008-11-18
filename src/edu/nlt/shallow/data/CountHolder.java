package edu.nlt.shallow.data;

public class CountHolder<T> {
	private T component;
	private int count;

	public CountHolder(T component) {
		super();
		this.component = component;
	}

	public T getComponent() {
		return component;
	}

	public Integer getCount() {
		return count;
	}

	public void increment() {
		count++;
	}

}

package data;

public class CountDecorator<T> {
	private T component;
	private int count;

	public CountDecorator(T component) {
		super();
		this.component = component;
	}

	public void increment() {
		count++;
	}

	public T getComponent() {
		return component;
	}

	public Integer getCount() {
		return count;
	}

}

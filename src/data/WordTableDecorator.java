package data;

public class WordTableDecorator<T extends Keyable> {

	private CounterTable<Word> table = new CounterTable<Word>();
	private T keyable;

	public WordTableDecorator(T keyable) {
		this.keyable = keyable;
	}

	public CounterTable<Word> getTable() {
		return table;
	}

	public T getComponent() {
		return keyable;
	}

	public int getCount(Word word) {
		return table.getCount(word);
	}
	
	public long getTotalCount()
	{
		return table.getTotalCount();
	}

	public void Add(Word word) {

		table.add(word);
	}

}

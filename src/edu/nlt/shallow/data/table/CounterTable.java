package edu.nlt.shallow.data.table;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import edu.nlt.shallow.data.CountHolder;

public abstract class CounterTable<T> {
	private Hashtable<String, CountHolder<T>> table = new Hashtable<String, CountHolder<T>>();

	protected Hashtable<String, CountHolder<T>> getTable() {
		return table;
	}

	private long totalCount = 0;

	protected abstract String getKey(T t);

	public void add(T value) {

		CountHolder<T> counter = table.get(getKey(value));

		if (counter == null) {
			counter = new CountHolder<T>(value);
			table.put(getKey(value), counter);
		}

		counter.increment();
		totalCount++;

	}

	public int getCount(T value) {
		CountHolder<T> counter = table.get(getKey(value));

		return counter != null ? counter.getCount() : 0;
	}

	public Collection<CountHolder<T>> values() {
		return table.values();
	}

	public Collection<CountHolder<T>> getReverseSorted() {
		ArrayList<CountHolder<T>> counters = new ArrayList<CountHolder<T>>(table.values());

		Collections.sort(counters, new Comparator<CountHolder<?>>() {

			@Override
			public int compare(CountHolder<?> o1, CountHolder<?> o2) {
				return o2.getCount().compareTo(o1.getCount());
			}

		});
		return counters;
	}

	public List<T> getTop(int count, boolean sortResultAlphabetically) {

		// get the top 10 entries
		ArrayList<T> list = new ArrayList<T>(count);
		for (CountHolder<T> counter : getReverseSorted()) {
			if (list.size() < count)
				list.add(counter.getComponent());
			else
				break;
		}

		if (sortResultAlphabetically) {
			Collections.sort(list, new Comparator<T>() {

				@Override
				public int compare(T o1, T o2) {
					return getKey(o1).compareTo(getKey(o2));
				}
			});
		}

		return list;

	}

	public long getTotalCount() {
		return totalCount;
	}

	public void printEntries(PrintStream out, int numToShow) {
		int i = 0;
		for (CountHolder<T> count : getReverseSorted()) {
			if (++i <= numToShow) {
				out.println(getKey(count.getComponent()) + "\t" + count.getCount());
			} else {
				break;
			}
		}
	}

}
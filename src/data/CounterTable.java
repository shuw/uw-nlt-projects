package data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

public class CounterTable<T extends Keyable> {
	private Hashtable<String, CountDecorator<T>> table = new Hashtable<String, CountDecorator<T>>();

	private long totalCount = 0;

	public void add(T value) {

		CountDecorator<T> counter = table.get(value.getKey());

		if (counter == null) {
			counter = new CountDecorator<T>(value);
			table.put(value.getKey(), counter);
		}

		counter.increment();
		totalCount++;

	}

	public List<T> getTop(int count, boolean sortResultAlphabetically) {

		// get the top 10 entries
		ArrayList<T> list = new ArrayList<T>(count);
		for (CountDecorator<T> counter : getReverseSorted()) {
			if (list.size() < count)
				list.add(counter.getComponent());
			else
				break;
		}

		if (sortResultAlphabetically) {
			Collections.sort(list, new Comparator<T>() {

				@Override
				public int compare(T o1, T o2) {
					return o1.getKey().compareTo(o2.getKey());
				}
			});
		}

		return list;

	}

	public Collection<CountDecorator<T>> getReverseSorted() {
		ArrayList<CountDecorator<T>> counters = new ArrayList<CountDecorator<T>>(
				table.values());

		Collections.sort(counters, new Comparator<CountDecorator<?>>() {

			@Override
			public int compare(CountDecorator<?> o1, CountDecorator<?> o2) {
				return o2.getCount().compareTo(o1.getCount());
			}

		});
		return counters;
	}

	public int getCount(T value) {
		CountDecorator<T> counter = table.get(value.getKey());

		return counter != null ? counter.getCount() : 0;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void printEntries(PrintStream out, int numToShow) {
		int i = 0;
		for (CountDecorator<T> count : getReverseSorted()) {
			if (++i <= numToShow) {
				out.println(count.getComponent().getKey() + "\t"
						+ count.getCount());
			} else {
				break;
			}
		}
	}
}
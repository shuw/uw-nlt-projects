package hw1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * Word frequency counter
 * 
 * @author Shu Wu, shuwu83@gmail.com
 * 
 */
public class hw1b {

	/**
	 * Calculates R^2 from Input Corpus TODO: Optimize sort array
	 */
	public static void main(String[] args) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		WordFrequencyCounter wfc = new WordFrequencyCounter();

		String line;
		try {
			do {
				line = reader.readLine();

				if (line != null) {
					wfc.processLine(line);
				}

			} while (line != null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		wfc.printFrequencies(System.out, 1500);

	}

}

class WordFrequencyCounter {

	private Hashtable<String, Integer> dictionary = new Hashtable<String, Integer>();

	public WordFrequencyCounter() {
		super();
	}

	private void CountWord(String word) {
		Integer count = dictionary.get(word);
		if (count == null) {
			count = new Integer(0);
		}
		count++;

		dictionary.put(word, count);
	}

	@SuppressWarnings("unchecked")
	public void printFrequencies(PrintStream out, int numOfResults) {
		Entry<String, Integer>[] entrySet = (Entry<String, Integer>[]) dictionary
				.entrySet().toArray(new Entry[] {});

		
		int maxValue = entrySet.length < 1500 ? entrySet.length : 1500;

		for (int i = 0; i < maxValue; i++) {
			Entry<String, Integer> entry = entrySet[i];
			out.println(entry.getKey() + "\t" + entry.getValue());
		}
	}

	public void processLine(String value) {
		if (value.length() > 0) {
			if (!value.matches("^<.*>$")) {
				String[] words = value.split(" ");
				for (String word : words) {

					word = word.replaceFirst("^[^A-Za-z0-9]*", "");
					word = word.replaceFirst("[^A-Za-z0-9]*$", "");
					if (word.length() > 0) {
						CountWord(word);
					}
				}

			}

		}

	}

}

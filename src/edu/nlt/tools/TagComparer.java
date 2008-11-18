package edu.nlt.tools;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.Tag.TagType;

public class TagComparer {
	/**
	 * Compares tags with gold standard, highlights errors, and prints report
	 * 
	 * @param tags
	 *            Tags
	 * @param goldTags
	 *            Tags - gold standard
	 */
	public static CompareResult compare(Collection<Tag> tags, Collection<Tag> goldTags) {
		ArrayList<TagCompareResult> tagResults = new ArrayList<TagCompareResult>(tags.size());

		int matchCount = 0;

		Iterator<Tag> goldTagsIterator = goldTags.iterator();

		boolean incrementGoldTag = true;
		int totalCount = 0;

		int tagCount = 0;
		if (goldTagsIterator.hasNext()) {
			Tag goldTag = null;

			for (Tag testTag : tags) {

				if (incrementGoldTag) {
					if (goldTagsIterator.hasNext()) {
						goldTag = goldTagsIterator.next();
					} else {
						break;
					}
				}
				incrementGoldTag = true;

				boolean isAccurate = testTag.getTagKey().equals(goldTag.getTagKey());

				tagResults.add(new TagCompareResult(testTag, isAccurate));

				if (testTag.getType() == TagType.NORMAL) {
					totalCount++;

					if (isAccurate) {
						matchCount++;
					}
				}

				if (testTag.getType() == TagType.END_OF_SENTENCE
						&& goldTag.getType() != TagType.END_OF_SENTENCE) {

					// If mismatch && END-OF-Sentence has been reached, sync
					// up at next sentence
					//

					while (goldTagsIterator.hasNext()) {
						goldTag = goldTagsIterator.next();
						if (goldTag.getType() == TagType.END_OF_SENTENCE) {
							break;
						}
					}

				} else if (goldTag.getType() == TagType.END_OF_SENTENCE
						&& testTag.getType() != TagType.END_OF_SENTENCE) {

					incrementGoldTag = false;
				}

				tagCount++;
			}
		}

		double accuracy = (double) matchCount / (double) totalCount;

		return new CompareResult(tagResults, accuracy, totalCount);
	}
}

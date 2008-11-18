package edu.nlt.util;

import java.util.ArrayList;
import java.util.Collection;

import edu.nlt.shallow.data.tags.DefaultTag;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.data.tags.Word;

public class PTBParser {
	public static Collection<TaggedKeyable<Word>> parseTags(String value) {
		String[] components = value.split(" ");

		ArrayList<TaggedKeyable<Word>> tags = new ArrayList<TaggedKeyable<Word>>(2);

		for (String component : components) {
			String[] typeTagPart = component.split("/");

			if (typeTagPart.length == 2) {
				tags.add(new TaggedKeyable<Word>(new DefaultTag(typeTagPart[1]), new Word(
						typeTagPart[0])));

			}

		}
		return tags;

	}
}

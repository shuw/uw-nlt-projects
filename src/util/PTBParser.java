package util;

import java.util.ArrayList;
import java.util.Collection;

import data.Tag;
import data.TagWord;
import data.Word;

public class PTBParser {
	public static Collection<TagWord> parseTags(String value) {
		String[] components = value.split(" ");

		ArrayList<TagWord> tags = new ArrayList<TagWord>(2);

		for (String component : components) {
			String[] typeTagPart = component.split("/");

			if (typeTagPart.length == 2) {
				tags.add(new TagWord(new Tag(typeTagPart[1]), new Word(
						typeTagPart[0])));

			}

		}
		return tags;

	}
}

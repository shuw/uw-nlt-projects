package edu.nlt.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import edu.nlt.shallow.data.tags.DefaultTag;
import edu.nlt.shallow.data.tags.EOWTag;
import edu.nlt.shallow.data.tags.Morpheme;
import edu.nlt.shallow.data.tags.PeriodTag;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.data.tags.UnknownTag;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;

/**
 * Annotation parser for Morphologically Annotated Korean Text
 * 
 * http://www.ldc.upenn.edu/Catalog/CatalogEntry.jsp?catalogId=LDC2004T03
 */
public class LDC2004T03Parser implements TagParser {

	public static final String EOSToken = "^EOS";

	/**
	 * @param word
	 *            Morphologically annoted korean word
	 * @return
	 * @throws ParserException
	 */
	private Collection<TaggedKeyable<Morpheme>> getMorphemeTags(String word) throws ParserException {

		String[] morphemes = word.split("\\+");

		ArrayList<TaggedKeyable<Morpheme>> tags = new ArrayList<TaggedKeyable<Morpheme>>(
				morphemes.length + 1);

		if (word.startsWith("+")) {
			tags.add(getTag(word));
			return tags;
		}

		for (String morpheme : morphemes) {

			// Handle ++ typo
			//
			if (morpheme.equals("")) {
				break;
			}

			tags.add(getTag(morpheme));
		}

		return tags;
	}

	private TaggedKeyable<Morpheme> getTag(String morpheme) throws ParserException {

		String[] components = morpheme.split("/");

		if (components.length == 2) {
			return new TaggedKeyable<Morpheme>(new DefaultTag(components[1]), new Morpheme(
					components[0]));

		} else {
			return new TaggedKeyable<Morpheme>(new UnknownTag(), new Morpheme(morpheme));
		}

	}

	@Override
	public Collection<Tag> getTags(String value) throws ParserException {
		String[] components = value.split("\t");

		LinkedList<Tag> tags = new LinkedList<Tag>();

		if (components.length == 1) {

			if (value.equals(EOSToken)) {
				tags.add(new PeriodTag());
				tags.add(new EOWTag());

			} else if (value.equals("")) {
				// do nothing
			} else {

				for (TaggedKeyable<Morpheme> tag : getMorphemeTags(components[0])) {
					tags.add(tag);
				}
				tags.add(new EOWTag());
			}
		} else if (components.length == 2) {

			try {
				// Handle plus symbol
				if (components[0].startsWith("+")) {
					tags.add(getTag(components[1]));
				} else {

					for (TaggedKeyable<Morpheme> tag : getMorphemeTags(components[1])) {
						tags.add(tag);
					}
				}
			} catch (ParserException ex) {
				throw new ParserException(components[1]);
			}

			tags.add(new EOWTag());

		} else {
			throw new ParserException(value);
		}

		return tags;
	}
}

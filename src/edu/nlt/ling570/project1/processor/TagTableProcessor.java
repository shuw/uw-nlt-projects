package edu.nlt.ling570.project1.processor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.CountHolder;
import edu.nlt.shallow.data.TagCounterTable;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.Tag.TagType;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.Globals;
import edu.nlt.util.LineProcessor;

public class TagTableProcessor implements LineProcessor {
	private TagCounterTable<Tag> tagTable = new TagCounterTable<Tag>();

	private final TagParser parser = new LDC2004T03Parser();

	public void print(PrintStream out) {
		Collection<CountHolder<Tag>> tags = tagTable.getReverseSorted();

		for (CountHolder<Tag> tagCounter : tags) {
			TagType type = tagCounter.getComponent().getType();
			if (type != TagType.END_OF_WORD && type != TagType.UNKNOWN) {
				out.println(tagCounter.getComponent().getTagKey());
			}
		}
	}

	public List<Tag> getTags() {

		Collection<CountHolder<Tag>> tagCounters = tagTable.getReverseSorted();

		ArrayList<Tag> tags = new ArrayList<Tag>(tagCounters.size());

		for (CountHolder<Tag> tagCounter : tagCounters) {
			tags.add(tagCounter.getComponent());
		}

		return tags;

	}

	@Override
	public void processLine(String value) {

		try {
			Collection<Tag> tags = parser.getTags(value);

			for (Tag tag : tags) {
				tagTable.add(tag);
			}

		} catch (ParserException e) {
			if (Globals.IsDebugEnabled) {
				e.printStackTrace();
			}
		}
	}

}
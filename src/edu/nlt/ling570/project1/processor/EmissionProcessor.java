package edu.nlt.ling570.project1.processor;

import java.io.PrintStream;
import java.util.Collection;

import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.Emission;
import edu.nlt.shallow.data.EmissionTable;
import edu.nlt.shallow.data.EmissionTableBuilder;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.Globals;
import edu.nlt.util.LineProcessor;
import edu.nlt.util.Singletons;

public class EmissionProcessor implements LineProcessor {
	private final TagParser parser = new LDC2004T03Parser();

	private final EmissionTableBuilder tableBuilder = new EmissionTableBuilder();

	public EmissionTable getEmissionTable() {
		return tableBuilder.getEmissionTable();
	}

	public void print(PrintStream out) {

		Collection<Tag> tags = tableBuilder.getTags();

		for (Tag tag : tags) {

			Collection<Emission<Keyable>> emissions = tableBuilder.getEmissions(tag.getTagKey())
					.values();

			for (Emission<Keyable> emission : emissions) {
				out.println(emission.getWord() + "|" + tag + " "
						+ Singletons.FractionFormatter.format(emission.getProbability()));
			}
		}

	}

	@Override
	public void processLine(String value) {

		try {

			Collection<Tag> tags = parser.getTags(value);

			for (Tag tag : tags) {
				if (tag instanceof TaggedKeyable) {
					tableBuilder.Add((TaggedKeyable<?>) tag);
				}
			}

		} catch (ParserException e) {
			if (Globals.IsDebugEnabled) {
				throw new RuntimeException(e);
			}
		}

	}

}

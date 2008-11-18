package edu.nlt.ling570.project1.processor;

import java.io.PrintStream;
import java.util.Collection;

import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.Transition;
import edu.nlt.shallow.data.TransitionTable;
import edu.nlt.shallow.data.TransitionTableBuilder;
import edu.nlt.shallow.data.tags.Bigram;
import edu.nlt.shallow.data.tags.PeriodTag;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.Tag.TagType;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.shallow.parser.TagParser;
import edu.nlt.util.Globals;
import edu.nlt.util.LineProcessor;
import edu.nlt.util.Singletons;

public class TransitionProcessor implements LineProcessor {
	final TagParser parser = new LDC2004T03Parser();
	private TransitionTableBuilder transitionTableBuilder = new TransitionTableBuilder(
			new PeriodTag());

	public TransitionTable getTransitionTable() {
		return transitionTableBuilder.getBigramTable();
	}

	private boolean ignoreEndOfWords;

	public TransitionProcessor(boolean ignoreEndOfWords) {
		super();
		this.ignoreEndOfWords = ignoreEndOfWords;
	}

	public void print(PrintStream out) {

		for (Transition<Bigram> transition : transitionTableBuilder.getBigramTransitions().values()) {

			Bigram bigram = transition.getLinkedTag();

			out.println(bigram.getTag2().getTagKey() + "|" + bigram.getTag1().getTagKey() + " "
					+ Singletons.FractionFormatter.format(transition.getProbability()));

		}
	}

	@Override
	public void processLine(String value) {

		try {
			Collection<Tag> tags = parser.getTags(value);

			for (Tag tag : tags) {
				if (!(ignoreEndOfWords && (tag.getType() == TagType.END_OF_WORD))) {
					transitionTableBuilder.addNext(tag);
				}
			}

		} catch (ParserException e) {
			if (Globals.IsDebugEnabled) {
				throw new RuntimeException(e);
			}

		}
	}
}

package edu.nlt.ling570.project1;

import java.io.File;
import java.util.Collection;

import edu.nlt.algorithm.Tagger;
import edu.nlt.algorithm.ViterbiAlgorithm;
import edu.nlt.ling570.project1.processor.EmissionProcessor;
import edu.nlt.ling570.project1.processor.TagTableProcessor;
import edu.nlt.ling570.project1.processor.TaggerProcessor;
import edu.nlt.ling570.project1.processor.TransitionProcessor;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.table.KeyCounterTable;
import edu.nlt.shallow.data.tags.Morpheme;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.parser.LDC2004T03Parser;
import edu.nlt.shallow.parser.ParserException;
import edu.nlt.util.InputUtil;
import edu.nlt.util.LineProcessor;

public class ViterbiHelper {
	public static Collection<Tag> getTags(File trainingFile, File inputFile) {
		Tagger tagger = getViterbiTagger(trainingFile);

		// Run Viterbi Tagger
		//
		TaggerProcessor taggerProcessor = new TaggerProcessor(tagger);
		InputUtil.process(inputFile, taggerProcessor);

		return taggerProcessor.getTotalTags();
	}

	public static Tagger getViterbiTagger(File trainingFile) {
		// Prepare training data
		//
		TransitionProcessor transitionProcessor = new TransitionProcessor(true);
		InputUtil.process(trainingFile, transitionProcessor);

		EmissionProcessor emissionProcessor = new EmissionProcessor();
		InputUtil.process(trainingFile, emissionProcessor);

		TagTableProcessor tagProcessor = new TagTableProcessor();
		InputUtil.process(trainingFile, tagProcessor);

		final KeyCounterTable<Keyable> vocabulary = new KeyCounterTable<Keyable>();

		InputUtil.process(trainingFile, new LineProcessor() {
			LDC2004T03Parser parser = new LDC2004T03Parser();

			@Override
			public void processLine(String value) {
				Collection<Tag> tags;
				try {
					tags = parser.getTags(value);

					for (Tag tag : tags) {
						if (tag.getComponent() != null) {
							vocabulary.add(new Morpheme(tag.getComponent().toString()));
						}
					}
				} catch (ParserException e) {
					e.printStackTrace();
				}
			}
		});

		return new ViterbiAlgorithm(emissionProcessor.getEmissionTable(), transitionProcessor
				.getTransitionTable(), tagProcessor.getTags());

	}
}

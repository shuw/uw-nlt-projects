package edu.nlt.ling570.project1.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import edu.nlt.algorithm.Tagger;
import edu.nlt.parser.LDC2004T03Parser;
import edu.nlt.shallow.data.tags.EOWTag;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.util.LineProcessor;

/**
 * Viterbi Algorithm
 * 
 * @author shu
 * 
 */
public class TaggerProcessor implements LineProcessor {

	private LinkedList<String> sentenceWithWordBreaks = new LinkedList<String>();

	private Tagger tagger;
	private LinkedList<Tag> totalTags = new LinkedList<Tag>();

	public TaggerProcessor(Tagger tagger) {
		super();
		this.tagger = tagger;
	}

	public Collection<Tag> getTotalTags() {
		return totalTags;
	}

	@Override
	public void processLine(String value) {
		
		{
			if (!value.startsWith("+")) {
				String words[] = value.split("\\+");

				for (String morpheme : words) {
					sentenceWithWordBreaks.add(morpheme);
				}
			} else {
				sentenceWithWordBreaks.add(value);
			}

			sentenceWithWordBreaks.add("\n"); // signifies work-break
		}

		if (value.equals(LDC2004T03Parser.EOSToken)) {

			ArrayList<String> sentence = new ArrayList<String>(sentenceWithWordBreaks.size());

			// Remove word-breaks
			//
			for (String token : sentenceWithWordBreaks) {
				if (!token.equals("\n")) {
					sentence.add(token);
				}
			}
			// Run viterbi on sentence
			//
			List<Tag> tags = tagger.getTags(sentence);

			// Add word breaks back in
			//
			int j = 0;
			for (String token : sentenceWithWordBreaks) {
				if (token.equals("\n")) {
					totalTags.add(new EOWTag());
				} else {
					totalTags.add(tags.get(j++));
				}
			}

			sentenceWithWordBreaks.clear();
		}
	}

}

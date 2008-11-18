package edu.nlt.algorithm;

import java.util.ArrayList;
import java.util.List;

import edu.nlt.shallow.data.Emission;
import edu.nlt.shallow.data.EmissionTable;
import edu.nlt.shallow.data.Keyable;
import edu.nlt.shallow.data.TransitionTable;
import edu.nlt.shallow.data.tags.Bigram;
import edu.nlt.shallow.data.tags.Morpheme;
import edu.nlt.shallow.data.tags.PeriodTag;
import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.TaggedKeyable;
import edu.nlt.shallow.data.tags.UnknownTag;
import edu.nlt.shallow.data.tags.Tag.TagType;

public class ViterbiAlgorithm implements Tagger {

	private EmissionTable emissionTable;

	private TransitionTable<Bigram> bigramTable;

	private List<Tag> tags;
	private static final boolean gotoEOSEndState = true;

	public ViterbiAlgorithm(EmissionTable emissionTable, TransitionTable<Bigram> bigramTable,
			List<Tag> tagSet) {
		super();
		this.emissionTable = emissionTable;
		this.bigramTable = bigramTable;
		init(tagSet);
	}

	private void init(List<Tag> tagSet) {
		ArrayList<Tag> filteredTags = new ArrayList<Tag>(tagSet.size() + 1);
		filteredTags.add(new PeriodTag());

		for (Tag tag : tagSet) {
			if (tag.getType() == TagType.NORMAL) {
				filteredTags.add(tag);
			}

		}

		this.tags = filteredTags;

	}

	@Override
	public List<Tag> getTags(List<String> tokens) {

		// Trellis probabilities in Log format
		ArrayList<double[]> viterbi = new ArrayList<double[]>(tokens.size() + 1);

		ArrayList<int[]> bestState = new ArrayList<int[]>(tokens.size() + 1);

		// Initial state is 0 (Period)
		//
		// Initial viterbi probability is 0 for State 0, Negative
		// infinity for all else
		//
		double[] initialViterbiState = new double[tags.size()];
		int[] initialBestState = new int[tags.size()];

		for (int i = 1; i < tags.size(); i++) {

			initialViterbiState[i] = Double.NEGATIVE_INFINITY;
		}

		viterbi.add(initialViterbiState);
		bestState.add(initialBestState);

		// This is the "induction" phase
		// process all input words/morphemes
		for (int i = 0; i < tokens.size(); i++) {
			viterbi.add(new double[tags.size()]);
			bestState.add(new int[tags.size()]);

			Morpheme morpheme = new Morpheme(tokens.get(i));

			for (int j = 0; j < tags.size(); j++) {
				Tag currentTag = tags.get(j);
				double maxProb = Double.NEGATIVE_INFINITY;
				int maxState = 0;

				for (int k = 0; k < tags.size(); k++) {

					Tag previousTag = tags.get(k);

					// Naive smoothing
					double emissionProbability = 0;
					// if (currentTag.getType() != TagType.END_OF_SENTENCE
					// && vocabulary.getCount(morpheme) == 0) {
					//
					// emissionProbability = (double) Math.sqrt(k);
					//
					// } else {
					Emission<Keyable> emission = emissionTable.getEmission(currentTag, morpheme);
					emissionProbability = emission.getProbability();
					// }

					double transitionProbability = bigramTable.getTransition(
							new Bigram(previousTag, currentTag)).getProbability();

					double logProb = viterbi.get(i)[k] + Math.log(emissionProbability)
							+ Math.log(transitionProbability);

					if (logProb > maxProb) {
						maxProb = logProb;
						maxState = k;
					}

				}
				viterbi.get(i + 1)[j] = maxProb;
				bestState.get(i + 1)[j] = maxState;

			}

		}

		// Termination and path-readout
		//

		int[] bestPath = new int[tokens.size() + 1];

		if (gotoEOSEndState) {
			// Always choose EOS End state
			//
			bestPath[tokens.size()] = 0;
		} else {
			// Use End State with highes probability
			//
			double maxProb = Double.NEGATIVE_INFINITY;
			int maxState = 0;
			for (int j = 0; j < tags.size(); j++) {

				double prob = viterbi.get(viterbi.size() - 1)[j];

				if (prob > maxProb) {
					maxProb = prob;
					maxState = j;
				}
			}

			bestPath[tokens.size()] = maxState;
		}

		for (int j = tokens.size(); j >= 1; j--) {
			bestPath[j - 1] = bestState.get(j)[bestPath[j]];
		}

		ArrayList<Tag> taggedKeys = new ArrayList<Tag>(tokens.size());

		for (int j = 0; j < tokens.size(); j++) {
			Tag tag = tags.get(bestPath[j + 1]);

			// End of sentence tag can only occur in end-state
			if (j != tokens.size() - 1 && tag.getType() == TagType.END_OF_SENTENCE) {
				tag = new UnknownTag();
			}

			// if (tag.getType() != TagType.END_OF_SENTENCE
			// && vocabulary.getCount(new Morpheme(tokens.get(j))) == 0) {
			// tag = tags.get(1);
			// }

			taggedKeys.add(new TaggedKeyable<Keyable>(tag, new Morpheme(tokens.get(j))));
		}
		return taggedKeys;

	}
}

package edu.nlt.algorithm;

import java.util.List;

import edu.nlt.shallow.data.tags.Tag;

public interface Tagger {
	public List<Tag> getTags(List<String> tokens);

}

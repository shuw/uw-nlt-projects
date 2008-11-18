package edu.nlt.shallow.data.tags;

import edu.nlt.shallow.data.Keyable;

public interface Tag extends Keyable {

	public String getTagKey();

	public TagType getType();

	public enum TagType {
		NORMAL, END_OF_WORD, END_OF_SENTENCE, UNKNOWN

	}

	public Object getComponent();

}

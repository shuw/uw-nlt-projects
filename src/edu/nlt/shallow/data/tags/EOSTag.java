package edu.nlt.shallow.data.tags;

/**
 * End of Sentence Tag.
 * 
 * @author shu
 * 
 */
public abstract class EOSTag implements Tag {

	@Override
	public String getKey() {
		return getTagKey();
	}

	@Override
	public abstract String getTagKey();

	@Override
	public TagType getType() {
		return TagType.END_OF_SENTENCE;
	}

}
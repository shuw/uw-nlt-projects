package edu.nlt.shallow.data.tags;


/**
 * End of Word Tag. Used for Agglutinating languages.
 * 
 * @author shu
 * 
 */
public class EOWTag implements Tag {

	@Override
	public String getKey() {
		return getTagKey();
	}

	@Override
	public String getTagKey() {
		return "EOW";
	}

	@Override
	public TagType getType() {
		return TagType.END_OF_WORD;
	}

	@Override
	public Object getComponent() {
		return null;
	}
}

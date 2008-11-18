package edu.nlt.formatter;

import edu.nlt.shallow.data.tags.Tag;
import edu.nlt.shallow.data.tags.Tag.TagType;
import edu.nlt.shallow.parser.LDC2004T03Parser;
import edu.nlt.tools.TagCompareResult;

public class LDC2004T03Formatter {
	public static String newline = System.getProperty("line.separator");
	int morphemeCount = 0;

	public String print(Tag tag) {
		return print(tag, true, false);
	}

	public String print(Tag tag, boolean isCorrect, boolean hideTagKey) {

		if (tag.getType() == TagType.END_OF_WORD) {
			morphemeCount = 0;
			return newline;
		} else {
			String rtnValue = "";

			if (morphemeCount++ > 0) {
				rtnValue += "+";
			}

			if (tag.getType() == TagType.END_OF_SENTENCE) {
				return LDC2004T03Parser.EOSToken;
			}

			else if (tag.getComponent() != null) {
				rtnValue += tag.getComponent().toString();
				if (!hideTagKey) {

					rtnValue += "/";

					if (isCorrect) {
						rtnValue += tag.getTagKey();
					} else {
						rtnValue += "**" + tag.getTagKey() + "**";
					}
				}
			}

			return rtnValue;
		}

	}

	public String print(TagCompareResult tagCompareResult) {

		return print(tagCompareResult.getTag(), tagCompareResult.isCorrect(), false);

	}
}

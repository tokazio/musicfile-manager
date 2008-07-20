package info.mp3lib.core;

public enum TagEnum {
	ALL_SAME_TAGS(16), // all track are tagged with the same tag
	ALL_DIFF_TAGS(8), // all track are tagged but at less two of them have different tag
	SOME_SAME_TAGS(4), // some track are tagged with the same tag
	SOME_DIFF_TAGS(2), // some track are tagged but at less two of them have different tag
	NO_TAGS(0); // no track is tagged

	private int value;

	private TagEnum(final int pValue) {
		value = pValue;
	}
	
}

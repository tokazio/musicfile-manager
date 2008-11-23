package info.mp3lib.core;

public enum TagEnum {
	/**
	 * all track are tagged with the same tag
	 */
	ALL_SAME_TAGS(16),
	/**
	 * all track are tagged but at less two of them have different tag
	 */
	ALL_DIFF_TAGS(8),
	/**
	 * not all of tracks are tagged but all that are tagged are with same tags
	 */
	SOME_SAME_TAGS(4),
	/**
	 * some track are tagged but at less two of them have different tag
	 */
	SOME_DIFF_TAGS(2),
	/**
	 * no track is tagged
	 */
	NO_TAGS(0);

	private int value;

	private TagEnum(final int pValue) {
		value = pValue;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
	    return value;
	}

}

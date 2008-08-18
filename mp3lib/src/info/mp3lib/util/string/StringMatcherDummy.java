package info.mp3lib.util.string;

/**
 * test implementation of StringMatcher interface
 * @author Gab
 */
@Deprecated
public class StringMatcherDummy implements StringMatcher {

	@Override
	public boolean match(String str1, String str2) {
		boolean result = false;
		if (str1.length() > str2.length()) {
			result = str1.toUpperCase().contains(str2.toUpperCase());
		} else {
			result = str2.toUpperCase().contains(str1.toUpperCase());
		}
		return result;
	}

}

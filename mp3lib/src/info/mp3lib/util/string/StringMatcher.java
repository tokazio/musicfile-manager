package info.mp3lib.util.string;
/**
 * Provides methods to compare to compare two Strings.
 * Singleton, can be configured
 * @author Gab
 *
 */
public class StringMatcher {

	/** the unique instance */
	private static StringMatcher instance;

	private StringMatcher() {
		// TODO
	}

	public static StringMatcher getInstance() {
		if (instance == null) {
			instance =  new StringMatcher();
		}
		return instance;
	}
	
	/**
	 * Configure this STringMatcher permissivity
	 * @param iqv
	 */
	public void configure(int iqv) {
		
	}
	
	public boolean match(String str1, String str2) {
		boolean match = false;
		// TODO method implementation
		return match;
	}
	
	public boolean include(String contain, String included) {
		boolean match = false;
		// TODO method implementation
		return match;
	}
}

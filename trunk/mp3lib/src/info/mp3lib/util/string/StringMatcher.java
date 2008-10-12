package info.mp3lib.util.string;
/**
 * Provides methods to compare two Strings.</br>
 * Use MatcherFactory.getStringMatcher(MatcherContext) to retrieve an Implementation
 * @see MatcherFactory
 * @see MatcherContext
 * @author Gab
 *
 */
public interface StringMatcher {

	/**
	 * Checks if str1 match str2 according to the concrete matcher implementation
	 * @param str1
	 * @param str2
	 * @return true if the two string match
	 */
	public boolean match(String str1, String str2);
	
	/**
	 * Compile the given string according to the concrete matcher implementation
	 * @param str
	 * @return the compiled string
	 */
	public String compile(String str);
	
	/**
	 * Retrieves a compiled pattern from the given string
	 * @param str
	 * @return the given string in a compiled pattern
	 */
	public StringPattern getPattern(String str);

}

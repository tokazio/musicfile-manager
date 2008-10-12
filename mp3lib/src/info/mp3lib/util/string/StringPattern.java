package info.mp3lib.util.string;

/**
 * Denotes a compiled string and its matcher context.<br/>
 * <br/><code>
 * StringMatcher matcher = [...];<br/>
 * StringPattern pattern = matcher.compile("toto");<br/>
 * pattern.match("tata");
 * <br/></code><br/>gives the same result than:<br/>
 * <br/><code>
 * StringMatcher matcher = [...];<br/>
 * matcher.match("toto","tata");<br/>
 * <br/></code>excepted than in the first case the String "toto" is already compiled and can be used
 * to perform many comparaison
 * @author Gab
 *
 */
public interface StringPattern {
	
	/**
	 * Checks if this pattern match the given string according to the concrete pattern implementation
	 * @param str
	 * @return true if this pattern matches the given string, false otherwise
	 */
	public boolean match(final String str);
}

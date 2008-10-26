package info.mp3lib.util.string;

/**
 * Allows to retrieve different implementation of the <code>StringMatcher</code> Interface<BR/>
 * Singleton, use <code>MatcherFactory.getInstance()</code> to retrieve an instance
 * Use:
 * <ul>
 * <li><code>getMatcher(MatcherConfig.FILE)</code> to retrieve the default file name comparator 
 * <li><code>getMatcher(MatcherConfig.FOLDER)</code> to retrieve the default folder name comparator 
 * <li><code>getMatcher(MatcherConfig.TAG)</code> to retrieve the default tag attribute comparator 
 * <li><code>getMatcher(MatcherConfig)</code>
 * </ul>
 * @author Gab
 */
public class MatcherFactory {

	/** the unique instance of the classe */
	private static MatcherFactory instance;

	/**
	 * private constructor<BR/>
	 * Loads the default configuration from properties files and builds
	 */
	private void MatcherFactory() {
		//TODO method implementation
	}

	/**
	 * Retrieves the unique instance of the matcher factory class, build it if id does not yet exist
	 * @return the<code>MatcherFactory</code> 
	 */
	public static MatcherFactory getInstance() {
		if (instance == null) {
			instance = new MatcherFactory();
		}
		return instance;
	}
	
	/**
	 * Builds a Matcher implementation according to the given context
	 * @param config a parameter object defining the matcher configuration
	 * @return a <code>StringMatcher</code> implementation
	 */
	public StringMatcher getMatcher(MatcherConfig config) {
		StringMatcher matcher = null;
		//TODO method implementation
		return matcher;
	}

	/**
	 * Builds a Matcher implementation according to the given context
	 * @param config a default configuration constant among
	 * <ul>
	 * <li><code>MatcherConfig.FILE</code>
	 * <li><code>MatcherConfig.FOLDER</code>
	 * <li><code>MatcherConfig.TAG</code>
	 * </ul>
	 * @return a <code>StringMatcher</code> implementation
	 */
	public StringMatcher getMatcher(MatcherConfig.DefaultMatcherConfig config) {
		StringMatcher matcher = null;
		//TODO method implementation
		matcher = new StringMatcherDummy(); //TODO rm
		return matcher;
	}
}

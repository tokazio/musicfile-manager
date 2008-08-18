package info.mp3lib.util.string;

import java.util.List;

/**
 * Parameter object defining what kind of <code>Matcher</code> Implementation will be returned by the 
 * <code>MatcherFactory</code><br/>
 * Use with <code>MatcherFactory.getMatcher(MatcherContext)</code><br/>
 * @author gab
 *
 */
public class MatcherContext {

	/* ------------------------ ATTRIBUTES ------------------------ */
	/** if true the java method <code>equals()</code> will be used to check equality (increase performance) */
	private boolean useSimpleEquality;
	
	/** if true the two compared string will match if one is include in the other */
	private boolean inclusionMatch;
	
	/** 
	 * if <code>inclusionMatch</code> is true, define the max difference of length allowed between 
	 * the two strong for a match.
	 */
	private int maxLengthDifferenceAllowed;
	
	/** if true, all regExp in the ignore list will be removed from the two strings to compare */
	private boolean useIgnoreList;
	
	/** 
	 * if true, if one of the all RegExp in the exclude list is found in one of the two strings to compare, 
	 * match will fail 
	 */
	private boolean useExcludeList;
	
	/** 
	 * if true, only regExp in the given ignore list (and not the default one) will be removed from the
	 * two strings to compare 
	 */
	private boolean useDefaultIgnoreList;
	
	/** 
	 * if true, if one of the all word in the given exclude (and not the default one) list is found in one 
	 * of the two strings to compare, match will fail 
	 */
	private boolean useDefaultExcludeList;
	
	/** 
	 * the list of all word to exclude from the string compared 
	 * (this list will be added to the default one or used alone if <code>useDefaultExcludeList</code> is false
	 */
	private List<String> excludeList;
	
	/** 
	 * the list of all word that will made the match fail
	 * (this list will be added to the default one or used alone if <code>useDefaultIgnoreList</code> is false
	 */
	private List<String> ignoreList;
	
	public enum MatcherConfig {
		FILE,
		FOLDER,
		TAG;
	};
	

	/* ----------------------- CONSTRUCTORS ----------------------- */
	/* -------------------------- SETTERS ------------------------- */
	/** 
	 * if <code>inclusionMatch</code> is true, define the max difference of length allowed between 
	 * the two strong for a match.
	 */
	public void setMaxLengthDifferenceAllowed(int maxLengthDifferenceAllowed) {
		this.maxLengthDifferenceAllowed = maxLengthDifferenceAllowed;
	}

	/**
	 * if true the java method <code>equals()</code> will be used to check equality (increase performance)
	 * else a complex StringMatcher will be used (detect similar strings)
	 */
	public void setUseSimpleEquality(boolean useSimpleEquality) {
		this.useSimpleEquality = useSimpleEquality;
	}

	/** if true the two compared string will match if one is include in the other */
	public void setInclusionMatch(boolean inclusionMatch) {
		this.inclusionMatch = inclusionMatch;
	}

	/** if true, all regExp in the ignore list will be removed from the two strings to compare */
	public void setUseIgnoreList(boolean useIgnoreList) {
		this.useIgnoreList = useIgnoreList;
	}

	/** 
	 * if true, if one of the all RegExp in the exclude list is found in one of the two strings to compare, 
	 * match will fail 
	 */
	public void setUseExcludeList(boolean useExcludeList) {
		this.useExcludeList = useExcludeList;
	}

	/** 
	 * if true, only regExp in the given ignore list (and not the default one) will be removed from the
	 * two strings to compare 
	 */
	public void setUseDefaultIgnoreList(boolean useDefaultIgnoreList) {
		this.useDefaultIgnoreList = useDefaultIgnoreList;
	}

	/** 
	 * if true, if one of the all word in the given exclude (and not the default one) list is found in one 
	 * of the two strings to compare, match will fail 
	 */
	public void setUseDefaultExcludeList(boolean useDefaultExcludeList) {
		this.useDefaultExcludeList = useDefaultExcludeList;
	}

	/** 
	 * the list of all word to exclude from the string compared 
	 * (this list will be added to the default one or used alone if <code>useDefaultExcludeList</code> is false
	 */
	public void setExcludeList(List<String> excludeList) {
		this.excludeList = excludeList;
	}

	/** 
	 * the list of all word that will made the match fail
	 * (this list will be added to the default one or used alone if <code>useDefaultIgnoreList</code> is false
	 */
	public void setIgnoreList(List<String> ignoreList) {
		this.ignoreList = ignoreList;
	}
	/* ------------------------ CONSTANTS ------------------------- */
	/** denote a StringMatcher implementation configured to compare file names */
	public final static MatcherConfig FILE = MatcherConfig.FILE;

	/** denote a StringMatcher implementation configured to compare folders names */
	public final static MatcherConfig FOLDER = MatcherConfig.FOLDER;
	
	/** denote a StringMatcher implementation configured to compare tag values */
	public final static MatcherConfig TAG = MatcherConfig.TAG;
}

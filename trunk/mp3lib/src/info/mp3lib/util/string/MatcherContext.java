package info.mp3lib.util.string;

import java.util.List;

/**
 * Parameter object defining what kind of <code>Matcher</code> Implementation will be returned by the 
 * <code>MatcherFactory</code><br/>
 * Use with <code>MatcherFactory.getMatcher(MatcherContext)</code><br/>
 * Uses the configuration hold by the <code>Config</code> object<br/>
 * @author gab
 */
public class MatcherContext {

	/* ------------------------ ATTRIBUTES ------------------------ */
	/** if true the java method <code>equals()</code> will be used to check equality (increase performance) */
	private boolean useSimpleEquality;
	
	/** if true the two compared string will match if one is include in the other */
	private boolean inclusionMatch;
	
	/** if false the given string will be compared ignoring case */
	private boolean caseSensitive;
	
	/** if false all accent will be removed from the given string before comparaison */
	private boolean accentSensitive;
	
	/** if true all the character defined as separator will be replaced by the separator defined */
	private boolean normalizeSeparator;
	
	/** 
	 * if <code>inclusionMatch</code> is true, define the max difference of length allowed between 
	 * the two string for a match.
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
	
	/** predefined configuration */
	public enum MatcherConfig {
		FILE,
		FOLDER,
		TAG;
	};

	/* ----------------------- CONSTRUCTORS ----------------------- */
	/** Default Constructor. */
	public MatcherContext() {
		setAccentSensitive(true);
		setCaseSensitive(true);
		setInclusionMatch(false);
		setMaxLengthDifferenceAllowed(0);
		setNormalizeSeparator(true);
		setUseDefaultExcludeList(true);
		setUseDefaultIgnoreList(true);
		setUseExcludeList(true);
		setUseIgnoreList(true);
		setUseSimpleEquality(false);
	}
	
	/* -------------------------- SETTERS ------------------------- */
	/** 
	 * if <code>inclusionMatch</code> is true, define the max difference of length allowed between 
	 * the two strong for a match.<br/>
	 * 0 means that there is no maximum, default is 0.
	 */
	public void setMaxLengthDifferenceAllowed(int maxLengthDifferenceAllowed) {
		this.maxLengthDifferenceAllowed = maxLengthDifferenceAllowed;
	}

	/**
	 * if true the java method <code>equals()</code> will be used to check equality (increase performance)
	 * else a complex StringMatcher will be used (detect similar strings)<br/>
	 * Default is false.
	 */
	public void setUseSimpleEquality(boolean useSimpleEquality) {
		this.useSimpleEquality = useSimpleEquality;
	}

	/** if true the two compared string will match if one is include in the other<br/>
	 * Default is false. */
	public void setInclusionMatch(boolean inclusionMatch) {
		this.inclusionMatch = inclusionMatch;
	}

	/** if true, all regExp in the ignore list will be removed from the two strings to compare<br/>
	 * Default is true. */
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
	
	/** if false the given string will be compared ignoring case<br/>
	 * Default is true. */
	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	/** if false the given string will be compared ignoring case */
	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	/** if false all accent will be removed from the given string before comparaison<br/>
	 * Default is true.*/
	public boolean isAccentSensitive() {
		return accentSensitive;
	}

	/** if false all accent will be removed from the given string before comparaison */
	public void setAccentSensitive(boolean accentSensitive) {
		this.accentSensitive = accentSensitive;
	}

	/** if true all the character defined as separator will be replaced by the separator defined<br/>
	 * Default is true. */
	public boolean isNormalizeSeparator() {
		return normalizeSeparator;
	}

	/** if true all the character defined as separator will be replaced by the separator defined */
	public void setNormalizeSeparator(boolean normalizeSeparator) {
		this.normalizeSeparator = normalizeSeparator;
	}

	/** if true the java method <code>equals()</code> will be used to check equality (increase performance) */
	public boolean isUseSimpleEquality() {
		return useSimpleEquality;
	}

	/** if true the two compared string will match if one is include in the other */
	public boolean isInclusionMatch() {
		return inclusionMatch;
	}

	/** 
	 * if <code>inclusionMatch</code> is true, define the max difference of length allowed between 
	 * the two string for a match.
	 */
	public int getMaxLengthDifferenceAllowed() {
		return maxLengthDifferenceAllowed;
	}

	/**
	 * @return true if ignore list is used, false otherwise
	 */
	public boolean isUseIgnoreList() {
		return useIgnoreList;
	}

	/**
	 * @return true if exclude list is used, false otherwise
	 */
	public boolean isUseExcludeList() {
		return useExcludeList;
	}

	/**
	 * if the ignore list is used, checks if the default ignore list defined in the <code>Config</code> 
	 * object is used or only the one defined in this MatcherContext
	 * @return true if ignore list are used and the default one is considered 
	 */
	public boolean isUseDefaultIgnoreList() {
		return useDefaultIgnoreList;
	}

	/**
	 * if the exclude list is used, checks if the default exclude list defined in the <code>Config</code> 
	 * object is used or only the one defined in this MatcherContext
	 * @return true if exclude list are used and the default one is considered 
	 */
	public boolean isUseDefaultExcludeList() {
		return useDefaultExcludeList;
	}

	/**
	 * retrieves the list of all word to exclude from the string compared 
	 * @return the excludeList, null if there is not
	 */
	public List<String> getExcludeList() {
		return excludeList;
	}

	/**
	 * the list of all word that will made the match fail
	 * @return the ignoreList, null if there is not
	 */
	public List<String> getIgnoreList() {
		return ignoreList;
	}
	
	
	/* ------------------------ CONSTANTS ------------------------- */
	/** denote a StringMatcher implementation configured to compare file names */
	public final static MatcherConfig FILE = MatcherConfig.FILE;

	/** denote a StringMatcher implementation configured to compare folders names */
	public final static MatcherConfig FOLDER = MatcherConfig.FOLDER;
	
	/** denote a StringMatcher implementation configured to compare tag values */
	public final static MatcherConfig TAG = MatcherConfig.TAG;

}

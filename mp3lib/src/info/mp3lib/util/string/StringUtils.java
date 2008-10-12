package info.mp3lib.util.string;

import java.io.File;

/**
 * Implements useful method to manipulate Strings
 * @author Gab
 */
public final class StringUtils {
	/**
	 * Remove if it exists the extension (ex .mp3) from the give file name.
	 * @param fileName the name of a file
	 * @return the file name without its extension
	 */
	public final static String removeExtension(final String fileName) {
		String name = fileName;
		if (!fileName.trim().isEmpty()) {
			final int dotPosition = name.lastIndexOf('.');
			if (dotPosition != -1) {
				name = name.substring(0,dotPosition);
			}
		}
		return name;
	}
	
	/**
	 * Retrieves the name of the parent folder of the file denoted by given filename
	 * @param fileName the name of a file
	 * @return the parent name
	 */
	public final static String getParentFolderName(final String fileName) {
		return new File(fileName).getParentFile().getName();
	}
	
	/**
	 * retrieves the longest common substring of the two given string. (substrings are necessarily contiguous).
	 * @param str1
	 * @param str2
	 * @return the longest common substring of the two given string, null if one of the two given 
	 * string is null
	 */
	public static String getLCSubstring(final String str1, final String str2) {
		//TODO method implementation
		return "";
	}
	
	/**
	 * Builds a normalized string from the given one<br/>
	 * @param str the string to normalize
	 * @param normalizeSeparator, if true replaces all the character defined as separator by the default 
	 * one<br/> Uses the configuration hold by the <code>Config</code> object
	 * @param noAccent, if true remove all accent
	 * @param noCase, if true remove the case (@see <code>java.text.Collator</code>)
	 * @return the normalized string
	 */
	public static String normalize(final String str, final boolean normalizeSeparator, 
			final boolean noAccent, final boolean noCase) {
		//TODO method implementation
		return "";
	}
}

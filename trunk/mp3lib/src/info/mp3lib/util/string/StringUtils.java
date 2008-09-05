package info.mp3lib.util.string;

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
	public final static String removeExtension(String fileName) {
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
	 * Uses the configuration hold by the <code>Config</code> object
	 * Replace all the character defined as separator by the separator defined
	 * Replace all 
	 * @param str
	 * @return
	 */
	public static String normalize(final String str) {
		String s = null;
		return "";
	}
}

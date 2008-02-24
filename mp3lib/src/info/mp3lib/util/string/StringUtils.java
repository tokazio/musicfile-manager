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
}

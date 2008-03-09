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
	 * Checks if name doesn't correspond to some patterns like 'track 01' 
	 * @param name the title of this track
	 * @return true if name is a valid title else return false
	 */
	public final static boolean isValidTitle(String name) {
		boolean valid = false;
		//TODO method implementation
		return valid;
	}
}

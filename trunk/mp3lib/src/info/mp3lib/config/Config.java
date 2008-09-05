package info.mp3lib.config;

//TODO class implementation and commment
public class Config {
	
	/** the unique instance of the singleton */
	private static Config instance;
	
	/** static access to the singleton */
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}
	
	/**
	 * Retrieves the path in which is located the library XML file.
	 * @return the library XML file directory path
	 */
	public String getLibraryFilePath() {
		// TODO method implementation
		return null;
	}
}

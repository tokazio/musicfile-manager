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
	
	public String getZicFilePath() {
		// TODO method implementation
		return null;
	}
}

package info.mp3lib.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.log4j.Logger;

/**
 * Holds the application configuration attributes loaded from the configuration properties file.<br/>
 * Singleton.
 * @author Gab
 */
public class Config {

	/** configuration properties file DAO */
	private Properties config;

	/** the unique instance of the singleton */
	private static Config instance;

	/** the absolute path of the configuration file */
	private String configFilePath;

	/** configuration attributes */
	private String[] separators;
	private String separator;
	private String libraryFile;

	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Config.class.getName());

	/** static access to the singleton */
	public static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}

	/**
	 * Constructor<br/>
	 * Retrieves the configuration properties file.
	 * @throws ConfigurationException if the configuration properties file can't be read or is not found
	 */
	public Config() {
		File configFile = new File(CONFIG_FILE);
		try {
			configFilePath = configFile.getPath();
			config.load(new FileInputStream(configFile));
			loadLibraryFilePath();
			loadSeparators();
		} catch (FileNotFoundException e) {
			throw new ConfigurationException(new StringBuffer("The configuration file [")
			.append(configFilePath).append("] can't be found :\n").append(e.getMessage()).toString());
		} catch (IOException e) {
			throw new ConfigurationException(new StringBuffer("The configuration file [")
			.append(configFilePath).append("] can't be read :\n").append(e.getMessage()).toString());
		}
//		System.getProperties("user.dir")
	}

	/**
	 * Checks if the given string denote a valid regular expression representing a single character
	 * @param str the string to check
	 * @return true if the given string is valid, false otherwise
	 */
	private boolean isValidRegExpChar(final String str) {
		boolean valid = true;
		if (str.length() < 3) {
			if (str.length() == 2) {
				if (str.startsWith("\\")) {
					valid = false;
				}
			}
			if (valid) {
				try {
					Pattern.compile(str);
				} catch (PatternSyntaxException e) {
					valid = false;
				}
			}
		} else {
			valid = false;
		}
		return valid;
	}

	/**
	 * Loads and checks SEPARATORS and DEFAULT.SEPARATOR configuration attributes<br/>
	 * log a warn if the property is missing or not set
	 * @throws ConfigurationException if one of the loaded separator is invalid
	 */
	private void loadSeparators() {
		String key = "SEPARATORS";
		final String value = config.getProperty(key);
		if (value == null) {
			LOGGER.warn(key + MISSING_PROP_ERROR);
			separators = new String[0];
		} else {
			separators = value.split(CONFIG_FILE_SEPARATOR);
			// check separator validity
			int i = 0;
			boolean valid = true;
			while (i < separators.length && valid) {
				valid = isValidRegExpChar(separators[i]);
			}
			if (!valid) {
				throw new ConfigurationException(new StringBuffer(key)
				.append("configuration property is invalid, only regular expression denoting character are allowed")
				.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
		}
		key = "SEPARATOR";
		separator = config.getProperty(key);
		if (separator == null ||  ! isValidRegExpChar(separator)) {
			throw new ConfigurationException(new StringBuffer(key)
			.append("configuration property is invalid, only regular expression denoting character are allowed")
			.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
		}
			
	}

	/**
	 * Loads and checks LIBRARY_FILE configuration attribute<br/>
	 * @throws ConfigurationException if the property is missing or not set
	 */
	private void loadLibraryFilePath() {
		final String key = "LIBRARY_FILE";
		libraryFile = config.getProperty(key);
		if (libraryFile == null) {
			throw new ConfigurationException(key + MISSING_PROP_ERROR);
		}
	}

	/**
	 * Retrieves the path in which is located the library XML file.
	 * @return the library XML file directory path
	 */
	public String getLibraryFilePath() {
		return libraryFile;
	}

	/**
	 * Retrieves all characters defined as separators, an empty array if the associated property is not
	 * set or missing in the configuration file.
	 * @return all characters defined as separators
	 */
	public String[] getSeparators() {
		return separators;
	}

	/* ------------------------ CONSTANTS ------------------------ */
	/** missing property error message */
	private String MISSING_PROP_ERROR = " property is not set or missing, configuration file may be corrupted";

	/** The relative path of the configuration file */
	private final String CONFIG_FILE = "../config/config.properties";

	/** The separator used by the configuration file */
	private final String CONFIG_FILE_SEPARATOR = ";";


}

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
				valid = isValidRegex(str);
			}
		} else {
			valid = false;
		}
		return valid;
	}
	
	/**
	 * Checks if the given string denote a valid regular expression
	 * @param str the string to check
	 * @return true if the given string is valid, false otherwise
	 */	
	private boolean isValidRegex(final String str) {
		boolean valid = true;
		try {
			Pattern.compile(str);
		} catch (PatternSyntaxException e) {
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
	 * Retrieves the given quality index modifier value and checks its validity
	 * @param modifier the name of the modifier to retrieve (@see configuration properties file)
	 * @return the given quality index modifier value
	 * @throws ConfigurationException if the property is missing or is not a valid integer
	 */
	public int getQIModifier(final String modifier) {
		int result = 0;
		final String value = config.getProperty(modifier);
		if (value == null) {
			throw new ConfigurationException(modifier + MISSING_PROP_ERROR);
		} else {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException  e) {
				throw new ConfigurationException(new StringBuffer(modifier)
				.append("configuration property is invalid, only integer values are allowed")
				.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
			
		}
		return result;
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
	
	/**
	 * Physical context quality index modifiers keys to access configuration data
	 * @see <code>info.mp3lib.validator.PhysicalContext</code>
	 */
	private final static String PAR = "PHYSICAL.MODIFIER.ARTIST.";
	public final static String PAR_SOME_DIFFERENT_ARTIST_IN_TREE = PAR + "SOME_DIFFERENT_ARTIST_IN_TREE";
	public final static String PAR_CONTAINS_INVALIDER_WORD = PAR + "CONTAINS_INVALIDER_WORD";
	public final static String PAR_CONTAINS_VALIDER_WORD = PAR + "CONTAINS_VALIDER_WORD";
	public final static String PAR_OTHER_ALBUM_ARTIST_MATCH = PAR + "OTHER_ALBUM_ARTIST_MATCH";
	public final static String PAR_ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST = PAR + "ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST";

	private final static String PAL = "PHYSICAL.MODIFIER.ALBUM.";
	public final static String PAL_NOT_LEAF = PAL + "NOT_LEAF";
	public final static String PAL_CONTAINS_INVALIDER_WORD = PAL + "CONTAINS_INVALIDER_WORD";
	public final static String PAL_SOME_DIFFERENT_ARTIST_IN_TREE = PAL + "SOME_DIFFERNT_ARTIST_IN_TREE";
	public final static String PAL_OTHER_ALBUM_ARTIST_MATCH = PAL + "OTHER_ALBUM_ARTIST_MATCH";
	public final static String PAL_NAME_FIRST_PART_MATCH_PARENT = PAL + "NAME_FIRST_PART_MATCH_PARENT";
	public final static String PAL_NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST = PAL + "NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST";
	public final static String PAL_ALL_ARTIST_TREE_WELL_FORMED = PAL + "ALL_ARTIST_TREE_WELL_FORMED";
	public final static String PAL_ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST = PAL + "ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST";

	private final static String PTR = "PHYSICAL.MODIFIER.TRACK.";
	public final static String PTR_NOT_LEAF = PTR + "NOT_LEAF";
	public final static String PTR_CONTAINS_INVALIDER_WORD = PTR + "CONTAINS_INVALIDER_WORD";
	public final static String PTR_REPEATING_SEQUENCE_NOT_IN_FOLDERNAME = PTR + "REPEATING_SEQUENCE_NOT_IN_FOLDERNAME";
	public final static String PTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE =PTR + "NO_ALPHADECIMAL_VARIABLE_SEQUENCE";
	public final static String PTR_BIG_VARIABLE_SEQUENCE = PTR + "BIG_VARIABLE_SEQUENCE";
	public final static String PTR_REPEATING_SEQUENCE_IN_FOLDERNAME = PTR + "REPEATING_SEQUENCE_IN_FOLDERNAME";

	/**
	 * Tag context quality index modifiers keys to access configuration data
	 * @see <code>info.mp3lib.validator.TagContext</code>
	 */
	private final static String TAR = "TAG.MODIFIER.ARTIST.";
	public final static String TAR_NO_ARTIST_FIELD_SET = TAR + "NO_ARTIST_FIELD_SET";
	public final static String TAR_SOME_ARTIST_FIELD_SET = TAR + "SOME_ARTIST_FIELD_SET";
	public final static String TAR_ALL_ARTIST_FIELD_SET = TAR + "ALL_ARTIST_FIELD_SET";
	public final static String TAR_SOME_DIFFERENT_ARTIST = TAR + "SOME_DIFFERENT_ARTIST";

	private final static String TAL = "TAG.MODIFIER.ALBUM.";
	public final static String TAL_NO_ALBUM_FIELD_SET = TAL + "NO_ALBUM_FIELD_SET";
	public final static String TAL_ALL_TITLE_FIELD_SET = TAL + "ALL_TITLE_FIELD_SET";
	public final static String TAL_SOME_DIFFERENT_ALBUM = TAL + "SOME_DIFFERENT_ALBUM";
	public final static String TAL_SOME_DIFFERENT_ARTIST = TAL + "SOME_DIFFERENT_ARTIST";

	private final static String TTR = "TAG.MODIFIER.TRACK.";
	public final static String TTR_NO_TITLE_FIELD_SET = TTR + "NO_TITLE_FIELD_SET";
	public final static String TTR_ALL_TITLE_FIELD_SET = TTR + "ALL_TITLE_FIELD_SET";
	public final static String TTR_CONTAINS_INVALIDER_WORD = TTR + "CONTAINS_INVALIDER_WORD";
	public final static String TTR_REPEATING_SEQUENCE = TTR + "REPEATING_SEQUENCE";
	public final static String TTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE = TTR + "NO_ALPHADECIMAL_VARIABLE_SEQUENCE";
	public final static String TTR_BIG_VARIABLE_SEQUENCE = TTR + "BIG_VARIABLE_SEQUENCE";
	
}

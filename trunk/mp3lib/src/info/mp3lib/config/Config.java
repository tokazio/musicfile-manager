package info.mp3lib.config;

import info.mp3lib.util.cddb.IDBQuery;

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

	/** 
	 * configuration attributes for string manipulation
	 * @see info.mp3lib.string.MatcherContext
	 */
	private String[] separators;
	private String separator;
	private String libraryFile;
	private String[] ignoreList;
	private String[] excludeList;
	
	
	/** Implementation class for <code>IDBQuery</code>ie. tag database access */
	@SuppressWarnings("unchecked")
	private Class tagDatabaseAccessImpl;

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
			loadTagDatabaseAccessImpl();
			ignoreList = getList("DEFAULT.IGNORED");
			excludeList = getList("DEFAULT.EXCLUDED");
		} catch (FileNotFoundException e) {
			throw new ConfigurationException(new StringBuffer("The configuration file [")
			.append(configFilePath).append("] can't be found :\n").append(e.getMessage()).toString());
		} catch (IOException e) {
			throw new ConfigurationException(new StringBuffer("Unable to load configuration file [")
			.append(configFilePath).append("]:\n").append(e.getMessage()).toString());
		}
//		System.getProperties("user.dir")
	}

	/**
	 * Checks if the given string denote a valid regular expression representing a single character
	 * @param str the string to check
	 * @return true if the given string is valid, false otherwise
	 */
	private boolean isValidRegexChar(final String str) {
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
	 * @throws ConfigurationException if loaded separator is invalid
	 */
	private void loadSeparators() {
		String key = "SEPARATORS";
		final String value = config.getProperty(key);
		if (value == null) {
			LOGGER.warn(key + MISSING_PROP_ERROR);
		} else {
			separators = value.split(CONFIG_FILE_SEPARATOR);
			// check separator validity
			int i = 0;
			boolean valid = true;
			while (i < separators.length && valid) {
				valid = isValidRegexChar(separators[i]);
			}
			if (!valid) {
				throw new ConfigurationException(new StringBuffer("configuration property [").append(key)
				.append("] is invalid, only regular expression denoting character are allowed")
				.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
		}
		key = "SEPARATOR";
		separator = config.getProperty(key);
		if (separator == null) {
			LOGGER.warn(key + MISSING_PROP_ERROR);
		}
		else if (! isValidRegexChar(separator)) {
			throw new ConfigurationException(new StringBuffer("configuration property [").append(key)
			.append("] is invalid, only regular expression denoting character are allowed")
			.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
		}
			
	}

	/**
	 * Loads and checks LIBRARY_FILE configuration attribute<br/>
	 * @throws ConfigurationException if the property is missing or not set
	 */
	private void loadTagDatabaseAccessImpl() {
		final String key = "TAG_DATABASE_ACCESS_IMPL";
		final String className = config.getProperty(key);
		if (className == null) {
			throw new ConfigurationException(key + MISSING_PROP_ERROR);
		} else {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				throw new ConfigurationException(new StringBuffer("configuration property [").append(key)
						.append("] is invalid, the class denoted by the given property does not exist")
						.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
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
			throw new ConfigurationException(new StringBuffer("configuration property [").append(key)
					.append("] is invalid, only integer values are allowed")
					.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
		}
	}
	
	/**
	 * Retrieves and checks list configuration attribute denoted by the given key<br/>
	 * @return an array of regexp if the given attribute is not empty, null otherwise
	 * @throws ConfigurationException if the property is missing / not set or contains
	 * invalid expression
	 */
	public String[] getList(final String key) {
		final String value = config.getProperty(key);
		String[] result = null;
		if (value == null) {
			LOGGER.warn(key + MISSING_PROP_ERROR);
		} else if (value.trim().length() != 0) {
			result = value.split(CONFIG_FILE_SEPARATOR);
			// check excluded regular expressions validity
			int i = 0;
			boolean valid = true;
			while (i < result.length && valid) {
				valid = isValidRegex(result[i]);
			}
			if (!valid) {
				throw new ConfigurationException(new StringBuffer("configuration property [").append(key)
				.append("] is invalid, only integer values are allowed")
				.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
		}
		return result;
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
				throw new ConfigurationException(new StringBuffer("configuration property [").append(modifier)
				.append("] is invalid, only integer values are allowed")
				.append("\ncheck the configuration file [").append(configFilePath).append("]").toString());
			}
			
		}
		return result;
	}
	
	/**
	 * @return implementation class used for tag database access.
	 */
	@SuppressWarnings("unchecked")
	public Class<IDBQuery> getTagDatabaseAccessImpl() {
		return tagDatabaseAccessImpl;
	}

	/**
	 * Retrieves the path in which is located the library XML file.
	 * @return the library XML file directory path
	 */
	public String getLibraryFilePath() {
		return libraryFile;
	}

	/**
	 * Retrieves all characters defined as separators, null if the associated property is not
	 * set or missing in the configuration file.
	 * @return all characters defined as separators
	 */
	public String[] getSeparators() {
		return separators;
	}
	
	public String getSeparator() {
		return separator;
	}

	public String getLibraryFile() {
		return libraryFile;
	}

	public String[] getIgnoreList() {
		return ignoreList;
	}

	public String[] getExcludeList() {
		return excludeList;
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
	
	public final static String P_TRACK_TITLE_INVALIDERS = "PHYSICAL.TRACK_TITLE_INVALIDERS";
	public final static String P_ARTIST_NAME_INVALIDERS = "PHYSICAL.ARTIST_NAME_INVALIDERS";
	public final static String P_ARTIST_NAME_VALIDERS = "PHYSICAL.ARTIST_NAME_VALIDERS";
	public final static String P_ALBUM_NAME_INVALIDERS = "PHYSICAL.ALBUM_NAME_INVALIDERS";
	
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
	
	public final static String T_TRACK_TITLE_INVALIDERS = "TAG.TRACK_TITLE_INVALIDERS";

	/**
	 * DBSelector quality index modifiers keys.
	 * Supposed to compare a DBResult with a Generic Context.
	 * used in -> ITagQueryResult (compareTo(Context))
	 */
	private final static String DAL = "DB.MODIFIER.ALBUM.";
	public final static String DAL_SAME_ARTIST = DAL + "SAME_ARTIST";
	public final static String DAL_SAME_ALBUM = DAL + "SAME_ALBUM";
	public final static String DAL_SAME_DISCID = DAL + "SAME_DISCID";
	public final static String DAL_SAME_GENRE = TTR + "SAME_GENRE";
	public final static String DAL_SAME_YEAR = DAL + "SAME_YEAR";
	public final static String DAL_GOOD_QUALITY = DAL + "GOOD_QUALITY";
	
}

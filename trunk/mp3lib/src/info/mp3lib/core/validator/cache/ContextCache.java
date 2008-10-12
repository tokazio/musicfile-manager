package info.mp3lib.core.validator.cache;

import java.util.Map;

import org.apache.log4j.Logger;

import info.mp3lib.config.Config;
import info.mp3lib.core.validator.TagContext.ArtistTagEnum;

/**
 * Manages caching of data and quality index deduced to an artist level analysis to avoid
 * redundant computation for different artist in the same directory
 * @author Gab
 */
public class ContextCache {
	
	private static class artistContext {
		/** all the artist names present in the current parent directory */
		private String[] artistNames[];
		
		/** artist name quality index modifiers */
		private ArtistTagEnum[] artistQI;
		
		
	}
	//TODO reflechir
	private static Map<String, artistContext> artistContexts;
	
	/** the unique instance of the singleton */
	private static ContextCache instance;
	
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(ContextCache.class.getName());

	/** static access to the singleton */
	public static ContextCache getInstance() {
		if (instance == null) {
			instance = new ContextCache();
		}
		return instance;
	}
	
}

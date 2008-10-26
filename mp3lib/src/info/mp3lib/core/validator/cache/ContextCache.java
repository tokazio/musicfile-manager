package info.mp3lib.core.validator.cache;

import java.util.Map;

import org.apache.log4j.Logger;

import info.mp3lib.config.Config;
import info.mp3lib.core.validator.TagContext.ArtistTagEnum;

/**
 * Manages caching for data and quality index deduced to an artist level analysis to avoid
 * redundant computation for different albums located in the same directory
 * @author Gab
 */
public class ContextCache {
	
	/** Holds the cached data **/
	private static Map<String, CachedContextBean> artistContexts;
	
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
	
	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.  
	 * @param key the folder name for which look for a cached value
	 * @return The context if there is one in the cache denoted by this key, null otherwise
	 */
	public CachedContextBean getCache(final String key) {
		return artistContexts.get(key);
	}
	
	public void setCache(final String key, final CachedContextBean context) {
		artistContexts.put(key, context);
	}
	
}

package info.mp3lib.core;

import java.util.Map;

/**
 * Holds all existing artist in Map.<br/>
 * Manages (creates and provides artists to the program)
 * Unique instance in the program (Singleton)
 * @author AkS - Gab
 */
public class Library {

	/** the list of existing artist in the library */
	private Map<String, Artist> artistList;
	
	/** the unique instance of the singleton */
	private static Library instance;
	
	/** static access to the singleton */
	public static Library getInstance() {
		if (instance == null) {
			instance = new Library();
		}
		return instance;
	}
	
	/**
	 * If it exists retrieves the artist denoted by the given name else creates it and 
	 * add it to the library
	 * @param artistName the name of the artist to retrieve
	 * @return the artist denoted by the given name
	 */
	public Artist getArtist(String artistName) {
		Artist artist = null;
		artist = artistList.get(artistName);
		if (artistList == null) {
			artist = new Artist(artistName);
			artistList.put(artistName, artist);
		}
		return artist;
	}
}

package info.mp3lib.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * Holds all existing artist in Map.<br/> Manages (creates and provides artists to the program) Unique instance in the program (Singleton)
 * @author   AkS - Gab
 * @uml.dependency   supplier="info.mp3lib.core.Artist" stereotypes="Standard::Create"
 */
public class Library {

	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Library.class.getName());

	/** the XML root element containing all artists elements */
	private Element rootElement;

	/** the list of existing artist in the library
	 * <ul>
	 * <li>key : artist name
	 * <li>value : artist object
	 * </ul>
	 */
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

	/** Constructor */
	private Library() {
		artistList = new HashMap<String, Artist>();
		rootElement =  new Element(ROOT_ELT);
	}

	/**
	 * Add the given artist to this librairy
	 * @param artist the artist to add
	 */
	public void add(final Artist artist) {
		artistList.put(artist.getName(), artist);
		rootElement.addContent(artist.getElement());
	}

	/**
	 * remove the given artist from this librairy
	 * @param artist the artist to remove
	 */
	public boolean remove(final Artist artist) {
		return (artistList.remove(artist.getName()) != null);
	}

	/**
	 * If it exists retrieves the artist denoted by the given name else creates it and 
	 * add it to the library
	 * @param artistName the name of the artist to retrieve
	 * @return the artist denoted by the given name
	 */
	public Artist getArtist(final String artistName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(new StringBuffer("Asked for artist [").append(artistName).append("]...").toString());
		}
		Artist artist = null;
		artist = artistList.get(artistName);
		if (artist == null) {
			LOGGER.debug("Does not exist, création...");
			artist = new Artist(artistName);
			add(artist);
		}
		return artist;
	}

	/**
	 * If it exists retrieves the album denoted by the given name in the artist denoted by the given name, 
	 * else creates the album and add it to the artist which is created too if it does not exist and added 
	 * to the library
	 * @param artistName the name of the artist in which to retrieve the album
	 * @param albumName the name of the album to retrieve
	 * @return the album denoted by the given name
	 */
	public Album getAlbum(final String artistName, final String albumName) {
		final Artist artist = getArtist(artistName);
		return artist.getAlbum(albumName);
	}

	/**
	 * Retrieves the XML root element holding all the artist element
	 * @return the XML node
	 */
	public Element getRootElement() {
		return rootElement;
	}

	/**
	 * Validate each albums of this library
	 */
	public void validate() {
		Collection<Artist> collection = artistList.values();
		Artist artist;
		Album album;
		for (Iterator<Artist> iterArt = collection.iterator(); iterArt.hasNext();) {
			artist = iterArt.next();
			for (Iterator<Album> iterAlb = artist.getAlbumIterator(); iterAlb.hasNext();) {
				album = iterAlb.next();
				album.validate();
			}
		}
	}

	/**
	 * @param folderPath the path of the folder in with are located the expected albums
	 * @return all albums located in the folder denoted by the given path
	 */
	public Album[] getAlbumsLocatedIn(final String folderPath) {
		Iterator<String> artistIt = artistList.keySet().iterator();
		final List<Album> albums = new LinkedList<Album>();
		String artistName = null;
		Iterator<Album> albumIt = null;
		while(artistIt.hasNext()) {
			albumIt = artistList.get(artistIt.next()).getAlbumIterator();
			while (albumIt.hasNext()) {
				final Album album = (Album) albumIt.next();
				if (album.getParentPath().equals(folderPath)) {
					albums.add(album);
				}
			}
		}
		return albums.toArray(new Album[albums.size()]);
	}

	/* ------------------------- CONSTANTS --------------------------- */

	/** The default name of an element denoting an undefined object*/
	public final static String DEFAULT_UNKNOWN_ELT_NAME = "__unknown__";

	/** The default name of an element denoting an undefined object*/
	public final static String ROOT_ELT = "artists";
}

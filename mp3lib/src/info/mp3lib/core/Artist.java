package info.mp3lib.core;

import info.mp3lib.util.string.StringMatcher;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
* All objects corresponding to a directory containing some albums (ie. directories containing music files).
* @author Gabriel Pala
*/
public class Artist extends XMLMusicElement {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Artist.class.getName());
	
	/** Collection of ALbums */
	private List<Album> albumList;
	
	/** The tag state of this artist */
	private TagEnum tagState;
	
	/** Total number of Artist */
	private static int id = 0;
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Artist and all Album it contains from the file specified.<br/>
	 *  /!\ should not be call directly use <code>Library.getInstance().getArtist()</code> instead.
	 * @param directory a File representing a directory containing albums
	 * @throws IllegalArgumentException when the File given in parameters
	 * doesn't correspond to a valid artist
	 */
	public Artist(String name) throws IllegalArgumentException {
		super(new Element(ELT_ARTIST));
		setName(name);
		id++;
		setId(id);
	}
	
	/**
	 * Constructs a new Artist from the node specified.<br/>
	 *  /!\ should not be call directly use <code>Library.getInstance().getArtist()</code> instead.
	 * @param Element a zicfile artist element
	 * @param retrieveFile if true retrieve and set musicFile from node informations
	 * @throws IllegalArgumentException when the Element given in parameters
	 * doesn't correspond to a valid artist Element
	 */
	public Artist(Element artistElement) throws IllegalArgumentException {
		super(artistElement);
		// TODO verify that given node well correspond to an artist and implement the exception mechanism
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * Retrieves the style attribute of the XML node
	 * @return the style
	 */
	public String getStyle() {
		return getElement().getAttributeValue(XMLMusicElement.ATTR_STYLE);
	}
	
	/**
	 * Sets the given style as XML element attribute
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		getElement().setAttribute(XMLMusicElement.ATTR_STYLE,style);
	}

	/**
	 * Checks if the current directory contains at less one album containing tag information.
	 * @author
	 * @return true if artist tracks are tagged, else return false
	 */
	public boolean isTagged() {
		return tagState != TagEnum.NO_TAGS;
	}
	
	/**
	 * @return the tagState
	 */
	public TagEnum getTagState() {
		return tagState;
	}
	
	/**
	 * Adds the given album to this artist
	 */
	public void add(Album album) {
		albumList.add(album);
		if (album.isTagged()) {
			if (tagState == TagEnum.NO_TAGS) {
				if (albumList.size() == 1) {
					tagState = TagEnum.ALL_SAME_TAGS;
				} else {
					tagState = TagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == TagEnum.ALL_SAME_TAGS) {
				// match album of first and current track
				if (!StringMatcher.getInstance().match(album.getArtistName(), albumList.get(0).getArtistName())) {
					tagState = TagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == TagEnum.SOME_SAME_TAGS) {
				// does nothing
			} else if (tagState == TagEnum.SOME_DIFF_TAGS) {
				// does nothing
			}
		} else {
			if (tagState == TagEnum.ALL_SAME_TAGS) {
				tagState = TagEnum.SOME_SAME_TAGS;
			} else if (tagState == TagEnum.ALL_DIFF_TAGS) {
				tagState = TagEnum.SOME_DIFF_TAGS;
			}
		}
	}
	
	/**
	 * Removes the given album from this artist
	 * @param album the album to remove
	 * @return true if the track was successfully removed, false otherwise
	 */
	public boolean remove(Album album) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(new StringBuffer("Artist [").append(getName()).append("]: Removing Album [")
					.append(album.getName()).append("]...").toString());
		}
		boolean success = false;
		if (getElement().removeChild(album.getName()))  {
			success = albumList.remove(album);
		}
		if (!success) {
			LOGGER.debug("Failure");
		}
		return success;
	}
	
	/**
	 * Retrieves an iterator of the album collection of this artist
	 * @return an album iterator
	 */
	public Iterator<Album> getAlbumIterator() {
		return albumList.iterator();
	}
	
	/**
	 * If it exists retrieves the album denoted by the given name else creates it and 
	 * add it to this artist
	 * @param albumName the name of the album to retrieve
	 * @return the album denoted by the given name
	 */
	public Album getAlbum(final String albumName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(new StringBuffer("Asked for album [").append(albumName).append("]...").toString());
		}
		Album album = null;
		final Iterator<Album> it = getAlbumIterator();
		boolean notFound = true;
		while (it.hasNext() && notFound) {
			album = (Album) it.next();
			notFound = !album.getName().equals(albumName);
		}
		if (notFound) {
			LOGGER.debug("Does not exist, création...");
			album = new Album(albumName);
			add(album);
		}
		return album;
	}
	
	/**
	 * Checks if the album list of this artist contains no elements. 
	 * @return true if this artist is empty
	 */
	public boolean isEmpty() {
		return albumList.isEmpty();
	}
}

package info.mp3lib.core;

import info.mp3lib.util.string.StringMatcher;

import java.security.InvalidParameterException;
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
	 * Constructs a new Artist and all Album it contains from the file specified.
	 * @param directory a File representing a directory containing albums
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid artist
	 */
	public Artist(String name) throws InvalidParameterException {
		super(new Element(name));
		id++;
	}
	
	/**
	 * Constructs a new Artist from the node specified.
	 * @param Element a zicfile artist element
	 * @param retrieveFile if true retrieve and set musicFile from node informations
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid artist Element
	 */
	public Artist(Element artistElement) throws InvalidParameterException {
		super(artistElement);
		id++;
		// TODO verify that given node well correspond to an artist and implement the exception mechanism
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * Retrieves the style attribute of the XML node
	 * @return the style
	 */
	public String getStyle() {
		return elt.getAttributeValue("style");
	}
	
	/**
	 * Sets the given style as XML element attribute
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		elt.setAttribute("style",style);
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
				if (!StringMatcher.getInstance().match(album.getArtist(), albumList.get(0).getArtist())) {
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
	
	public Iterator<Album> getAlbumIterator() {
		return albumList.iterator();
	}
}

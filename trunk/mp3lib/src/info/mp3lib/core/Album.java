package info.mp3lib.core;

import info.mp3lib.core.xom.XMLAlbum;
import info.mp3lib.util.string.StringMatcher;

import java.security.InvalidParameterException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;

/**
 * A container of physical music files. Hold the XMLAlbum. Allows to retrieve various information
 * from the directory.
 * @author Gabriel Pala
 */
public class Album {
	
	public enum albumTagEnum {
		ALL_SAME_TAGS(16), // all track are tagged with the same tag
		ALL_DIFF_TAGS(8), // all track are tagged but at less two of them have different tag
		SOME_SAME_TAGS(4), // some track are tagged with the same tag
		SOME_DIFF_TAGS(2), // some track are tagged but at less two of them have different tag
		NO_TAGS(0); // no track is tagged

		private int value;

		private albumTagEnum(final int pValue) {
			value = pValue;
		}
		
	}
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Album.class.getName());
	
	/** The artist name of this album */
	private String artist;
	
	/** The tag state of this album */
	private albumTagEnum tagState;
	
	/** Collection of Tracks */
	private List<Track> trackList;
	
	/** The XOM album */
	private XMLAlbum xmlAlbum;
	
	/** Total number of ALbum */
	private static int id = 0;
	/* ----------------------- CONSTRUCTORS ----------------------- */
	
	/**
	 * Constructs a new empty Album.
	 * This method should not be called directly, used by <code>MusicDataScanner.read(File)</code>
	 */
	public Album() {
		id++;
		xmlAlbum = new XMLAlbum(new Integer(id).toString());
		trackList = new LinkedList<Track>();
		tagState = albumTagEnum.NO_TAGS;
	}
	
	/**
	 * Constructs a new Album from the Element specified.
	 * @param Element a zicfile album element
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid album Element
	 */
	@SuppressWarnings("unchecked")
	public Album(final Element albumElement) throws InvalidParameterException {
		super();
		xmlAlbum = new XMLAlbum(albumElement);
		// retrieve the list of Files contained by this directory from albumElement
		List<Element> listTrackElement = albumElement.getChildren();
		trackList = new LinkedList<Track>(); //TODO remove
		for (Iterator<Element> iterator = listTrackElement.iterator(); iterator
				.hasNext();) {
			final Element trackElement = iterator.next();
			try {
				trackList.add(new Track(trackElement)); // TODO use add method instead
			} catch (InvalidParameterException e) {
				LOGGER.warn(new StringBuffer("Unable to build a Track from ")
						.append(trackElement.getAttribute("name"))
						.append(" : ")
						.append(e.getMessage()).toString());
			}
		}		
		if (trackList.isEmpty()) {
			throw new InvalidParameterException(new StringBuffer("Album : ")
					.append(albumElement.getAttribute("name"))
					.append(" does not contain any valid audio files").toString());
		}
	}

	/* ------------------------- METHODS --------------------------- */	
	/**
	 * Adds the given track to this album
	 */
	public void add(Track track) {
		trackList.add(track);
		if (track.isTagged()) {
			if (tagState == albumTagEnum.NO_TAGS) {
				if (trackList.size() == 1) {
					tagState = albumTagEnum.ALL_SAME_TAGS;
				} else {
					tagState = albumTagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == albumTagEnum.ALL_SAME_TAGS) {
				// match album of first and current track
				if (!StringMatcher.getInstance().match(track.getAlbum(), trackList.get(0).getAlbum())) {
					tagState = albumTagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == albumTagEnum.SOME_SAME_TAGS) {
				// do nothing
			} else if (tagState == albumTagEnum.SOME_DIFF_TAGS) {
				// do nothing
			}
		} else {
			if (tagState == albumTagEnum.ALL_SAME_TAGS) {
				tagState = albumTagEnum.SOME_SAME_TAGS;
			} else if (tagState == albumTagEnum.ALL_DIFF_TAGS) {
				tagState = albumTagEnum.SOME_DIFF_TAGS;
			}
		}
	}
	
	public Iterator<Track> getTrackIterator() {
		return trackList.iterator();
	}
	
	/**
	 * Checks if the current directory contains at less one file containing tag information.
	 * @return true if album tracks are tagged, else return false
	 */
	public boolean isTagged() {
		return tagState != albumTagEnum.NO_TAGS;
	}
	
	/**
	 * Checks if at less more than half of music files of the current directory contains different values
	 * compared two by two for tags artist or album
	 * @return true if album is known to be a compilation, else return false
	 */
	public boolean isCompilation() {
		return tagState == albumTagEnum.ALL_DIFF_TAGS || tagState == albumTagEnum.SOME_DIFF_TAGS;
	}
	
	/**
	 * build the track Element from informations retrieved from the album directory
	 * and set it in this.node
	 */
	protected void buildElementFromFile() {
		// TODO method implementation
	}
	
	/**
	 * @return the tagState
	 */
	public albumTagEnum getTagState() {
		return tagState;
	}
	
	public XMLAlbum getXMLElement() {
		return xmlAlbum;
	}
	
}

package info.mp3lib.core;

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
public class Album extends XMLMusicElement {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Album.class.getName());
	
	/** The tag state of this album */
	private TagEnum tagState;
	
	/** Collection of Tracks */
	private List<Track> trackList;
	
	/** Total number of ALbum */
	private static int id = 0;
	/* ----------------------- CONSTRUCTORS ----------------------- */
	
	/**
	 * Constructs a new empty Album.
	 * This method should not be called directly, used by <code>MusicDataScanner.read(File)</code>
	 */
	public Album() {
		super(new Element(new Integer(id).toString()));
		id++;
		trackList = new LinkedList<Track>();
		tagState = TagEnum.NO_TAGS;
	}
	
	/**
	 * Constructor
	 * @param name the name of this element
	 */
	public Album(String name) {
		super(new Element(name));
		id++;
	}
	
	/**
	 * Constructs a new Album from the Element specified.
	 * @param Element a zicfile album element
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid album Element
	 */
	@SuppressWarnings("unchecked")
	public Album(final Element albumElement) throws InvalidParameterException {
		super(albumElement);
		id++;
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
	 * Retrieves the name of the parent artist element
	 * @return the artist
	 */
	public String getArtist() {
		final Element parent = (Element)elt.getParent();
		return parent.getAttributeValue("name");
	}

	/**
	 * Retrieves the size attribute of the XML node
	 * @return the size
	 */
	public int getSize() {
		return Integer.parseInt(elt.getAttributeValue("size"));
	}

	/**
	 * Sets the given size as XML element attribute
	 * @param size the size to set
	 */
	public void setSize(final int size) {
		elt.setAttribute("size", new Integer(size).toString());
	}

	/**
	 * Retrieves the year attribute of the XML node
	 * @return the year
	 */
	public int getYear() {
		return Integer.parseInt(elt.getAttributeValue("year"));
	}

	/**
	 * Sets the given artist as XML element attribute
	 * @param year the year to set
	 */
	public void setArtist(String name) {
		final Element parent = (Element)elt.getParent();
		// TODO : recherche xom si il existe un artiste du nom 'name'
		//		- si oui, on déplace le node de l'album
		//		- si non, on créé un nouveau node artist, dans lequel on déplace le node album.
		
//		parent.setAttribute("name", name);
	}
	
	/**
	 * Sets the given year as XML element attribute
	 * @param year the year to set
	 */
	public void setYear(final int year) {
		elt.setAttribute("year", new Integer(year).toString());
	}
	/**
	 * Adds the given track to this album
	 */
	public void add(Track track) {
		trackList.add(track);
		if (track.isTagged()) {
			if (tagState == TagEnum.NO_TAGS) {
				if (trackList.size() == 1) {
					tagState = TagEnum.ALL_SAME_TAGS;
				} else {
					tagState = TagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == TagEnum.ALL_SAME_TAGS) {
				// match album of first and current track
				if (!StringMatcher.getInstance().match(track.getAlbum(), trackList.get(0).getAlbum())) {
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
	
	public Iterator<Track> getTrackIterator() {
		return trackList.iterator();
	}
	
	/**
	 * Checks if the current directory contains at less one file containing tag information.
	 * @return true if album tracks are tagged, else return false
	 */
	public boolean isTagged() {
		return tagState != TagEnum.NO_TAGS;
	}
	
	/**
	 * Checks if at less more than half of music files of the current directory contains different values
	 * compared two by two for tags artist or album
	 * @return true if album is known to be a compilation, else return false
	 */
	public boolean isCompilation() {
		return tagState == TagEnum.ALL_DIFF_TAGS || tagState == TagEnum.SOME_DIFF_TAGS;
	}
	
	/**
	 * @return the tagState
	 */
	public TagEnum getTagState() {
		return tagState;
	}
}

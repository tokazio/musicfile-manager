package info.mp3lib.core;

import info.mp3lib.core.validator.Validator;
import info.mp3lib.util.cddb.ITagQueryResult;
import info.mp3lib.util.string.MatcherConfig;
import info.mp3lib.util.string.MatcherFactory;
import info.mp3lib.util.string.StringMatcher;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Element;
import org.jdom.IllegalAddException;

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
	 * This method should not be called directly, used by <code>DataScanner.read(File)</code>
	 * @param name the name of this album.<br/>
	 * Initialized with the physical name of the original album folder by <code>DataScanner.read(File)</code>
	 */
	public Album(final String name) {
		super(new Element(ELT_ALBUM));
		setName(name);
		id++;
		setId(id);
		trackList = new LinkedList<Track>();
		tagState = TagEnum.NO_TAGS;
		LOGGER.warn("NO XML UPDATE OF PHYSICAL TAG !!!");
		// TODO:  XML UPDATE OF PHYSICAL TAGs..
	}

	/**
	 * Constructs a new Album from the Element specified.
	 * @param Element a zicfile album element
	 * @throws IllegalArgumentException when the Element given in parameters
	 * doesn't correspond to a valid album Element
	 */
	@SuppressWarnings("unchecked")
	public Album(final Element albumElement) throws IllegalArgumentException {
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
			} catch (IllegalArgumentException e) {
				LOGGER.warn(new StringBuilder("Unable to build a Track from ")
				.append(trackElement.getAttribute(XMLMusicElement.ATTR_NAME))
				.append(" : ")
				.append(e.getMessage()).toString());
			}
		}
		if (trackList.isEmpty()) {
			throw new IllegalArgumentException(new StringBuilder("Album [")
			.append(albumElement.getAttribute(XMLMusicElement.ATTR_NAME))
			.append("] does not contain any valid audio files").toString());
		}
	}

	/* ------------------------- METHODS --------------------------- */

	/**
	 * TODO: set new tags for this album
	 */
	public void write(ITagQueryResult tag) {
		if (!getArtistName().equals(tag.getArtist())) {
			this.moveTo(tag.getArtist());
		}
		if (!getName().equals(tag.getAlbum())) {
			this.setName(tag.getAlbum());
		}
		if (getYear() != Integer.parseInt(tag.getYear())) {
			this.setYear(Integer.parseInt(tag.getYear()));
		}
		//TODO ... other fields ??
	}


	/**
	 * Retrieves the name of the parent artist element
	 * @return the artist
	 */
	public String getArtistName() {
		final Element parent = (Element)getElement().getParent();
		return parent.getAttributeValue(XMLMusicElement.ATTR_NAME);
	}

	/**
	 * Retrieves the size attribute of the XML node
	 * @return the size
	 */
	public int getSize() {
		return trackList.size();
//		return Integer.parseInt(getElement().getAttributeValue(XMLMusicElement.ATTR_SIZE));
	}

	/**
	 * Sets the given size as XML element attribute
	 * @param size the size to set
	 */
	public void setSize(final int size) {
		getElement().setAttribute(XMLMusicElement.ATTR_SIZE, new Integer(size).toString());
	}

	/**
	 * Retrieves the year attribute of the XML node
	 * @return the year
	 */
	public int getYear() {
		return Integer.parseInt(getElement().getAttributeValue(XMLMusicElement.ATTR_YEAR));
	}

	/**
	 * <p>Moves this album to the artist denoted by the specified name<br/>
	 * (This one will be created and added to library if it does not already exist)</p>
	 * <p>Removes beforehand this album from its actual artist if there is one.</p>
	 * @param artistName the name denoting the artist in which to move this album
	 * @throws IllegalArgumentException if the given artist name is null or empty
	 */
	public void moveTo(final String artistName) {
		final Library library = Library.getInstance();
		if (artistName == null || artistName.trim().length() == 0) {
			throw new IllegalArgumentException("Given artist name can't be null or empty");
		}
		// if this album already belongs to an artist
		final Element parentArtist = (Element) getElement().getParent();
		if (parentArtist != null) {
			// retrieve the artist
			final Artist artist = library.getArtist(parentArtist.getName());
			// and remove the album from it
			if (!artist.remove(this)) {
				// warn if the album was in the XOM but not in the object model
				LOGGER.warn(new StringBuilder("Data corruption, XOM and Object model for the album [")
				.append(getName()).append("], id [").append(getId()).append("] are desynchronised")
				.toString());

				// remove the artist if empty
				if (artist.isEmpty()) {
					library.remove(artist);
				}
			}
		}
		library.getArtist(artistName).add(this);
	}

	/**
	 * Sets the given year as XML element attribute
	 * @param year the year to set
	 */
	public void setYear(final int year) {
		getElement().setAttribute(XMLMusicElement.ATTR_YEAR, new Integer(year).toString());
	}

	/**
	 * Adds the given track to this album
	 * @param track the track to add
	 * @throws IllegalAddException if the given track already has a parent.
	 */
	public void add(Track track) throws IllegalAddException {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(new StringBuilder("Album [").append(getName()).append("]: Adding track [")
					.append(track.getName()).append("], file [").append(track.getAbsolutePath()).append("]...").toString());
		}
		getElement().addContent(track.getElement()); // adds the track element to the album element
		trackList.add(track); // adds the file to the album list
//		setSize(getSize()+1); // update album size
		if (track.isTagged()) {
			if (tagState == TagEnum.NO_TAGS) {
				if (trackList.size() == 1) {
					tagState = TagEnum.ALL_SAME_TAGS;
				} else {
					tagState = TagEnum.SOME_SAME_TAGS;
				}
			} else if (tagState == TagEnum.ALL_SAME_TAGS) {
				// match album of first and current track
				final StringMatcher tagMatcher = MatcherFactory.getInstance().getMatcher(MatcherConfig.TAG);
				if (!tagMatcher.match(track.getAlbumName(), trackList.get(0).getAlbumName())) {
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
	 * Removes the given track from this album
	 * @param track the track to remove
	 * @return true if the track was successfully removed, false otherwise
	 */
	public boolean remove(Track track) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(new StringBuilder("Album [").append(getName()).append("]: Removing track [")
					.append(track.getName()).append("]...").toString());
		}
		boolean success = false;
		if (getElement().removeChild(track.getName()))  {
			success = trackList.remove(track);
		}
		if (!success) {
			LOGGER.debug("Failure");
		}
		return success;
	}

	/**
	 * Launch validation procedure for this album...
	 */
	public void validate() {
		Validator vdt = new Validator(this);
		// Class entry point method:
		vdt.validate();
	}

	/**
	 * Retrieves an iterator on the track collection of this album
	 * @return a track iterator
	 */
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
	 * Checks if the track list of this album contains no elements. 
	 * @return true if this album is empty
	 */
	public boolean isEmpty() {
		return trackList.isEmpty();
	}

	/**
	 * @return the tagState
	 */
	public TagEnum getTagState() {
		return tagState;
	}

	/**
	 * Retrieves the parent folder of the first track of this album.<br/>
	 * /!\ At some point of its lifecycle an album can holds tracks located in different directories
	 * TODO rajouter un attribut fsSynchronisation
	 * @return the original folder from which this album was build
	 */
	public File getFile() {
		return trackList.get(1).getFile().getParentFile();
	}
	
	/**
	 * Retrieves the parent file path of the current album
	 * @return the parent of the original folder path from which this album was build
	 * @see <code>Album.getFile()</code>
	 */
	public String getParentPath() {
		return getFile().getParentFile().getPath();
	}

	/**
	 * @return an array of all the name of tracks contained in this album
	 */
	public String[] getTracksName() {
		final String[] result = new String[trackList.size()];
		int i = 0;
		for (Iterator<Track> iterator = trackList.iterator(); iterator.hasNext();) {
			result[i] = iterator.next().getFileName();
			i++;
		}
		return result;
	}
	
	/**
	 * Convenient method to retrieve the first track of this album to avoid redundant analysis when tag state
	 * is <code>TagEnum.ALL_SAME_TAGS</code>
	 * @return the first track of this album
	 */
	public Track getFirstTrack() {
		return trackList.get(0);
	}
}

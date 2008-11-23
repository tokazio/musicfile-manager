package info.mp3lib.core;

import java.io.File;

import org.apache.log4j.Logger;
import org.jdom.Element;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

/**
 * All objects corresponding to a music files in audio format.
 * @author Gabriel Pala
 */
public class Track extends XMLMusicElement {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Track.class.getName()); 
	
	/** the physical file holding tag object (inherits from File) */
	private AudioFile musicFile;
	
	/** total number of Tracks */
	private static int id = 0;
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new audio from the file specified.
	 * @param audioFile a file in audio format
	 * @throws IllegalArgumentException when the File given in parameters
	 * doesn't correspond to a valid audio file
	 */
	public Track(File audioFile) throws IllegalArgumentException {
		super(new Element(ELT_TRACK));
		id++;
		try {
			musicFile = AudioFileIO.read(audioFile);
			buildElementFromFile();
		} catch (CannotReadException e) {
				throw new IllegalArgumentException(e.getMessage());
		}
	}

	/**
	 * Constructs a new audio from the node specified.
	 * @param trackElement a zicfile track element
	 * @throws IllegalArgumentException when the Element given in parameters
	 * doesn't correspond to a valid track Element
	 */
	public Track(Element trackElement) throws IllegalArgumentException {
		super(trackElement);
		try {
			musicFile = AudioFileIO.read(new File(getPath()));
		} catch (CannotReadException e) {
			throw new IllegalArgumentException("The given XML contains invalid audio files : "
					.concat(getPath()));
		}
	}
	
	/**
	 * Constructor
	 * @param name the title of this track
	 */
	public Track(final String name) {
		super(new Element(ELT_TRACK));
		id++;
		setId(id);
		setName(name);
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * Retrieves the name of the parent album element
	 * @return the album name
	 */
	public String getAlbumName() {
		return getElement().getParentElement().getAttributeValue(XMLMusicElement.ATTR_NAME);
	}

	/**
	 * Retrieves the name of the parent artist element
	 * @return the artist
	 */
	public String getArtistName() {
		return getElement().getParentElement().getParentElement().getAttributeValue(XMLMusicElement.ATTR_NAME);
	}

	/**
	 * <p>Moves this track to the album denoted by the specified name in the artist 
	 * denoted by the specified name too.<br/>
	 * (Those one will be created and added to library if they do not already exist)</p>
	 * <p>Removes beforehand this track from its actual album if there is one.</p>
	 * @param artistName the name of the artist containing the album below 
	 * (can be null, in this case the album will be moved in default unknown artist)
	 * @param albumName the name of the album in which to move this track
	 * @throws IllegalArgumentException if the given album name is null or empty
	 */
	public void moveTo(String artistName, final String albumName) {
		final Library library = Library.getInstance();
		if (albumName == null || albumName.trim().length() == 0) {
			throw new IllegalArgumentException("Given album name can't be null or empty");
		}
		// if this track already belongs to an album
		final Element parentAlbum = (Element) getElement().getParent();
		if (parentAlbum != null) {
			final Element parentArtist = (Element) parentAlbum.getParent();// an album is necessary in a artist
			// retrieve the album
			final Album album = library.getAlbum(parentArtist.getName(), albumName);
			// and remove the track from it
			if (!album.remove(this)) {
				// warn if the track was in the XOM but not in the object model
				LOGGER.warn(new StringBuffer("Data corruption, XOM and Object model for the track [")
				.append(getName()).append("], id [").append(getId()).append("] are desynchronised")
				.toString());
				
				// remove the album if empty
				if (album.isEmpty()) {
					library.getArtist(album.getArtistName()).remove(album);
				}
			}
		}
		if (artistName == null) {
			artistName = Library.DEFAULT_UNKNOWN_ELT_NAME;
		}
		// retrieve the album denoted by the given name
		final Album album = library.getAlbum(artistName, albumName);
		album.add(this);
	}

	/**
	 * Retrieves the size attribute of the XML node
	 * @return the size
	 */
	public int getSize() {
		return Integer.parseInt(getElement().getAttributeValue(XMLMusicElement.ATTR_SIZE));
	}
	
	/**
	 * Sets the given size as XML element attribute
	 * @param size the size to set
	 */
	public void setSize(final int size) {
		getElement().setAttribute(XMLMusicElement.ATTR_SIZE, new Integer(size).toString());
	}
	
	/**
	 * Retrieves the length attribute of the XML node
	 * @return the length (in seconds)
	 */
	public int getLength() {
		return Integer.parseInt(getElement().getAttributeValue(XMLMusicElement.ATTR_LENGTH));
	}
	
	/**
	 * Sets the given length as XML element attribute
	 * @param length the length to set (in seconds)
	 */
	public void setLength(final int length) {
		getElement().setAttribute(XMLMusicElement.ATTR_LENGTH, new Integer(length).toString());
	}
	
	/**
	 * Retrieves the absolute path of this track
	 * @return the path
	 */
	public String getAbsolutePath() {
		return musicFile.getAbsolutePath();
	}
	
	/**
	 * Retrieves the path attribute of the XML node
	 * @return the path
	 */
	public String getPath() {
		return new StringBuffer(getElement().getParentElement().getAttributeValue(XMLMusicElement.ATTR_PATH))
		.append(File.separator).append(getFileName()).toString();
	}

	/**
	 * Retrieves the filename attribute of the XML node
	 * @return the filename
	 */
	public String getFileName() {
		return (getElement().getParentElement().getAttributeValue(XMLMusicElement.ATTR_FILENAME));
	}

	
	/**
	 * Returns true if the current file contains tag information.
	 * @return true if file is tagged, else return false
	 */
	public boolean isTagged() {
		return ! musicFile.getTag().isEmpty();
	}

	/**
	 * Retrieve tags from the audio file.
	 * @return a Tag object containing all tag information of the audioFile
	 * return null if tags aren't available
	 */
	public Tag getTag() {
		return musicFile.getTag();
	}
	
	/**
	 * Retrieve the file associated to the audio file.
	 * @return a file pointer on this track file
	 */
	public File getFile() {
		return musicFile;
	}

	/**
	 * build the track Element from informations retrieved from the audio file
	 * and set it in this.node
	 */
	private void buildElementFromFile() {
		final Tag tag = musicFile.getTag();
//		setAlbum(tag.getFirstAlbum()); //TODO check when and how set the album (artist ?). here does not seems good
		setId(id);
		setName(tag.getFirstTitle());
		setCode(0);
		setLength(musicFile.getLength());
		setSize(new Long(musicFile.length() * 1024).intValue());
	}

}

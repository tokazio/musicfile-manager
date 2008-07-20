package info.mp3lib.core;

import java.io.File;
import java.security.InvalidParameterException;

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
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid audio file
	 */
	public Track(File audioFile) throws InvalidParameterException {
		id++;
		try {
			musicFile = AudioFileIO.read(audioFile);
			buildElementFromFile();
		} catch (CannotReadException e) {
				LOGGER.debug("the given file is not in a supported audio format : "
						.concat(audioFile.getAbsolutePath()));
				throw new InvalidParameterException(e.getMessage());
		}
	}

	/**
	 * Constructs a new audio from the node specified.
	 * @param trackElement a zicfile track element
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid track Element
	 */
	public Track(Element trackElement) throws InvalidParameterException {
		super(trackElement);
		try {
			musicFile = AudioFileIO.read(new File(getPath()));
		} catch (CannotReadException e) {
			throw new InvalidParameterException("The given XML contains invalid audio files : "
					.concat(getPath()));
		}
	}
	
	/**
	 * Constructor
	 * @param name the name of this element
	 */
	public Track(String name) {
		super(new Element(name));
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * Retrieves the album attribute of this track Element if it exists
	 * else return the name of the parent album element
	 * @return the album
	 */
	public String getAlbum() {
		String album;
		album = elt.getAttributeValue("album");
		if (album == null) {
			final Element parent = (Element)elt.getParent();
			album = parent.getAttributeValue("name");
		}
		return album;
	}

	/**
	 * Retrieves the of the parent artist element
	 * @return the artist
	 */
	public String getArtist() {
		return elt.getParentElement().getParentElement().getAttributeValue("name");
	}

	/**
	 * Sets the given album as XML element attribute
	 * /!\ if the track denoted by this is not in a compilation
	 * you should set the album at album level
	 * @param album the album to set
	 */
	public void setAlbum(final String album) {
		elt.setAttribute("album",album);
	}

	/**
	 * Sets the given artist as XML element attribute
	 * /!\ if the track denoted by this is not in a compilation
	 * you should set the artist at artist level
	 * @param artist the artist to set
	 */
	public void setArtist(final String artist) {
		elt.setAttribute("artist",artist);
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
	 * Retrieves the length attribute of the XML node
	 * @return the length (in seconds)
	 */
	public int getLength() {
		return Integer.parseInt(elt.getAttributeValue("length"));
	}
	
	/**
	 * Sets the given length as XML element attribute
	 * @param length the length to set (in seconds)
	 */
	public void setLength(final int length) {
		elt.setAttribute("length", new Integer(length).toString());
	}
	
	/**
	 * Retrieves the path attribute of the XML node
	 * @return the path
	 */
	public String getPath() {
		return (elt.getParentElement().getAttributeValue("path"));
	}

	/**
	 * Returns true if the current file contains tag information.
	 * @return true if file is tagged, else return false
	 */
	public boolean isTagged() {
		return musicFile.getTag().isEmpty();
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
	 * build the track Element from informations retrieved from the audio file
	 * and set it in this.node
	 */
	private void buildElementFromFile() {
		final Tag tag = musicFile.getTag();
		elt = new Element(musicFile.getName());
		setAlbum(tag.getFirstAlbum());
		setArtist(tag.getFirstArtist());
		setName(tag.getFirstTitle());
		setCode(0);
		setId(id);
		setLength(musicFile.getLength());
		setSize(new Long(musicFile.length() * 1024).intValue());
	}

}

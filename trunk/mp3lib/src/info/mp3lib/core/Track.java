package info.mp3lib.core;

import info.mp3lib.core.xom.XMLTrack;

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
public class Track {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(Track.class.getName()); 
	
	/** the physical file holding tag object (inherits from File) */
	private AudioFile musicFile;
	
	/** The XOM track */
	private XMLTrack xmlTrack;
	
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
		xmlTrack = new XMLTrack(trackElement);
		try {
			musicFile = AudioFileIO.read(new File(xmlTrack.getPath()));
		} catch (CannotReadException e) {
			throw new InvalidParameterException("The given XML contains invalid audio files : "
					.concat(xmlTrack.getPath()));
		}
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * Returns true if the current file contains tag information.
	 * @return true if file is tagged, else return false
	 */
	public boolean isTagged() {
		return musicFile.getTag().isEmpty();
	}

	public String getArtist() {
		return musicFile.getTag().getFirstArtist();
	}
	
	public String getAlbum() {
		return musicFile.getTag().getFirstAlbum();
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
		xmlTrack = new XMLTrack(musicFile.getName());
		xmlTrack.setAlbum(tag.getFirstAlbum());
		xmlTrack.setArtist(tag.getFirstArtist());
		xmlTrack.setName(tag.getFirstTitle());
		xmlTrack.setCode(0);
		xmlTrack.setId(id);
		xmlTrack.setLength(musicFile.getLength());
		xmlTrack.setSize(new Long(musicFile.length() * 1024).intValue());
	}
	
	/**
	 * Retrieves the duration in seconds
	 * @return number of Tracks of this Album
	 */
	public int getLength() {
		return musicFile.getLength();
		
	}

	/**
	 * @return the xmlTrack
	 */
	public XMLTrack getXMLElement() {
		return xmlTrack;
	}

}

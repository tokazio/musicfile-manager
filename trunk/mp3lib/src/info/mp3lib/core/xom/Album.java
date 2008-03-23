package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * All objects corresponding to a directory containing music files. Allows to retrieve various information
 * from the directory.
 * @author Gabriel Pala
 */
public class Album extends AbstractMusicDirectory {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName());
	
	/** The artist name of this album */
	private String artist;
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Album from the file specified .
	 * @param directory a File representing a directory containing music files
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid album
	 */
	public Album(File albumDirectory) throws InvalidParameterException {
		super(albumDirectory);
		buildElementFromFile();
		// Initialize listFile with Tracks contained in this Album
		final File[] listFiles = albumDirectory.listFiles();
		final List<ITaggedMusicFile> linkedListFile = new LinkedList<ITaggedMusicFile>();
		for (int i = 0; i < listFiles.length; i++) {
			try {
				linkedListFile.add(new Track(listFiles[i]));
			} catch (InvalidParameterException e) {
				LOGGER.warn(new StringBuffer("Unable to build a Track from ")
						.append(listFiles[i].getName())
						.append(" : ")
						.append(e.getMessage()).toString());
			}
		}
		if (linkedListFile.isEmpty()) {
			throw new InvalidParameterException(new StringBuffer("Album : ")
					.append(albumDirectory.getName())
					.append(" does not contain any valid audio files").toString());
		}
		listFile = (ITaggedMusicFile[])linkedListFile.toArray();
	}
	
	/**
	 * Constructs a new Album from the file and the array of Tracks specified.
	 * @param directory a File representing a directory containing music files
	 * @param listFileArg all the Track instances contained in this Album
	 */
	public Album(final File directory, final ITaggedMusicFile[] listFileArg) {
		super(directory, listFileArg);
		listFile = listFileArg;
		buildElementFromFile();
	}
	
	/**
	 * Constructs a new Album from the Element specified.
	 * @param Element a zicfile album element
	 * @param artist The artist name of this album
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid album Element
	 */
	public Album(final Element albumElement, String artist ) throws InvalidParameterException {		
		super(albumElement);
		// retrieve the list of Files contained by this directory from albumElement
		NodeList listTrackElement = albumElement.getChildNodes();
		final List<ITaggedMusicFile> linkedListFile = new LinkedList<ITaggedMusicFile>();
		for (int i = 0; i < listTrackElement.getLength(); i++) {
			final Element trackElement = (Element)listTrackElement.item(i);
			try {
				linkedListFile.add(new Track(trackElement));
			} catch (InvalidParameterException e) {
				LOGGER.warn(new StringBuffer("Unable to build a Track from ")
						.append(trackElement.getAttribute("name"))
						.append(" : ")
						.append(e.getMessage()).toString());
			}
		}		
		if (linkedListFile.isEmpty()) {
			throw new InvalidParameterException(new StringBuffer("Album : ")
					.append(((Element)node).getAttribute("name"))
					.append(" does not contain any valid audio files").toString());
		}
		listFile = (ITaggedMusicFile[])linkedListFile.toArray();
	}

	/* ------------------------- METHODS --------------------------- */	
	/**
	 * Checks if the current directory contains at less one file containing tag information.
	 * @author
	 * @return true if album tracks are tagged, else return false
	 */
	@Override
	public boolean isTagged() {
		boolean tagged = false;
		// TODO method implementation
		return tagged;
	}
	
	/**
	 * Checks if at less more than half of music files of the current directory contains different values
	 * compared two by two for tags artist or album
	 * @return true if album is known to be a compilation, else return false
	 */
	public boolean isCompilation() {
		boolean compilation = false;
		// TODO method implementation
		return compilation;
	}
	
	/**
	 * build the track Element from informations retrieved from the album directory
	 * and set it in this.node
	 */
	protected void buildElementFromFile() {
		// TODO method implementation
	}
	
	/**
	 * retrieves the name of the album, ie. tag album if possible else physical name
	 * @return the name of the album
	 */
	private String getTagName() {
		String name = new String();
		// TODO method implementation
		return name;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return artist;
	}

	/**
	 * @param artist the artist to set
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}

}

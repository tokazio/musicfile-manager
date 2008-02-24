package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;

import org.w3c.dom.Element;

/**
* All objects corresponding to a directory containing some albums (ie. directories containing music files).
* @author Gabriel Pala
*/
public class Artist extends AbstractMusicDirectory {
	/* ------------------------ ATTRIBUTES ------------------------ */
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Artist from the file specified.
	 * @param directory a File representing a directory containing albums
	 * @param buildNode if true build and set node according to informations retrieved from mp3File
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid artist
	 */
	public Artist(File artistDirectory, boolean buildNode) throws InvalidParameterException {
		super(artistDirectory);
		// TODO verify the validity of argument and implement the exception mechanism
		if (buildNode) {
			buildElementFromFile();
		}
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
		// TODO verify that given node well correspond to an artist and implement the exception mechanism
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the name of the artist, ie. tag album if possible else physical name
	 * @return the name of the artist
	 */
	@Override
	public String getNameFromTag() {
		String name = new String();
		// TODO method implementation
		return name;
	}
	
	/**
	 * Checks if the current directory contains at less one album containing tag information.
	 * @author
	 * @return true if artist tracks are tagged, else return false
	 */
	@Override
	public boolean isTagged() {
		boolean tagged = false;
		// TODO method implementation
		return tagged;
	}
	
	/**
	 * build the track Element from informations retrieved from the artist directory
	 * and set it in this.node
	 */
	protected void buildElementFromFile() {
		// TODO method implementation
	}
	
}

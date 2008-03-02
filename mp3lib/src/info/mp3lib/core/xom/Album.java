package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;

import org.w3c.dom.Element;

/**
 * All objects corresponding to a directory containing music files. Allows to retrieve various information
 * from the directory.
 * @author Gabriel Pala
 */
public class Album extends AbstractMusicDirectory {
	/* ------------------------ ATTRIBUTES ------------------------ */
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Album from the file specified.
	 * @param directory a File representing a directory containing music files
	 * @param buildNode if true build and set node according to informations retrieved from mp3File
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid album
	 */
	public Album(File albumDirectory, boolean buildNode) throws InvalidParameterException {
		super(albumDirectory, buildNode);
		// TODO verify the validity of argument and implement the exception mechanism
		if (buildNode) {
			buildElementFromFile();
		}

	}
	
	/**
	 * Constructs a new Album from the Element specified.
	 * @param Element a zicfile album element
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid album Element
	 */
	public Album(Element albumElement) throws InvalidParameterException {		
		super(albumElement);
		// TODO verify that given node well correspond to an album and implement the exception mechanism
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the name of the album, ie. tag album if possible else physical name
	 * @return the name of the album
	 */
	@Override
	public String getNameFromTag() {
		String name = new String();
		// TODO method implementation
		return name;
	}
	
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
	 * Checks if at less two music file of the current directory contains different values
	 * for tags artist or album
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

}

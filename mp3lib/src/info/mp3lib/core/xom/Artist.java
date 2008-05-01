package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

/**
* All objects corresponding to a directory containing some albums (ie. directories containing music files).
* @author Gabriel Pala
*/
public class Artist extends AbstractMusicContainer {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName());
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Artist and all Album it contains from the file specified.
	 * @param directory a File representing a directory containing albums
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid artist
	 */
	public Artist(String name) throws InvalidParameterException {
		super();
		// TODO singleton holding the DocumentBuilder
		// TODO enum album
		Document doc =  DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
		Element element = doc.createElement("artist");
		// TODO verify the validity of argument and implement the exception mechanism
			buildElementFromFile();
	}
	
	/**
	 * Constructs a new Artist and all Album it contains from the file specified.
	 * @param directory a File representing a directory containing albums
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid artist
	 */
	public Artist(File artistDirectory) throws InvalidParameterException {
		super(artistDirectory);
		// TODO verify the validity of argument and implement the exception mechanism
			buildElementFromFile();
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

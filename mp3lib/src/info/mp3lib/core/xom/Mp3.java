package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

/**
 * All objects corresponding to a music files in mp3 format.
 * @author Gabriel Pala
 */
public class Mp3 extends AbstractMusicFile implements ITaggedMusicFile{
	/* ------------------------ ATTRIBUTES ------------------------ */
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Mp3 from the file specified.
	 * @param mp3File a file in mp3 format
	 * @param buildNode if true build and set node according to informations retrieved from mp3File
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid mp3 file
	 */
	public Mp3(File mp3File, boolean buildNode) throws InvalidParameterException {
		super(mp3File);
		// TODO verify the validity of argument and implement the exception mechanism
		// if (mp3File == null || mp3File.getName() != *.mp3) => throw new InvalidParameterException
		if (buildNode) {
			buildTrackElementFromFile();
		}
	}
	
	/**
	 * Constructs a new Mp3 from the node specified.
	 * @param node a zicfile element (artist, album or track)
	 * @param retrieveFile if true retrieve and set musicFile from node informations
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid track Element
	 */
	public Mp3(Node node, boolean retrieveFile) throws InvalidParameterException {
		super(node);
		// TODO verify the validity of argument and implement the exception mechanism
		if (retrieveFile) {
			RetrieveFileFromTrackElement();
		}
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the name of the mp3, ie. tag title if possible else physical name
	 * @return the title of the mp3 song
	 */
	@Override
	public String getName() {
		String name = new String();
		return name;
	}
	
	/**
	 * Returns true if the current file contains tag information.
	 * @author
	 * @return true if file is tagged, else return false
	 */
	@Override
	public boolean isTagged() {
		boolean tagged = false;
		// TODO method implementation
		return tagged;
	}
	
	/**
	 * Retrieve tags from the mp3 file.
	 * @author
	 * @return tagMap a map binding tag name and tag value, 
	 * return null if tags aren't available
	 */
	public Map<String, String> getTags() {
		Map<String, String> tagMap =  new HashMap<String, String>();
		// TODO method implementation
		return tagMap;
	}
	
	/**
	 * build the track Element from informations retrieved from the mp3 file
	 * and set it in this.node
	 */
	private void buildTrackElementFromFile() {
		// TODO method implementation
	}
	
	/**
	 * Retrieve a File pointing on the mp3 file from path attribute of Element track
	 * and set it in this.musicFile
	 */
	private void RetrieveFileFromTrackElement() {
		// TODO method implementation
	}

}

package info.mp3lib.core.xom;

import java.security.InvalidParameterException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import org.apache.log4j.*;
import org.apache.log4j.spi.LoggerFactory;

/**
 * All objects corresponding to a music files in mp3 format.
 * @author Gabriel Pala
 */
public class Track extends AbstractMusicFile implements ITaggedMusicFile{
	/* ------------------------ ATTRIBUTES ------------------------ */
	 private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName()); 
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new Mp3 from the file specified.
	 * @param mp3File a file in mp3 format
	 * @param buildNode if true build and set node according to informations retrieved from mp3File
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid mp3 file
	 */
	public Track(AudioFile mp3File, boolean buildNode) throws InvalidParameterException {
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
	public Track(Node node, boolean retrieveFile) throws InvalidParameterException {
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
	 * @return a Tag object containing all tag information of the audioFile
	 * return null if tags aren't available
	 */
	public Tag getTag() {
		return ((AudioFile)musicFile).getTag();
	}
	
	/**
	 * build the track Element from informations retrieved from the mp3 file
	 * and set it in this.node
	 * @throws ParserConfigurationException 
	 */
	private void buildTrackElementFromFile() {
		try {
			LOGGER.debug("buildTrackElementFromFile()");
			Document doc =  DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			Element element = doc.createElement("track");
			element.setAttribute("code", "0");
			element.setAttribute("currentPath", musicFile.getPath());
			element.setAttribute("newPath", "");
			element.setAttribute("name", getTag().getFirstTitle());
			element.setAttribute("length",new StringBuffer().append(musicFile.length()).toString());
			element.setAttribute("size",new StringBuffer().append(((AudioFile)musicFile).getLength()).toString());
			node = element;
		} catch (DOMException e) {
			LOGGER.error(new StringBuffer("unable to build XML element : ")
					.append(e.getMessage()).toString());
			//e.printStackTrace();
		} catch (ParserConfigurationException e) {
			LOGGER.error(new StringBuffer("unable to build XML element : ")
			.append(e.getMessage()).toString());
			//e.printStackTrace();
		}
	}
	
	/**
	 * Retrieve a File pointing on the mp3 file from path attribute of Element track
	 * and set it in this.musicFile
	 */
	private void RetrieveFileFromTrackElement() {
		// TODO method implementation
	}

}

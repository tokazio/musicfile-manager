package info.mp3lib.core.xom;

import info.mp3lib.util.string.StringUtils;

import java.io.File;
import java.security.InvalidParameterException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;

/**
 * All objects corresponding to a music files in audio format.
 * @author Gabriel Pala
 */
public class Track extends AbstractMusicFile implements ITaggedMusicFile{
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName()); 
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new audio from the file specified.
	 * @param audioFile a file in audio format
	 * @param buildNode if true build and set node according to informations retrieved from audioFile
	 * @throws InvalidParameterException when the File given in parameters
	 * doesn't correspond to a valid audio file
	 */
	public Track(File audioFile, boolean buildNode) throws InvalidParameterException {
		super(audioFile, buildNode);
		try {
			musicFile = AudioFileIO.read(audioFile);
		} catch (CannotReadException e) {
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
			musicFile = AudioFileIO.read(new File(trackElement.getAttribute("currentPath")));
		} catch (CannotReadException e) {
			// should never happen, only correct audio files are listed in zicfile
		}
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the name of the audio, ie. tag title if possible else physical name
	 * @return the title of the audio song
	 */
	public String getTagName() {
		String name = getTag().getFirstTitle();
		if (name.trim().isEmpty()) {
			// TODO maybe check if some common pattern like 'CD' or 'encoded by' could be removed
			name = StringUtils.removeExtension(name);
		}
		return name;
	}

	/**
	 * Returns true if the current file contains tag information.
	 * @return true if file is tagged, else return false
	 */
	@Override
	public boolean isTagged() {
		return ((AudioFile)musicFile).getTag().isEmpty();
	}

	/**
	 * Retrieve tags from the audio file.
	 * @return a Tag object containing all tag information of the audioFile
	 * return null if tags aren't available
	 */
	public Tag getTag() {
		return ((AudioFile)musicFile).getTag();
	}

	/**
	 * build the track Element from informations retrieved from the audio file
	 * and set it in this.node
	 * @throws ParserConfigurationException 
	 */
	protected void buildElementFromFile() throws ParserConfigurationException {
		LOGGER.debug("buildTrackElementFromFile()");
		Document doc =  DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
		Element element = doc.createElement("track");
		element.setAttribute("code", "0");
		element.setAttribute("currentPath", musicFile.getPath());
		element.setAttribute("newPath", "");
		element.setAttribute("name", getTagName());
		element.setAttribute("length",new StringBuffer().append(musicFile.length()).toString());
		element.setAttribute("size",new StringBuffer().append(((AudioFile)musicFile).getLength()).toString());
		node = element;
	}
	
	public final float getXMLLength() { return Float.parseFloat(getXML("length")); }
	/**
	 * Retourne la value de l'attribut XML en parametre.
	 * TODO: OU NULL si inexistant.. / throw exception...
	 * @param arg String XML argument
	 * @return String XML value
	 */

}

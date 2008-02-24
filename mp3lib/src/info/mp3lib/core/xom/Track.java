package info.mp3lib.core.xom;

import info.mp3lib.util.string.StringUtils;

import java.security.InvalidParameterException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;

/**
 * All objects corresponding to a music files in mp3 format.
 * @author Gabriel Pala
 */
public class Track extends AbstractMusicFile implements ITaggedMusicFile{
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** Apache log4j logger */
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
		super(mp3File, buildNode);
		// TODO verify the validity of argument and implement the exception mechanism
		// if (mp3File == null || mp3File.getName() != *.mp3) => throw new InvalidParameterException		
	}

	/**
	 * Constructs a new Mp3 from the node specified.
	 * @param trackElement a zicfile track element
	 * @throws InvalidParameterException when the Element given in parameters
	 * doesn't correspond to a valid track Element
	 */
	public Track(Element trackElement) throws InvalidParameterException {
		super(trackElement);
		// TODO verify the validity of argument and implement the exception mechanism
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the name of the mp3, ie. tag title if possible else physical name
	 * @return the title of the mp3 song
	 */
	@Override
	public String getNameFromTag() {
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
	protected void buildElementFromFile() throws ParserConfigurationException {
		LOGGER.debug("buildTrackElementFromFile()");
		Document doc =  DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
		Element element = doc.createElement("track");
		element.setAttribute("code", "0");
		element.setAttribute("currentPath", musicFile.getPath());
		element.setAttribute("newPath", "");
		element.setAttribute("name", getNameFromTag());
		element.setAttribute("length",new StringBuffer().append(musicFile.length()).toString());
		element.setAttribute("size",new StringBuffer().append(((AudioFile)musicFile).getLength()).toString());
		node = element;
	}

}

package info.mp3lib.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.InvalidParameterException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Basic abstract implementation of Interface MusicFile.
 * @author Gabriel Pala
 */
public abstract class AbstractMusicFile implements IMusicFile {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** The current music file;*/
	protected File musicFile;
	
	/** The XML element representing this in the zicfile */
	protected IXMLMusicElement xmlElement;
	
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName());
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new AbstractMusicFile from the file specified.
	 * @param musicFile a file in any music format
	 */
	public AbstractMusicFile(final File musicFileArg) {
		musicFile = musicFileArg;
		try {
			buildElementFromFile();
		} catch (ParserConfigurationException e) {
			LOGGER.error(new StringBuffer("unable to build XML element : ")
			.append(e.getMessage()).toString());
			//e.printStackTrace();
		}
	}
	
	/**
	 * Constructs a new empty AbstractMusicFile.
	 */
	public AbstractMusicFile() {
		xmlElement = null;
		musicFile = null;
	}
	
	/**
	 * Constructs a new AbstractMusicFile from the node specified.
	 * @param node a zicfile element (artist, album or track)
	 * TODO review and implement the method
	 */
	public AbstractMusicFile(final Node nodeArg) {		
		try {
			RetrieveFileFromElement();
		} catch (FileNotFoundException e) {
			LOGGER.error(new StringBuffer("unable to retrieve the associated music file ")
			.append(e.getMessage()).toString());
			throw new InvalidParameterException("Node given is misformed");
			//e.printStackTrace();
		}
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the java absolute path (using / as separator) of the current music file.
	 * @return the file path
	 */
	@Override
	public String getAbsolutePath() {
		return musicFile.getAbsolutePath();
	}

	/**
	 * retrieves the file size (in KB) of the current music file.
	 * @return the file size
	 */
	@Override
	public long getFileSize() {
		// TODO manage directory case
		return musicFile.length();
	}
	
	/**
	 * retrieves the name of the current music file.
	 * @return the file name
	 */
	@Override
	public String getFileName() {
		return musicFile.getName();
	}
	
	/**
	 * retrieves the zicFile node associated to the current music file
	 * @return the node if it exists, else return null
	 */
	@Override
	public Node getNode() {
		return node;
	}
	
	/**
	 * retrieves the File associated to the current music file
	 * @return the File if it exists, else return null
	 */
	@Override
	public File getFile() {
		return musicFile;
	}
	
	/**
	 * Return XML value of XML attribute in parameter
	 * @param arg String XML Argument
	 * @return String XML value
	 */
	protected final String getXML(final String arg)
	{
		return ((Element)node).getAttribute(arg);
	}
	
	/**
	 * Checks if the current file or directory contains tag informations.
	 * @author
	 * @return true if directory is tagged, else return false
	 */
	abstract public boolean isTagged();
	
	/**
	 * Retrieve the name of this music file ie. title for track or album/artist name
	 * @return the name of this music file
	 */
	public final String getName() { 
		return getXML("name"); 
	}
	
	/**
	 * build the track Element from informations retrieved from the mp3 file
	 * and set it in this.node
	 * @throws ParserConfigurationException 
	 */
	protected abstract void buildElementFromFile() throws ParserConfigurationException;
	
	/**
	 * Retrieve a File pointing on the associated music file from path attribute of the current Element
	 * and set it in this.musicFile
	 * @throws FileNotFoundException when attribute currentPath of the current element doesn't
	 * represent a real file
	 */
	private void RetrieveFileFromElement() throws FileNotFoundException {
		LOGGER.debug("RetrieveFileFromTrackElement()");
		if (node != null) {
			final String path = ((Element)node).getAttribute("currentPath");
			File f = new File(path);
			if (!f.exists()) {
				throw new FileNotFoundException();
			} else {
				musicFile = f;
			}
		}
	}
}

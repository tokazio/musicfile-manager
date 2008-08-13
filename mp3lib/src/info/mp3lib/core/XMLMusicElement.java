package info.mp3lib.core;



import info.mp3lib.config.Config;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Abstract implementation of {@link IXMLMusicElement} interface
 * @author Gab
 */
public abstract class XMLMusicElement implements IXMLMusicElement {

	/** The XML node */
	private Element elt;
	
	/**
	 * Constructor
	 * @param element
	 */
	public XMLMusicElement(final Element element) {
		elt = element;
	}
	
	/**
	 * Defaut constructor
	 * Never called explicitly
	 */
	protected XMLMusicElement() {
	}
	
	/**
	 * Retrieves the XML element holding the IMusicFile parent data
	 * @return the XML node
	 */
	public Element getElement() {
		return elt;
	}
	
	/**
	 * Sets the given XML element
	 * @param element the element to set
	 */
	protected void setElement(final Element element) {
		elt = element;
	}
	
	/**
	 * Retrieves the id attribute of the XML node
	 * @return the id
	 */
	public int getId() {
		return Integer.parseInt(elt.getAttributeValue(ATTR_ID));
	}
	
	/**
	 * Sets the given id as XML element attribute
	 * @param id the id to set
	 */
	public void setId(final int id) {
		elt.setAttribute(ATTR_ID, new Integer(id).toString());
	}
	
	/**
	 * Retrieves the code attribute of the XML node
	 * @return the code
	 */
	public int getCode() {
		return Integer.parseInt(elt.getAttributeValue(ATTR_ID));
	}
	
	/**
	 * Sets the given code as XML element attribute
	 * @param code the code to set
	 */
	public void setCode(final int code) {
		elt.setAttribute(ATTR_CODE, new Integer(code).toString());
	}
	
	/**
	 * Retrieves the name attribute of the XML node
	 * @return the name
	 */
	public String getName() {
		return elt.getAttributeValue(ATTR_NAME);
	}
	
	/**
	 * Sets the given name as XML element attribute
	 * @param name the name to set
	 */
	public void setName(final String name) {
		elt.setAttribute(ATTR_NAME,name);
	}
	
	/**
	 * Saves the XML node in the zicfile.xml file on the file system.
	 * @throws IOException when an IO error occurs
	 */
	public void save() throws IOException {
		File f = new File(Config.getInstance().getZicFilePath());
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		PrintWriter pw = new PrintWriter(f);
		try {
			outputter.output(elt.getDocument(), pw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* ------------------------- CONSTANTS --------------------------- */
	
	/** name attribute : used by artist, album, track */
	public final static String ATTR_NAME = "name";
	
	/** id attribute : used by artist, album, track */
	public final static String ATTR_ID = "id";
	
	/** code attribute : used by artist, album, track */
	public final static String ATTR_CODE = "code";
	
	/** size attribute : used by album, track */
	public final static String ATTR_SIZE = "size";
	
	/** style attribute : used by artist */
	public final static String ATTR_STYLE = "style";
	
	/** path attribute : used by album */
	public final static String ATTR_PATH = "path";
	
	/** year attribute : used by album */
	public final static String ATTR_YEAR = "year";
	
	/** length attribute : used by track */
	public final static String ATTR_LENGTH = "length";
	
	/** XML artist element */
	public final static String ELT_ARTIST = "artist";
	
	/** XML album element */
	public final static String ELT_ALBUM = "album";
	
	/** XML track element */
	public final static String ELT_TRACK = "track";
}

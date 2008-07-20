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
	protected Element elt;
	
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
	 * Retrieves the id attribute of the XML node
	 * @return the id
	 */
	public int getId() {
		return Integer.parseInt(elt.getAttributeValue("id"));
	}
	
	/**
	 * Sets the given id as XML element attribute
	 * @param id the id to set
	 */
	public void setId(final int id) {
		elt.setAttribute("id", new Integer(id).toString());
	}
	
	/**
	 * Retrieves the code attribute of the XML node
	 * @return the code
	 */
	public int getCode() {
		return Integer.parseInt(elt.getAttributeValue("id"));
	}
	
	/**
	 * Sets the given code as XML element attribute
	 * @param code the code to set
	 */
	public void setCode(final int code) {
		elt.setAttribute("code", new Integer(code).toString());
	}
	
	/**
	 * Retrieves the name attribute of the XML node
	 * @return the name
	 */
	public String getName() {
		return elt.getAttributeValue("name");
	}
	
	/**
	 * Sets the given name as XML element attribute
	 * @param name the name to set
	 */
	public void setName(final String name) {
		elt.setAttribute("name",name);
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
	
}

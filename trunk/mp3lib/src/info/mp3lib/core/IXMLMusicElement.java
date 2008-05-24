package info.mp3lib.core;

import org.jdom.Element;

/**
 * Denotes all objects that holds an XML node containing music file
 * data.
 * @author Gab
 */
public interface IXMLMusicElement {

	/**
	 * Retrieves the XML node holding the IMusicFile parent data
	 * @return the XML node
	 */
	public Element getElement();
	
	/**
	 * Retrieves the id attribute of the XML node
	 * @return the id
	 */
	public int getId();
	
	/**
	 * Sets the given id as XML element attribute
	 * @param id the id to set
	 */
	public void setId(final int id);
	
	/**
	 * Retrieves the code attribute of the XML node
	 * @return the code
	 */
	public int getCode();
	
	/**
	 * Sets the given code as XML element attribute
	 * @param code the code to set
	 */
	public void setCode(final int code);
	
	/**
	 * Retrieves the name attribute of the XML node
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Sets the given name as XML element attribute
	 * @param name the name to set
	 */
	public void setName(final String name);	
	
}

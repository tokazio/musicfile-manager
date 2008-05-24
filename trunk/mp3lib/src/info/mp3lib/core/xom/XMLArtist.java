package info.mp3lib.core.xom;

import org.jdom.Element;

import info.mp3lib.core.IXMLMusicElement;

public class XMLArtist extends XMLMusicElement implements IXMLMusicElement {
	
	/**
	 * Constructor
	 * @param element
	 */
	public XMLArtist(final Element element) {
		super(element);
	}
	
	/**
	 * Retrieves the style attribute of the XML node
	 * @return the style
	 */
	public String getStyle() {
		return elt.getAttributeValue("style");
	}
	
	/**
	 * Sets the given style as XML element attribute
	 * @param style the style to set
	 */
	public void setStyle(final String style) {
		elt.setAttribute("style",style);
	}
	
}

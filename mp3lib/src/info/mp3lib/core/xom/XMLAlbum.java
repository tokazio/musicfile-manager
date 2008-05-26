package info.mp3lib.core.xom;

import org.jdom.Element;

import info.mp3lib.core.IXMLMusicElement;

public class XMLAlbum extends XMLMusicElement implements IXMLMusicElement {

	/**
	 * Constructor
	 * @param element
	 */
	public XMLAlbum(final Element element) {
		super(element);
	}
	
	/**
	 * Constructor
	 * @param name the name of this element
	 */
	public XMLAlbum(String name) {
		super(new Element(name));
	}
	
	/**
	 * Retrieves the name of the parent artist element
	 * @return the artist
	 */
	public String getArtist() {
		final Element parent = (Element)elt.getParent();
		return parent.getAttributeValue("name");
	}

	/**
	 * Retrieves the size attribute of the XML node
	 * @return the size
	 */
	public int getSize() {
		return Integer.parseInt(elt.getAttributeValue("size"));
	}

	/**
	 * Sets the given size as XML element attribute
	 * @param size the size to set
	 */
	public void setSize(final int size) {
		elt.setAttribute("size", new Integer(size).toString());
	}

	/**
	 * Retrieves the year attribute of the XML node
	 * @return the year
	 */
	public int getYear() {
		return Integer.parseInt(elt.getAttributeValue("year"));
	}

	/**
	 * Sets the given year as XML element attribute
	 * @param year the year to set
	 */
	public void setArtist(String name) {
		final Element parent = (Element)elt.getParent();
		// TODO : recherche xom si il existe un artiste du nom 'name'
		//		- si oui, on déplace le node de l'album
		//		- si non, on créé un nouveau node artist, dans lequel on déplace le node album.
		
//		parent.setAttribute("name", name);
	}
	
	/**
	 * Sets the given year as XML element attribute
	 * @param year the year to set
	 */
	public void setYear(final int year) {
		elt.setAttribute("year", new Integer(year).toString());
	}
}

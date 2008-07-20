package info.mp3lib.core.xom;

import info.mp3lib.core.IXMLMusicElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Denotes an object that holds an xml node containing a track data.
 * @author Gab
 */
public class XMLTrack extends XMLMusicElement implements IXMLMusicElement {
	
	/**
	 * Constructor
	 * @param element
	 */
	public XMLTrack(final Element element) {
		super(element);
	}
	
	/**
	 * Constructor
	 * @param name the name of this element
	 */
	public XMLTrack(String name) {
		super(new Element(name));
	}
	
	public void write() throws FileNotFoundException {
		// TODO Get file Path / filename
		File f = null;
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		PrintWriter pw = new PrintWriter(f);
		try {
			outputter.output(elt.getDocument(), pw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Retrieves the album attribute of this track Element if it exists
	 * else return the name of the parent album element
	 * @return the album
	 */
	public String getAlbum() {
		String album;
		album = elt.getAttributeValue("album");
		if (album == null) {
			final Element parent = (Element)elt.getParent();
			album = parent.getAttributeValue("name");
		}
		return album;
	}

	/**
	 * Retrieves the artist attribute of this track Element if it exists
	 * else return the artist hold by the parent album element if it exists
	 * else return the name of the parent artist element
	 * @return the artist
	 */
	public String getArtist() {
		String artist;
		artist = elt.getAttributeValue("artist");
		if (artist == null) {
			final Element parent = elt.getParentElement();
			artist = new XMLAlbum(parent).getArtist();
		}
		return artist;
	}

	/**
	 * Sets the given album as XML element attribute
	 * /!\ if the track denoted by this is not in a compilation
	 * you should set the album at album level
	 * @param album the album to set
	 */
	public void setAlbum(final String album) {
		elt.setAttribute("album",album);
	}

	/**
	 * Sets the given artist as XML element attribute
	 * /!\ if the track denoted by this is not in a compilation
	 * you should set the artist at artist level
	 * @param artist the artist to set
	 */
	public void setArtist(final String artist) {
		elt.setAttribute("artist",artist);
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
	 * Retrieves the length attribute of the XML node
	 * @return the length (in seconds)
	 */
	public int getLength() {
		return Integer.parseInt(elt.getAttributeValue("length"));
	}
	
	/**
	 * Sets the given length as XML element attribute
	 * @param length the length to set (in seconds)
	 */
	public void setLength(final int length) {
		elt.setAttribute("length", new Integer(length).toString());
	}
	
	/**
	 * Retrieves the path attribute of the XML node
	 * @return the path
	 */
	public String getPath() {
		return (elt.getParentElement().getAttributeValue("path"));
	}

}

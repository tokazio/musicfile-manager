package info.mp3lib.core;

import java.io.File;

import org.w3c.dom.Node;

/**
 * All objects corresponding to a music file (It includes directory containing music files)
 * Irrespective of the file music format (see ITaggedMusicFile for this aspect)
 * @author Gabriel Pala
 */
public interface IMusicFile {
	
	/**
	 * retrieves the java absolute path (using / as separator) of the current music file.
	 * @return the file path
	 */
	public String getAbsolutePath();
	
	/**
	 * retrieves the file size (in KB) of the current music file.
	 * @return the file size
	 */
	public long getFileSize();
	
	/**
	 * retrieves the name of the current music file.
	 * @return the file name
	 */
	public String getFileName();
	
	/**
	 * retrieves the File associated to the current music file
	 * @return the File if it exists, else return null
	 */
	public File getFile();
	
	/**
	 * retrieves the xml element associated to the current music file
	 * @return the xml element
	 */
	public IXMLMusicElement getXMLElement();

}

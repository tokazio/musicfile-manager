package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
/**
 * Object corresponding to a directory on the file system containing some music files (ie. Tracks or ALbums)
 * @author Gabriel Pala
 */
public abstract class AbstractMusicDirectory extends AbstractMusicFile implements IMusicDirectory {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** array of AbstractMusicFile contained in this AbstractMusicDirectory */
	protected ITaggedMusicFile[] listFile;
	
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName()); 
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new AbstractMusicDirectory from the file specified.
	 * @param directory a File representing a directory containing music files
	 */
	public AbstractMusicDirectory(File directory) {
		super(directory);		
	}
	
	/**
	 * Constructs a new AbstractMusicDirectory from the node specified.
	 * @param node a zicfile element representing an artist or album
	 */
	public AbstractMusicDirectory(Node node) {
		super(node);
	}
	
	/* ------------------------- METHODS --------------------------- */
	
	/**
	 * Checks if the current directory contains at less one file containing tag information.
	 * @author
	 * @return true if directory is tagged, else return false
	 */
	@Override
	public boolean isTagged() {
		boolean tagged = false;
		// TODO method implementation
		return tagged;
	}

	private final ITaggedMusicFile getItem(final int indice) {
		return listFile[indice]; 
	}
	
	/**
	 * Retrieves the number of tracks contained in this IMusicDirectory.
	 * @return number of IMusicFile of this Album
	 */
	public int getLength() {
		return listFile.length;
		
	}
	
}
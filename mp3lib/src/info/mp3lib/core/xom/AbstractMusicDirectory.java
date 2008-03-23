package info.mp3lib.core.xom;

import java.io.File;

import org.apache.log4j.Logger;
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
	 * Constructs a new AbstractMusicDirectory and all ITaggedMusicFile it contains from the file specified.
	 * @param directory a File representing a directory containing music files
	 */
	public AbstractMusicDirectory(final File directory) {
		super(directory);		
	}
	
	/**
	 * Constructs a new AbstractMusicDirectory from the file specified.
	 * @param directory a File representing a directory containing music files
	 * listFileArg all the ITaggedMusicFile instances contained in this AbstractMusicDirectory
	 */
	public AbstractMusicDirectory(final File directory, final ITaggedMusicFile[] listFileArg) {
		super(directory);
		listFile = listFileArg;
	}
	
	/**
	 * Constructs a new AbstractMusicDirectory from the node specified.
	 * @param node a zicfile element representing an artist or album
	 */
	public AbstractMusicDirectory(final Node node) {
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

	public final ITaggedMusicFile getItem(final int indice) {
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
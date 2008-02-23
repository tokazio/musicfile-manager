package info.mp3lib.core.xom;

import java.io.File;

import org.w3c.dom.Node;
/**
 * Object corresponding to a directory on the file system containing some music files
 * @author Gabriel Pala
 */
public abstract class AbstractMusicDirectory extends AbstractMusicFile implements ITaggedMusicFile {
	/* ------------------------ ATTRIBUTES ------------------------ */
	
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
	 * retrieves the name of the AbstractMusicDirectory
	 * @return the name of the AbstractMusicDirectory
	 */
	@Override
	public String getName() {
		String name = new String();
		// TODO method implementation
		return name;
	}
	
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
}

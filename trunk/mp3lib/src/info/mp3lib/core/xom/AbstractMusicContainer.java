package info.mp3lib.core.xom;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import com.sun.org.apache.bcel.internal.generic.LNEG;
/**
 * Object corresponding to a directory on the file system containing some music files (ie. Tracks or ALbums)
 * @author Gabriel Pala
 */
public abstract class AbstractMusicContainer extends AbstractMusicFile implements IMusicContainer {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** List of AbstractMusicFile contained in this AbstractMusicContainer */
	protected List<ITaggedMusicFile> listFile;
	
	/** Apache log4j logger */
	private final static Logger LOGGER = Logger.getLogger(AbstractMusicFile.class.getName()); 
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new AbstractMusicContainer and all ITaggedMusicFile it contains from the file specified.
	 * @param directory a File representing a directory containing music files
	 */
	public AbstractMusicContainer(final File directory) {
		super(directory);		
	}
	
	/**
	 * Constructs a new empty AbstractMusicContainer.
	 */
	public AbstractMusicContainer() {
		super();
		listFile = new LinkedList<ITaggedMusicFile>();
	}
	
	/**
	 * Constructs a new AbstractMusicContainer from the node specified.
	 * @param node a zicfile element representing an artist or album
	 */
	public AbstractMusicContainer(final Node node) {
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

	public final ITaggedMusicFile getItem(final int index) {
		return listFile.get(index); 
	}
	
	/**
	 * Retrieves the number of tracks contained in this IMusicContainer.
	 * @return number of IMusicFile of this Album
	 */
	public int getLength() {
		return listFile.size();
		
	}
	
}
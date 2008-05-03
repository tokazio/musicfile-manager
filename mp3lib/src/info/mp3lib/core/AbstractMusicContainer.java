package info.mp3lib.core;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
/**
 * Object corresponding to a directory on the file system containing some music files (ie. Tracks or ALbums)
 * @author Gabriel Pala
 */
public abstract class AbstractMusicContainer extends AbstractMusicFile implements IMusicContainer {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** List of AbstractMusicFile contained in this AbstractMusicContainer */
	protected List<IMusicFile> listFile;
	
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
		listFile = new LinkedList<IMusicFile>();
	}
	
	/**
	 * Constructs a new AbstractMusicContainer from the node specified.
	 * @param node a zicfile element representing an artist or album
	 */
	public AbstractMusicContainer(final Node node) {
		super(node);
	}
	
	/* ------------------- INHERITS / REQUIRED METHODS ---------------------- */
	
	public void add(ITaggedMusicFile f) {
		listFile.add(f);
	}
	
	public Iterator<IMusicFile> getIterator() {
		return listFile.iterator();
	}
	
	
	/* ------------------------- BUSINESS METHODS --------------------------- */

	public final IMusicFile getItem(final int index) {
		return listFile.get(index); 
	}
	
	public List<IMusicFile> getContainedList() {
		return listFile;
	}
	
	/**
	 * Retrieves the number of item contained in this Container.
	 * @return number of IMusicFile of this IMusicContainer
	 */
	public int getLength() {
		return listFile.size();
		
	}
	
}
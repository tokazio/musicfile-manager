package info.mp3lib.core.xom;

import java.io.File;

import org.w3c.dom.Node;

/**
 * Basic abstract implementation of Interface MusicFile.
 * @author Gabriel Pala
 */
public abstract class AbstractMusicFile implements IMusicFile {
	/* ------------------------ ATTRIBUTES ------------------------ */
	/** The current mp3 file;*/
	protected File musicFile;
	
	/** The XML element representing this in the zicfile */
	protected Node node;
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	/**
	 * Constructs a new AbstractMusicFile from the file specified.
	 * @param musicFile a file in any music format
	 */
	public AbstractMusicFile(File _musicFile) {
		musicFile = _musicFile;
		node = null;
	}
	
	/**
	 * Constructs a new AbstractMusicFile from the node specified.
	 * @param node a zicfile element (artist, album or track)
	 */
	public AbstractMusicFile(Node _node) {		
		node = _node;
		// TODO retrieve the current File corresponding to the zicfile element and set it in musicFile
	}
	
	/* ------------------------- METHODS --------------------------- */
	/**
	 * retrieves the java absolute path (using / as separator) of the current music file.
	 * @return the file path
	 */
	@Override
	public String getAbsolutePath() {
		return musicFile.getAbsolutePath();
	}

	/**
	 * retrieves the file size (in KB) of the current music file.
	 * @return the file size
	 */
	@Override
	public long getFileSize() {
		// TODO manage directory case
		return musicFile.length();
	}
	
	/**
	 * retrieves the name of the current music file.
	 * @return the file name
	 */
	@Override
	public String getFileName() {
		return musicFile.getName();
	}
	
	/**
	 * retrieves the zicFile node associated to the current music file
	 * @return the node if it exists, else return null
	 */
	@Override
	public Node getNode() {
		return node;
	}
	
	/**
	 * retrieves the File associated to the current music file
	 * @return the File if it exists, else return null
	 */
	@Override
	public File getFile() {
		return musicFile;
	}

}

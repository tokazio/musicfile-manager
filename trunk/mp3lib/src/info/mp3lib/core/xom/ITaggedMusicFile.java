package info.mp3lib.core.xom;

/**
 * All objects corresponding to a music file (It includes directory containing music files)
 * including tag management.
 * @author Gabriel Pala
 */
public interface ITaggedMusicFile extends IMusicFile {

	/**
	 * retrieves the name of the current object (artist or album name or song title).
	 * @return the name
	 */
	public String getName();
	
	/**
	 * retrieves the length of the current object 
	 * (number of items for artist or album and length in second for the Track ).
	 * @return the length of the music file
	 */
	public int getLength();
	
	/**
	 * Checks if the current file contains tag informations
	 * @return true if file is tagged, else return false
	 */
	public boolean isTagged();
}

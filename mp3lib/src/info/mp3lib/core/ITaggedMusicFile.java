package info.mp3lib.core;


/**
 * All objects corresponding to a music file (It includes directory containing music files)
 * including tag management.
 * @author Gabriel Pala
 */
public interface ITaggedMusicFile extends IMusicFile {

	public enum tagEnum {
		TAG_VALID_CDDB(32),
		TAG_MULTIPLE_CDDB(16),
		TAG_NO_CDDB(8),
		N0_TAG_VALID_CDDB(4),
		N0_TAG_MULTIPLE_CDDB(2),
		N0_TAG_NO_CDDB(0);

		private int value;

		private tagEnum(final int pValue) {
			value = pValue;
		}
		
	}
	
	public enum contextEnum {
		ARTIST_CONTAINER_CONTAINS_ONLY_VALID_ARTIST__ARTIST_CONTAINS_ONLY_VALID_ALBUM(20),
		ARTIST_CONTAINER_CONTAINS_ONLY_VALID_ARTIST__ARTIST_CONTAINS_MAJORITY_VALID_ALBUM(18), ARTIST_CONTAINER_CONTAINS_ONLY_VALID_ARTIST(16),
		ARTIST_CONTAINER_CONTAINS_MAJORITY_VALID_ARTIST__ARTIST_CONTAINS_ONLY_VALID_ALBUM(12),
		ARTIST_CONTAINER_CONTAINS_MAJORITY_VALID_ARTIST__ARTIST_CONTAINS_MAJORITY_VALID_ALBUM(10),
		ARTIST_CONTAINER_CONTAINS_MAJORITY_VALID_ARTIST(8),
		ARTIST_CONTAINS_ONLY_VALID_ALBUM(4),
		ARTIST_CONTAINS_MAJORITY_VALID_ALBUM(2),
		BAD_CONTEXT(0);

		private int value;

		private contextEnum(final int pValue) {
			value = pValue;
		}

	}
		
	/**
	 * retrieves the name of the current object (artist or album name or song
	 * title).
	 * 
	 * @return the name
	 */
	public abstract String getName();
	
	/**
	 * retrieves the length of the current object 
	 * (number of items for artist or album and length in second for the Track ).
	 * @return the length of the music file
	 */
	public abstract int getLength();
	
	/**
	 * Checks if the current file contains tag informations
	 * @return true if file is tagged, else return false
	 */
	public abstract boolean isTagged();
	
}

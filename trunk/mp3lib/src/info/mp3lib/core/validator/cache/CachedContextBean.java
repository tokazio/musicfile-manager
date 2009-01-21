package info.mp3lib.core.validator.cache;

/**
 * Holds data common to all album located in the same directory.<br/>
 * those data are retrieved from physical context<br/>
 * Stored in the ContextCache unique instance.
 */
import info.mp3lib.core.validator.PhysicalContext.ArtistPhysicalEnum;
import info.mp3lib.core.validator.TagContext.ArtistTagEnum;

public class CachedContextBean {
	/** all the artist names deduced from tags present in the current 'artist' directory */
	private String[] artistNames;
	
	/** If true, all album of this context have tag state <code>TagEnum.ALL_SAME_TAGS</code> */
	private boolean allALbumHighestState;
	
	/** artist name quality index modifiers */
	private ArtistPhysicalEnum[] artistQIModifiers;

	/**
	 * @return the artistNames
	 */
	public String[] getArtistNames() {
		return artistNames;
	}

	/**
	 * @param artistNames the artist names to set
	 */
	public void setArtistNames(String[] artistNames) {
		this.artistNames = artistNames;
	}

	/**
	 * @return the artist quality index modifiers
	 */
	public ArtistPhysicalEnum[] getArtistQIModifiers() {
		return artistQIModifiers;
	}

	/**
	 * @param artistQI the artist quality index modifiers to set
	 */
	public void setArtistQIModifiers(ArtistPhysicalEnum[] artistQI) {
		this.artistQIModifiers = artistQI;
	}
	
	
}
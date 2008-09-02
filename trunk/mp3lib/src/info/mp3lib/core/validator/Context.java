package info.mp3lib.core.validator;

public interface Context {

	/**
	 * Retrieves the quality index of the artist name deduced from the context
	 * @return the quality index for the artist name deduction
	 */
    public int getArtistQI();
    
    /**
	 * Retrieves the quality index of the album name deduced from the context
	 * @return the quality index for the album name deduction
	 */
    public int getAlbumQI();
    
    /**
	 * Retrieves the quality index of the tracks name deduced from the context
	 * @return the quality index for the tracks name deduction
	 */
    public int getTracksQI();
    
    /**
     * @return the alnum name deduced from the context
     */
    public String getAlbumName();
    
    /**
     * @return the artist name deduced from the context
     */
    public String getArtistName();
    
    /**
     * @return the tracks name deduced from the context
     */
    public String[] getTracksName();
    
    /**
     * @return the tracks length deduced from the context
     */
    public int[] getTracksLength();
    
    /**
     * @return the tracks count
     */
    public int getTracksCount();
}

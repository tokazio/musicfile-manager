package info.mp3lib.core.validator;

import info.mp3lib.core.TagEnum;

public class TagContext implements Context {

    public enum ArtistEnum {
	NO_TAGS(0); // no artist is tagged

	private int value;

	private ArtistEnum(final int pValue) {
		value = pValue;
	}
	public int getValue() {
	    return value;
	}
    };
    
    public enum AlbumEnum {
	NO_TAGS(0); // no album is tagged

	private int value;

	private AlbumEnum(final int pValue) {
		value = pValue;
	}
	public int getValue() {
	    return value;
	}
    };
    
    public enum TrackEnum {
	NO_TAGS(0); // no track is tagged

	private int value;

	private TrackEnum(final int pValue) {
		value = pValue;
	}

	public int getValue() {
	    return value;
	}
    };
    
    private ArtistEnum artistIQV;
    private AlbumEnum albumIVQ;
    private TagEnum tracksIQV;
    
    /* (non-Javadoc)
     * @see info.mp3lib.core.validator.Context#getAlbumName()
     */
    @Override
    public String getAlbumName() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.core.validator.Context#getArtistName()
     */
    @Override
    public String getArtistName() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.core.validator.Context#getTracksCount()
     */
    @Override
    public int getTracksCount() {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.core.validator.Context#getTracksLength()
     */
    @Override
    public int[] getTracksLength() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.core.validator.Context#getTracksName()
     */
    @Override
    public String[] getTracksName() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getArtistIQV() {
	return artistIQV.value;
    }
    
    @Override
    public int getAlbumIQV() {
	return albumIVQ.value;
    }

    @Override
    public int getTracksIQV() {
	return tracksIQV.getValue();
    }

}

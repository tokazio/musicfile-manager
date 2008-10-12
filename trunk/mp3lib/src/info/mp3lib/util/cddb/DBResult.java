package info.mp3lib.util.cddb;

import info.mp3lib.config.Config;
import info.mp3lib.core.validator.Context;
import info.mp3lib.util.string.MatcherContext;
import info.mp3lib.util.string.MatcherFactory;
import info.mp3lib.util.string.StringMatcher;


public abstract class DBResult implements ITagQueryResult {

    public DBResult() {
	// TODO Auto-generated constructor stub
    }

    public static enum AlbumTagResultEnum {
	/**
	 * Different artist tag field are present in tracks contained in directories at the same level
	 */
	SAME_ALBUM(Config.getInstance().getQIModifier(Config.DAL_SAME_ARTIST)),

	SAME_ARTIST(Config.getInstance().getQIModifier(Config.DAL_SAME_ALBUM)),
	
	SAME_DISCID(Config.getInstance().getQIModifier(Config.DAL_SAME_DISCID)),
	
	SAME_GENRE(Config.getInstance().getQIModifier(Config.DAL_SAME_GENRE)),
	
	SAME_YEAR(Config.getInstance().getQIModifier(Config.DAL_SAME_YEAR)),
	
	GOOD_QUALITY(Config.getInstance().getQIModifier(Config.DAL_GOOD_QUALITY))
	;
			
	private int value;

	private AlbumTagResultEnum(final int pValue) {
		value = pValue;
	}
	
	public int getValue() {
		return value;
	}
	
    }
    
    
    public int compareTo(Context context) {
	
	int finalIQV = 0;
	
	// TODO : compare each field ...
	StringMatcher matcher = MatcherFactory.getInstance().getMatcher(MatcherContext.TAG);
	if (matcher.match(getAlbum(), context.getAlbumName())) {
	    finalIQV += AlbumTagResultEnum.SAME_ALBUM.getValue();
	}
	if (matcher.match(getArtist(), context.getArtistName())) {
	    finalIQV += AlbumTagResultEnum.SAME_ARTIST.getValue();
	}
//	if (matcher.match(getDiscId(), context.getD)) {
//	    finalIQV += AlbumTagResultEnum.SAME_ARTIST.getValue();
//	}
//	if (matcher.match(getGenre(), context.getG)) {
//	    finalIQV += AlbumTagResultEnum.SAME_GENRE.getValue();
//	}
//	if (matcher.match(getYear(), context.getY)) {
//	    finalIQV += AlbumTagResultEnum.SAME_YEAR.getValue();
//	}
	// TODO: vérification du nom des pistes ...
//	context.getTracksName();
	
	return finalIQV;
    }
    
    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o) {
	// TODO : compare each field ...
	return 0;
    }

    @Override
    public String getAlbum() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getAlbumComment() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getArtist() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getCategory() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getDiscId()
     */
    @Override
    public String getDiscId() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getGenre()
     */
    @Override
    public String getGenre() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getQuality()
     */
    @Override
    public int getQuality() {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getTrackComment(int)
     */
    @Override
    public String getTrackComment(int i) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getTrackDuration(int)
     */
    @Override
    public int getTrackDuration(int i) {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getTrackNumber(int)
     */
    @Override
    public int getTrackNumber(int i) {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getTracksNumber()
     */
    @Override
    public int getTracksNumber() {
	// TODO Auto-generated method stub
	return 0;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getTrackTitle(int)
     */
    @Override
    public String getTrackTitle(int i) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#getYear()
     */
    @Override
    public String getYear() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#isExactMatch()
     */
    @Override
    public boolean isExactMatch() {
	// TODO Auto-generated method stub
	return false;
    }

    /* (non-Javadoc)
     * @see info.mp3lib.util.cddb.ITagQueryResult#swapTracks(int, int)
     */
    @Override
    public void swapTracks(int i1, int i2) {
	// TODO Auto-generated method stub
	
    }
    
}

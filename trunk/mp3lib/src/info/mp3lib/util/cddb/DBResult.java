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
	
	System.out.println("context.getAlbumName(): "+context.getAlbumName());
	
	// TODO : compare each field ... prendre en compte les champs null -> modifiers
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
    
}

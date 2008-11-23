package info.mp3lib.core.validator;

import java.util.Arrays;
import java.util.Iterator;

import info.mp3lib.config.Config;
import info.mp3lib.core.Album;
import info.mp3lib.core.TagEnum;
import info.mp3lib.core.Track;
import info.mp3lib.util.string.MatcherConfig;
import info.mp3lib.util.string.MatcherFactory;
import info.mp3lib.util.string.StringMatcher;
import info.mp3lib.util.string.StringPattern;

/**
 * Denotes all data deduced from tags of an album and the quality index associated to these values
 * /!\ all new modifiers added must be added in the config.properties file too
 * 
 * @author do - Gab
 */
public class TagContext implements Context
{

    private Album    album;

    private String   albumName;
    private String[] tracksName;

    /**
     * Denotes the possible value modifiers for the quality index of the artist name deduction from
     * the tag context The final QI returned by the <code>getArtistQI()</code> method is a sum of
     * all modifiers
     */
    public enum ArtistTagEnum
    {
	/** The artist field is missing for all the tracks of this album */
	NO_ARTIST_FIELD_SET(Config.getConfig().getQIModifier(Config.TAR_NO_ARTIST_FIELD_SET)),

	/** The artist field is set for some tracks of this album */
	SOME_ARTIST_FIELD_SET(Config.getConfig().getQIModifier(Config.TAR_SOME_ARTIST_FIELD_SET)),

	/**
	 * The artist field is set for all tracks of this album this modifiers have a weak value
	 * because it overloads <code>SOME_ARTIST_FIELD_SET</code>
	 */
	ALL_ARTIST_FIELD_SET(Config.getConfig().getQIModifier(Config.TAR_ALL_ARTIST_FIELD_SET)),

	/** The artist field is not the same for all the tracks of this album */
	SOME_DIFFERENT_ARTIST(Config.getConfig().getQIModifier(Config.TAR_SOME_DIFFERENT_ARTIST));

	private int value;

	private ArtistTagEnum(final int pValue) {
	    value = pValue;
	}

	public int getValue()
	{
	    return value;
	}
    };

    /**
     * Denotes the possible value modifiers for the quality index of the album name deduction from
     * the tag context<br/>
     * The final QI returned by the <code>getAlbumQI()</code> method is a sum of all modifiers
     */
    public enum AlbumTagEnum
    {
	/** The album field is missing for all the tracks of this album */
	NO_ALBUM_FIELD_SET(Config.getConfig().getQIModifier(Config.TAL_NO_ALBUM_FIELD_SET)),

	/** The album field is set for all the tracks of this album */
	ALL_TITLE_FIELD_SET(Config.getConfig().getQIModifier(Config.TAL_ALL_TITLE_FIELD_SET)),

	/** The album field is not the same for all the tracks of this album */
	SOME_DIFFERENT_ALBUM(Config.getConfig().getQIModifier(Config.TAL_SOME_DIFFERENT_ALBUM)),

	/** The artist field is not the same for all the tracks of this album */
	SOME_DIFFERENT_ARTIST(Config.getConfig().getQIModifier(Config.TAL_SOME_DIFFERENT_ARTIST));

	private int value;

	private AlbumTagEnum(final int pValue) {
	    value = pValue;
	}

	public int getValue()
	{
	    return value;
	}
    };

    /**
     * Denotes the possible value modifiers for the quality index of the tracks name deduction from
     * the tag context<br/>
     * The final QI returned by the <code>getTracksQI()</code> method is a sum of all modifiers
     */
    public enum TrackTagEnum
    {
	/** The field title is missing for all the tracks of this album */
	NO_TITLE_FIELD_SET(Config.getConfig().getQIModifier(Config.TTR_NO_TITLE_FIELD_SET)),

	/** The field title is set for all the tracks of this album */
	ALL_TITLE_FIELD_SET(Config.getConfig().getQIModifier(Config.TTR_ALL_TITLE_FIELD_SET)),

	/**
	 * The title field of all tracks contains the word defined as invalid in the program
	 * configuration ("track", "piste", , ...) this modifiers have a weak value because it
	 * overloads <code>REPEATING_SEQUENCE</code>
	 */
	CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(
		Config.TTR_CONTAINS_INVALIDER_WORD)),

	/**
	 * The title field of all tracks contains the same reapeating sequence wich is not include
	 * in the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
	 */
	REPEATING_SEQUENCE(Config.getConfig().getQIModifier(Config.TTR_REPEATING_SEQUENCE)),

	/**
	 * Once eliminated the common repeating sequence for the title field of all tracks, the
	 * remaining variable sequence is short (less than 3 char) or only composed of decimal
	 * characters<br/>
	 * overloads <code>REPEATING_SEQUENCE</code>
	 */
	NO_ALPHADECIMAL_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(
		Config.TTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE)),

	/**
	 * Once eliminated the common repeating sequence for the title field of all tracks, the
	 * remaining variable sequence is important (more than 2 char) and composed of alphadecimal
	 * (not only decimal) characters
	 */
	BIG_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(Config.TTR_BIG_VARIABLE_SEQUENCE));

	private int value;

	private TrackTagEnum(final int pValue) {
	    value = pValue;
	}

	public int getValue()
	{
	    return value;
	}
    };

    /** artist name quality index modifiers */
    private ArtistTagEnum[] artistQI;

    /** album name quality index modifiers */
    private AlbumTagEnum[]  albumQI;

    /** tracks name quality index modifiers */
    private TrackTagEnum[]  tracksQI;

    public TagContext(Album album) {
	// TODO: MAKE CONSTRUCTOR
	this.album = album;
	tracksName = new String[album.getSize()];
	artistQI = new ArtistTagEnum[4];
	albumQI = new AlbumTagEnum[4];
	tracksQI = new TrackTagEnum[6];
    }

    public void processAlbumContext()
    {
	/**
	 * -> up if FIELDS are SET or SAME ... need modifiers -> incrémentation impossible sur tablo
	 * d'enum ..
	 */

	final StringMatcher tagMatcher = MatcherFactory.getInstance().getMatcher(MatcherConfig.TAG);

	// TODO try to get best Album and Artist name ...
	String bestAlbumName = null, bestArtistName = null;

	if (album.getTagState().equals(TagEnum.ALL_SAME_TAGS))
	{
	    Track firstOne = album.getTrackIterator().next();
	    bestAlbumName = firstOne.getAlbumName();
	    bestArtistName = firstOne.getArtistName();
	}

	else
	{
	    // iterate ONCE
	    boolean continueLoop = true;
	    int i = 0;
	    StringPattern artistPattern = null;
	    StringPattern albumPattern = null;
	    for (Iterator<Track> iTrack = album.getTrackIterator(); iTrack.hasNext()
		    && continueLoop; i++)
	    {
		Track track = iTrack.next();
		/** @set Track name */
		tracksName[i] = track.getName();

		/** @CHECK NO_TITLE_FIELD_SET */
		if (album.getTagState().equals(TagEnum.NO_TAGS))
		{
		    /** @set all QI */
		    setValueTracksQI(TrackTagEnum.NO_TITLE_FIELD_SET);
		    setValueAlbumQI(AlbumTagEnum.NO_ALBUM_FIELD_SET);
		    setValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET);
		    continueLoop = false;
		}
		// TODO: Add to Album.add() ALL/SAME_ALBUM and ALL/SAME_ARTIST
		// difference
		else
		{
		    /** @CHECK ALL_TITLE_FIELD_SET */
		    if (album.getTagState().equals(TagEnum.ALL_SAME_TAGS))
		    {
			// all same tags and first artist set => ALL_ARTIST_FIELD_SET
			if (track.getArtistName() != null && !track.getArtistName().isEmpty()
				&& bestArtistName == null)
			{
			    setValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
			    bestArtistName = track.getArtistName();
			}
			// all same tags and first album set => ALL_ALBUM_FIELD_SET
			if (track.getAlbumName() != null && !track.getAlbumName().isEmpty()
				&& bestAlbumName == null)
			{
			    setValueAlbumQI(AlbumTagEnum.ALL_TITLE_FIELD_SET);
			    bestAlbumName = track.getAlbumName();
			}
			// stop loop process
			continueLoop = false;
		    }
		    else
		    {
			if (album.getTagState().equals(TagEnum.SOME_SAME_TAGS))
			{
			    // SAME ARTISTS IF DEFINED
			    if (track.getArtistName() == null
				    || (track.getArtistName() != null && track.getArtistName()
					    .isEmpty()))
			    {
				// no artist field
				setValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET);
			    }
			    else if (bestArtistName == null)
			    {
				// first iteration, set current artist name to best one
				// initialize QI to : SOME ARTISTS & ALL ARTISTS
				bestArtistName = track.getArtistName();
				artistPattern = tagMatcher.getPattern(bestArtistName);
				setValueArtistQI(ArtistTagEnum.SOME_ARTIST_FIELD_SET);
				setValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
			    }
			    else if (bestArtistName != null)
			    {
				// next, check for false ALL_ARTIST_FIELD
				if (track.getArtistName() == null
					|| (track.getArtistName() != null && track.getArtistName()
						.isEmpty()))
				{
				    setNullValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
				}
				// check for DIFFERENT_ARTIST
				if (!artistPattern.match(track.getArtistName()))
				{
				    setValueArtistQI(ArtistTagEnum.SOME_DIFFERENT_ARTIST);
				    setValueAlbumQI(AlbumTagEnum.SOME_DIFFERENT_ARTIST);
				}
			    }

			    // SAME ALBUMS IF DEFINED
			    if (track.getAlbumName() == null
				    || (track.getAlbumName() != null && track.getAlbumName()
					    .isEmpty()))
			    {
				// no album field
				setValueAlbumQI(AlbumTagEnum.NO_ALBUM_FIELD_SET);
			    }
			    else if (bestAlbumName == null)
			    {
				// first iteration, set current album name to best one
				// initialize QI to : SOME ALBUMS & ALL ALBUMS
				bestAlbumName = track.getAlbumName();
				albumPattern = tagMatcher.getPattern(bestAlbumName);
			    }
			    else if (bestAlbumName != null)
			    {
				// next, check for DIFFERENT_ALBUMS
				if (!albumPattern.match(track.getAlbumName()))
				{
				    setValueAlbumQI(AlbumTagEnum.SOME_DIFFERENT_ALBUM);
				}
			    }

			}
			else if (album.getTagState().equals(TagEnum.SOME_DIFF_TAGS))
			{
			    // DIFF ARTISTS
			    if (track.getArtistName() == null
				    || (track.getArtistName() != null && track.getArtistName().isEmpty())
				    && getValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET) == null
				    && getValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET) == null	// !
				    && getValueArtistQI(ArtistTagEnum.SOME_ARTIST_FIELD_SET) == null)	// !
			    {
				// no artist field
				setValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET);
			    }
			    else if (bestArtistName == null && track.getArtistName() != null && !track.getArtistName().isEmpty())
			    {
				// first iteration, set current artist name to best one
				// initialize QI to : SOME ARTISTS & ALL ARTISTS
				bestArtistName = track.getArtistName();
				artistPattern = tagMatcher.getPattern(bestArtistName);
				setNullValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET);
				setValueArtistQI(ArtistTagEnum.SOME_ARTIST_FIELD_SET);
				setValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
			    }
			    else if (bestArtistName != null)
			    {
				// next, check for false ALL_ARTIST_FIELD
				if (track.getArtistName() == null
					|| (track.getArtistName() != null && track.getArtistName().isEmpty()))
				{
				    setNullValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
				}
				// check for DIFFERENT_ARTIST
				if (!artistPattern.match(track.getArtistName()))
				{
				    setValueArtistQI(ArtistTagEnum.SOME_DIFFERENT_ARTIST);
				    setValueAlbumQI(AlbumTagEnum.SOME_DIFFERENT_ARTIST);
				}
			    }
			    
			    // DIFF ALBUMS
			    if (track.getAlbumName() == null
				    || (track.getAlbumName() != null && track.getAlbumName().isEmpty())
				    && getValueAlbumQI(AlbumTagEnum.NO_ALBUM_FIELD_SET) == null
				    /*&& getValueAlbumQI(SOM) == null*/)	// !
			    {
				// no artist field
				setValueAlbumQI(AlbumTagEnum.NO_ALBUM_FIELD_SET);
				// TODO: SET BOOLEAN BOTTOM TO FALSE WHEN SOME ALBUMS SET
			    }
			    else if (bestAlbumName == null && track.getAlbumName() != null && !track.getAlbumName().isEmpty())
			    {
				// first iteration, set current artist name to best one
				// initialize QI to : SOME ARTISTS & ALL ARTISTS
				bestAlbumName = track.getAlbumName();
				albumPattern = tagMatcher.getPattern(bestAlbumName);
				setNullValueArtistQI(ArtistTagEnum.NO_ARTIST_FIELD_SET);
				setValueArtistQI(ArtistTagEnum.SOME_ARTIST_FIELD_SET);
				setValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
			    }
			    else if (bestAlbumName != null)
			    {
				// next, check for false ALL_ARTIST_FIELD
				if (track.getAlbumName() == null
					|| (track.getAlbumName() != null && track.getAlbumName().isEmpty()))
				{
				    setNullValueArtistQI(ArtistTagEnum.ALL_ARTIST_FIELD_SET);
				}
				// check for DIFFERENT_ARTIST
				if (!albumPattern.match(track.getAlbumName()))
				{
				    setValueAlbumQI(AlbumTagEnum.SOME_DIFFERENT_ARTIST);
				}
			    }
			}
		    }
		}

		// no album or artist access
		if (!album.getTagState().equals(TagEnum.ALL_SAME_TAGS))
		{
		    // Track validation

		}

	    }
	}

    }

    public void setValueArtistQI(ArtistTagEnum artistQI)
    {
	this.artistQI[artistQI.getValue()] = artistQI;
    }

    public void setNullValueArtistQI(ArtistTagEnum artistQI)
    {
	this.artistQI[artistQI.getValue()] = null;
    }

    public void setValueAlbumQI(AlbumTagEnum albumQI)
    {
	this.albumQI[albumQI.getValue()] = albumQI;
    }

    public void setValueTracksQI(TrackTagEnum tracksQI)
    {
	this.tracksQI[tracksQI.getValue()] = tracksQI;
    }

    public ArtistTagEnum getValueArtistQI(ArtistTagEnum artistQIvalue)
    {
	return this.artistQI[artistQIvalue.getValue()];
    }

    public AlbumTagEnum getValueAlbumQI(AlbumTagEnum albumQIvalue)
    {
	return this.albumQI[albumQIvalue.getValue()];
    }

    public TrackTagEnum getValueTracksQI(TrackTagEnum tracksQIvalue)
    {
	return this.tracksQI[tracksQIvalue.getValue()];
    }

    @Override
    public String getAlbumName()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getArtistName()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getTracksCount()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int[] getTracksLength()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String[] getTracksName()
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public int getArtistQI()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int getAlbumQI()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int getTracksQI()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    /* ------------------------- CONSTANTS --------------------------- */
    /** all words (regexp) defined as invalid in a track title. */
    private final static String[] TRACK_TITLE_INVALIDERS = Config.getConfig().getList(
								 Config.T_TRACK_TITLE_INVALIDERS);
}

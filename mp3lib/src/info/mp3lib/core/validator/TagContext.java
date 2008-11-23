package info.mp3lib.core.validator;

import java.util.Arrays;
import java.util.Iterator;

import info.mp3lib.config.Config;
import info.mp3lib.core.Album;
import info.mp3lib.core.TagEnum;
import info.mp3lib.core.Track;

/**
 * Denotes all data deduced from tags of an album and the quality index associated to these values
 * /!\ all new modifiers added must be added in the config.properties file too
 * @author do - Gab
 */
public class TagContext implements Context {

    	private Album album;
    	
    	private String albumName;
    	private String[] tracksName;
    
	/**
	 * Denotes the possible value modifiers for the quality index of the artist name deduction from the tag 
	 * context The final QI returned by the <code>getArtistQI()</code> method is a sum of all modifiers
	 */
	public enum ArtistTagEnum {
		/** The artist field is missing for all the tracks of this album */
		NO_ARTIST_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TAR_NO_ARTIST_FIELD_SET)),
		
		/** The artist field is set for some tracks of this album */
		SOME_ARTIST_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TAR_SOME_ARTIST_FIELD_SET)),

		/** 
		 * The artist field is set for all tracks of this album 
		 * this modifiers have a weak value because it overloads <code>SOME_ARTIST_FIELD_SET</code>
		 */
		ALL_ARTIST_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TAR_ALL_ARTIST_FIELD_SET)),
		
		/** The artist field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ARTIST(Config.getConfig()
				.getQIModifier(Config.TAR_SOME_DIFFERENT_ARTIST));

		private int value;

		private ArtistTagEnum(final int pValue) {
			value = pValue;
		}
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible value modifiers for the quality index of the album name deduction 
	 * from the tag context<br/>
	 * The final QI returned by the <code>getAlbumQI()</code> method is a sum of all modifiers
	 */
	public enum AlbumTagEnum {
		/** The album field is missing for all the tracks of this album */
		NO_ALBUM_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TAL_NO_ALBUM_FIELD_SET)),

		/** The album field is set for all the tracks of this album */
		ALL_TITLE_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TAL_ALL_TITLE_FIELD_SET)),
		
		/** The album field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ALBUM(Config.getConfig()
				.getQIModifier(Config.TAL_SOME_DIFFERENT_ALBUM)),
		
		/** The artist field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ARTIST(Config.getConfig()
				.getQIModifier(Config.TAL_SOME_DIFFERENT_ARTIST))
		;
		private int value;

		private AlbumTagEnum(final int pValue) {
			value = pValue;
		}
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible value modifiers for the quality index of the tracks name deduction 
	 * from the tag context<br/>
	 * The final QI returned by the <code>getTracksQI()</code> method is a sum of all modifiers
	 */
	public enum TrackTagEnum {
		/** The field title is missing for all the tracks of this album */
		NO_TITLE_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TTR_NO_TITLE_FIELD_SET)),
		
		/** The field title is set for all the tracks of this album */
		ALL_TITLE_FIELD_SET(Config.getConfig()
				.getQIModifier(Config.TTR_ALL_TITLE_FIELD_SET)),
		
		/** 
		 * The title field of all tracks contains the word defined as invalid in the program configuration
		 * ("track", "piste", , ...)
		 * this modifiers have a weak value because it overloads <code>REPEATING_SEQUENCE</code>
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig()
				.getQIModifier(Config.TTR_CONTAINS_INVALIDER_WORD)),
		
		/** 
		 * The title field of all tracks contains the same reapeating sequence wich is not include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE(Config.getConfig()
				.getQIModifier(Config.TTR_REPEATING_SEQUENCE)),
		
		/** 
		 * Once eliminated the common repeating sequence for the title field of all tracks, the remaining 
		 * variable sequence is short (less than 3 char) or only composed of decimal characters<br/>
		 * overloads <code>REPEATING_SEQUENCE</code>
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(Config.getConfig()
				.getQIModifier(Config.TTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE)),

		/** 
		 * Once eliminated the common repeating sequence for the title field of all tracks, the remaining 
		 * variable sequence is important (more than 2 char) and composed of alphadecimal (not only decimal)
		 * characters
		 */
		BIG_VARIABLE_SEQUENCE (Config.getConfig()
				.getQIModifier(Config.TTR_BIG_VARIABLE_SEQUENCE));


		private int value;

		private TrackTagEnum(final int pValue) {
			value = pValue;
		}

		public int getValue() {
			return value;
		}
	};

	/** artist name quality index modifiers */
	private ArtistTagEnum[] artistQI;

	/** album name quality index modifiers */
	private AlbumTagEnum[] albumQI;

	/** tracks name quality index modifiers */
	private TrackTagEnum[] tracksQI;

	public TagContext(Album album) {
	    // TODO: MAKE CONSTRUCTOR
	    this.album = album;
	    tracksName = new String[album.getSize()];
	    artistQI = new ArtistTagEnum[album.getSize()];
	    albumQI = new AlbumTagEnum[album.getSize()];
	    tracksQI = new TrackTagEnum[album.getSize()];
	}
	
	
	public void processAlbumContext() {
	    /**
	     * -> up if FIELDS are SET or SAME ... need modifiers
	     * 
	     * -> incrémentation impossible sur tablo d'enum ..
	     */
	    
	    
	    //TODO try to get best Album and Artist name ...
	    String bestAlbumName = null, bestArtistName = null; 
	    
	    if (album.getTagState().equals(TagEnum.ALL_SAME_TAGS)) {
		Track firstOne = album.getTrackIterator().next();
		bestAlbumName = firstOne.getAlbumName();
		bestArtistName = firstOne.getArtistName();
	    }
	    
	    else {
		// iterate ONCE
		boolean continueLoop = true;
		int i = 0;
		for (Iterator<Track> iTrack = album.getTrackIterator(); iTrack.hasNext() && continueLoop; i++) {
		    Track track = iTrack.next();
		    /** @set Track name */
		    tracksName[i] = track.getName();

		    /** @CHECK NO_TITLE_FIELD_SET */
		    if (album.getTagState().equals(TagEnum.NO_TAGS)) {
			/** @set all QI */
			tracksQI[i] = TrackTagEnum.NO_TITLE_FIELD_SET;
			albumQI[i] = AlbumTagEnum.NO_ALBUM_FIELD_SET;
			artistQI[i] = ArtistTagEnum.NO_ARTIST_FIELD_SET;
		    }
		    // TODO: Add to Album.add() ALL/SAME_ALBUM and ALL/SAME_ARTIST difference
		    else {
			/** @CHECK ALL_TITLE_FIELD_SET */
			if (album.getTagState().equals(TagEnum.ALL_SAME_TAGS)) { //ARTIST.
			    artistQI[i] = ArtistTagEnum.ALL_ARTIST_FIELD_SET;
			}
			if (album.getTagState().equals(TagEnum.ALL_SAME_TAGS)) { //ALBUM.
			    albumQI[i] = AlbumTagEnum.ALL_TITLE_FIELD_SET;
			}
			
		    }
		    
		    
		    
		    // no album or artist access
		    if (!album.getTagState().equals(TagEnum.ALL_SAME_TAGS)) {
			// Track validation
			
		    }
		    

		    
		}
	    }
	    
	}
	
	
	@Override
	public String getAlbumName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getArtistName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTracksCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getTracksLength() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTracksName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getArtistQI() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAlbumQI() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTracksQI() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/* ------------------------- CONSTANTS --------------------------- */
	/** all words (regexp) defined as invalid in a track title. */
	private final static String[] TRACK_TITLE_INVALIDERS = 
		Config.getConfig().getList(Config.T_TRACK_TITLE_INVALIDERS);
}

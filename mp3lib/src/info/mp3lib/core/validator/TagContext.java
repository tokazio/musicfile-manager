package info.mp3lib.core.validator;

import info.mp3lib.core.TagEnum;

/**
 * Denotes all data deduced from tags of an album and the quality index associated to these values
 * /!\ all new modifiers added must be added in the config.properties file too
 * @author do - Gab
 */
public class TagContext implements Context {

	/**
	 * Denotes the possible value modifiers for the quality index of the artist name deduction from the tag 
	 * context The final QI returned by the <code>getArtistQI()</code> method is a sum of all modifiers
	 */
	public enum ArtistTagEnum {
		/** The artist field is missing for all the tracks of this album */
		NO_ARTIST_FIELD_SET(-5),

		/** The artist field is set for all the tracks of this album */
		ALL_ARTIST_FIELD_SET(5),
		
		/** The artist field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ARTIST(-4);

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
		NO_ALBUM_FIELD_SET(-5),

		/** The album field is set for all the tracks of this album */
		ALL_TITLE_FIELD_SET(5),
		
		/** The album field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ALBUM(-4),
		
		/** The artist field is not the same for all the tracks of this album */
		SOME_DIFFERENT_ARTIST(-2)
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
		NO_TITLE_FIELD_SET(-5),
		
		/** The field title is set for all the tracks of this album */
		ALL_TITLE_FIELD_SET(2),
		
		/** 
		 * The title field of all tracks contains the word defined as invalid in the program configuration
		 * ("track", "piste", , ...)
		 * this modifiers have a weak value because it overloads <code>REPEATING_SEQUENCE</code>
		 */
		CONTAINS_INVALIDER_WORD(-1),
		
		/** 
		 * The title field of all tracks contains the same reapeating sequence wich is not include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE(-3),
		
		/** 
		 * Once eliminated the common repeating sequence for the title field of all tracks, the remaining 
		 * variable sequence is short (less than 3 char) or only composed of decimal characters<br/>
		 * overloads <code>REPEATING_SEQUENCE</code>
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(-2),

		/** 
		 * Once eliminated the common repeating sequence for the title field of all tracks, the remaining 
		 * variable sequence is important (more than 2 char) and composed of alphadecimal (not only decimal)
		 * characters
		 */
		BIG_VARIABLE_SEQUENCE (3);


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
	private TagEnum[] tracksQI;

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

}

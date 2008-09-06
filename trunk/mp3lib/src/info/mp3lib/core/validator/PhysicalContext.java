package info.mp3lib.core.validator;

import info.mp3lib.core.Album;


/**
 * Denotes all data deduced from the physical context of an album and the quality index associated to these values
 * /!\ all new modifiers added must be added in the config.properties file too
 * @author Gab
 */
public class PhysicalContext implements Context {

	/* ------------------------ QUALITY INDEX MODIFIERS ------------------------ */
	/**
	 * Denotes the possible value modifiers for the quality index of the artist name deduction 
	 * from the physical context<br/>
	 * The final QI returned by the <code>getArtistQI()</code> method is a sum of all modifiers
	 */
	public enum ArtistPhysicalEnum {
		/**
		 * Different artist tag field are present in tracks contained in directories at the same level
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(-3),
		
		/** 
		 * this parent directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", ...) 
		 */
		CONTAINS_INVALIDER_WORD(-3),
		
		/** 
		 * this parent directory name contains one of the word defined as quality index improver in the program 
		 * configuration ("discography", "full discography", ...)
		 */
		CONTAINS_VALIDER_WORD(3),
		
		/** 
		 * Some tracks contained in folders at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(4),
		
		/** 
		 * All directories at the same level (not including this) are tagged and artist field
		 * is the same for all<br/>
		 * this modifiers have a weak value because it overloads <code>OTHER_ALBUM_ARTIST_MATCH</code>
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST (1)
		;

		private int value;

		private ArtistPhysicalEnum(final int pValue) {
			value = pValue;
		}
		
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible value modifiers for the quality index of the album name deduction 
	 * from the physical context<br/>
	 * The final QI returned by the <code>getAlbumQI()</code> method is a sum of all modifiers
	 */
	public enum AlbumPhysicalEnum {
		/**  This directory contains at less another folder */
		NOT_LEAF(-1),
		
		/** 
		 * this directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", "discography", ...)  
		 */
		CONTAINS_INVALIDER_WORD(-3),
		
		/**
		 * Different artist tag field are present in tracks contained in directories at the same level
		 */
		SOME_DIFFERNT_ARTIST_IN_TREE(-1),
		
		/** 
		 * Some tracks contained in directories at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(1),
		
		/** 
		 * The first part of this directory name contains the parent directory name<br/>
		 * album name is the remaining sequence
		 */
		NAME_FIRST_PART_MATCH_PARENT(1),
		
		/** The first part of this directory name contains the artist tag field of same level directories */
		NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST(2),

		/** 
		 * All directories at the same level (including this) are leaf directories and contains 
		 * some music files
		 */
		ALL_ARTIST_TREE_WELL_FORMED (1),
		
		/** 
		 * All directories at the same level (not including this) are tagged and artist field
		 * is the same for all
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST (3)
		;

		private int value;

		private AlbumPhysicalEnum(final int pValue) {
			value = pValue;
		}
		
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible value modifiers for the quality index of the tracks name deduction 
	 * from the physical context<br/>
	 * The final QI returned by the <code>getTracksQI()</code> method is a sum of all modifiers
	 */
	public enum TrackPhysicalEnum {
		/**  The parent folder contains at less another folder */
		NOT_LEAF(-1),
		
		/** 
		 * The tracks names all contain the word defined as invalid in the program configuration
		 * ("track", "piste", , ...)
		 * this modifiers have a weak value because it overloads 
		 * <code>REPEATING_SEQUENCE_NOT_IN_FOLDERNAME</code>
		 */
		CONTAINS_INVALIDER_WORD(-1),
		
		/** 
		 * The tracks names all contain the same reapeating sequence wich is not include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_NOT_IN_FOLDERNAME (-1),
		
		/** 
		 * Once eliminated the common repeating sequence the variable sequence is short (less than 3 char)
		 * or only composed of decimal characters
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(-2),

		/** 
		 * Once eliminated the common repeating sequence all tracks have an important (more than 2 char)
		 * alphadecimal (not only decimal) remaining variable sequence
		 */
		BIG_VARIABLE_SEQUENCE (3),

		/** 
		 * The tracks names all contain the same reapeating sequence wich is include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_IN_FOLDERNAME (2),
		;

		private int value;

		private TrackPhysicalEnum(final int pValue) {
			value = pValue;
		}

		public int getValue() {
			return value;
		}
	};

	/* ------------------------ ATTRIBUTES ------------------------ */
	/** The album from which is built this Context */
	private Album album;
	
	/** artist name quality index modifiers */
	private ArtistPhysicalEnum[] artistQIModifiers;

	/** album name quality index modifiers */
	private AlbumPhysicalEnum[] albumQIModifiers;

	/** tracks name quality index modifiers */
	private TrackPhysicalEnum[] tracksQIModifiers;
	
	/* ----------------------- CONSTRUCTORS ----------------------- */
	public PhysicalContext(final Album pAlbum) {
		album = pAlbum;
		artistQIModifiers = new ArtistPhysicalEnum[5];
		albumQIModifiers = new AlbumPhysicalEnum[8];
		tracksQIModifiers = new TrackPhysicalEnum[6];
	}
	
	/* ------------------------- METHODS --------------------------- */
	@Override
	public int getAlbumQI() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getArtistQI() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTracksQI() {
		// TODO Auto-generated method stub
		return 0;
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


}

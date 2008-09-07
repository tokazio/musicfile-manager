package info.mp3lib.core.validator;

import info.mp3lib.config.Config;
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
	public static enum ArtistPhysicalEnum {
		/**
		 * Different artist tag field are present in tracks contained in directories at the same level
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getInstance()
				.getQIModifier(Config.PAR_SOME_DIFFERENT_ARTIST_IN_TREE)),
		
		/** 
		 * this parent directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", ...) 
		 */
		CONTAINS_INVALIDER_WORD(Config.getInstance()
				.getQIModifier(Config.PAR_CONTAINS_INVALIDER_WORD)),
		
		/** 
		 * this parent directory name contains one of the word defined as quality index improver in the program 
		 * configuration ("discography", "full discography", ...)
		 */
		CONTAINS_VALIDER_WORD(Config.getInstance()
				.getQIModifier(Config.PAR_CONTAINS_VALIDER_WORD)),
		
		/** 
		 * Some tracks contained in folders at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getInstance()
				.getQIModifier(Config.PAR_OTHER_ALBUM_ARTIST_MATCH)),
		
		/** 
		 * All directories at the same level (not including this) are tagged and artist field
		 * is the same for all<br/>
		 * this modifiers have a weak value because it overloads <code>OTHER_ALBUM_ARTIST_MATCH</code>
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST (Config.getInstance()
				.getQIModifier(Config.PAR_ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST))
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
	public static enum AlbumPhysicalEnum {
		/**  This directory contains at less another folder */
		NOT_LEAF(Config.getInstance().getQIModifier(Config.PAL_NOT_LEAF)),
		
		/** 
		 * this directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", "discography", ...)  
		 */
		CONTAINS_INVALIDER_WORD(Config.getInstance()
				.getQIModifier(Config.PAL_CONTAINS_INVALIDER_WORD)),
		
		/**
		 * Different artist tag field are present in tracks contained in directories at the same level
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getInstance()
				.getQIModifier(Config.PAL_SOME_DIFFERENT_ARTIST_IN_TREE)),
		
		/** 
		 * Some tracks contained in directories at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getInstance()
				.getQIModifier(Config.PAL_OTHER_ALBUM_ARTIST_MATCH)),
		
		/** 
		 * The first part of this directory name contains the parent directory name<br/>
		 * album name is the remaining sequence
		 */
		NAME_FIRST_PART_MATCH_PARENT(Config.getInstance()
				.getQIModifier(Config.PAL_NAME_FIRST_PART_MATCH_PARENT)),
		
		/** The first part of this directory name contains the artist tag field of same level directories */
		NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST(Config.getInstance()
				.getQIModifier(Config.PAL_NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST)),

		/** 
		 * All directories at the same level (including this) are leaf directories and contains 
		 * some music files
		 */
		ALL_ARTIST_TREE_WELL_FORMED (Config.getInstance()
				.getQIModifier(Config.PAL_ALL_ARTIST_TREE_WELL_FORMED)),
		
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
	public static enum TrackPhysicalEnum {
		/**  The parent folder contains at less another folder */
		NOT_LEAF(-1),
		
		/** 
		 * The tracks names all contain the word defined as invalid in the program configuration
		 * ("track", "piste", , ...)
		 * this modifiers have a weak value because it overloads 
		 * <code>REPEATING_SEQUENCE_NOT_IN_FOLDERNAME</code>
		 */
		CONTAINS_INVALIDER_WORD(Config.getInstance()
				.getQIModifier(Config.PTR_CONTAINS_INVALIDER_WORD)),
		
		/** 
		 * The tracks names all contain the same reapeating sequence wich is not include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_NOT_IN_FOLDERNAME (Config.getInstance()
				.getQIModifier(Config.PTR_REPEATING_SEQUENCE_IN_FOLDERNAME)),
		
		/** 
		 * Once eliminated the common repeating sequence the variable sequence is short (less than 3 char)
		 * or only composed of decimal characters
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(Config.getInstance()
				.getQIModifier(Config.PTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE)),

		/** 
		 * Once eliminated the common repeating sequence all tracks have an important (more than 2 char)
		 * alphadecimal (not only decimal) remaining variable sequence
		 */
		BIG_VARIABLE_SEQUENCE (Config.getInstance()
				.getQIModifier(Config.PTR_BIG_VARIABLE_SEQUENCE)),

		/** 
		 * The tracks names all contain the same reapeating sequence wich is include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_IN_FOLDERNAME (Config.getInstance()
				.getQIModifier(Config.PTR_REPEATING_SEQUENCE_IN_FOLDERNAME)),
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

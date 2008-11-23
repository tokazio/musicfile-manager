package info.mp3lib.core.validator;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import info.mp3lib.config.Config;
import info.mp3lib.core.Album;
import info.mp3lib.core.Library;
import info.mp3lib.core.TagEnum;
import info.mp3lib.core.Track;
import info.mp3lib.core.validator.cache.CachedContextBean;
import info.mp3lib.core.validator.cache.ContextCache;
import info.mp3lib.util.string.MatcherConfig;
import info.mp3lib.util.string.MatcherFactory;
import info.mp3lib.util.string.StringMatcher;
import info.mp3lib.util.string.StringPattern;

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
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getConfig().getQIModifier(
				Config.PAR_SOME_DIFFERENT_ARTIST_IN_TREE)),

		/** 
		 * this parent directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", ...) 
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(
				Config.PAR_CONTAINS_INVALIDER_WORD)),

		/** 
		 * this parent directory name contains one of the word defined as quality index improver in the program 
		 * configuration ("discography", "full discography", ...)
		 */
		CONTAINS_VALIDER_WORD(Config.getConfig().getQIModifier(
				Config.PAR_CONTAINS_VALIDER_WORD)),

		/** 
		 * Some tracks contained in folders at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getConfig().getQIModifier(
				Config.PAR_OTHER_ALBUM_ARTIST_MATCH)),

		/** 
		 * All directories at the same level (not including this) are tagged and artist field
		 * is the same for all<br/>
		 * this modifiers have a weak value because it overloads <code>OTHER_ALBUM_ARTIST_MATCH</code>
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST(Config.getConfig()
				.getQIModifier(
						Config.PAR_ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST));

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
		NOT_LEAF(Config.getConfig().getQIModifier(Config.PAL_NOT_LEAF)),

		/** 
		 * this directory name contains one of the word defined as invalid in the program configuration
		 * ("compilation", "discography", ...)  
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(
				Config.PAL_CONTAINS_INVALIDER_WORD)),

		/**
		 * Different artist tag field are present in tracks contained in directories at the same level
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getConfig().getQIModifier(
				Config.PAL_SOME_DIFFERENT_ARTIST_IN_TREE)),

		/** 
		 * Some tracks contained in directories at the same level have the artist tag field matching
		 * the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getConfig().getQIModifier(
				Config.PAL_OTHER_ALBUM_ARTIST_MATCH)),

		/** 
		 * The first part of this directory name contains the parent directory name<br/>
		 * album name is the remaining sequence
		 */
		NAME_FIRST_PART_MATCH_PARENT(Config.getConfig().getQIModifier(
				Config.PAL_NAME_FIRST_PART_MATCH_PARENT)),

		/** The first part of this directory name contains the artist tag field of same level directories */
		NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST(Config.getConfig()
				.getQIModifier(
						Config.PAL_NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST)),

		/** 
		 * All directories at the same level (including this) are leaf directories and contains 
		 * some music files
		 */
		ALL_ARTIST_TREE_WELL_FORMED(Config.getConfig().getQIModifier(
				Config.PAL_ALL_ARTIST_TREE_WELL_FORMED)),

		/** 
		 * All directories at the same level (not including this) are tagged and artist field
		 * is the same for all
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST(3);

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
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(
				Config.PTR_CONTAINS_INVALIDER_WORD)),

		/** 
		 * The tracks names all contain the same reapeating sequence wich is not include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_NOT_IN_FOLDERNAME(Config.getConfig()
				.getQIModifier(Config.PTR_REPEATING_SEQUENCE_IN_FOLDERNAME)),

		/** 
		 * Once eliminated the common repeating sequence the variable sequence is short (less than 3 char)
		 * or only composed of decimal characters
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(
				Config.PTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE)),

		/** 
		 * Once eliminated the common repeating sequence all tracks have an important (more than 2 char)
		 * alphadecimal (not only decimal) remaining variable sequence
		 */
		BIG_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(
				Config.PTR_BIG_VARIABLE_SEQUENCE)),

		/** 
		 * The tracks names all contain the same reapeating sequence wich is include in 
		 * the parent folders name (2 level up, 3 if the parent folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_IN_FOLDERNAME(Config.getConfig().getQIModifier(
				Config.PTR_REPEATING_SEQUENCE_IN_FOLDERNAME)), ;

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

	/** the album name deduced from the context */
	private String albumName;

	/** the artist name deduced from the context */
	private String artistName;

	/** the tracks name name deduced from the context */
	private String[] tracksName;

	/** artist name quality index modifiers */
	private ArtistPhysicalEnum[] artistQIModifiers;

	/** album name quality index modifiers */
	private AlbumPhysicalEnum[] albumQIModifiers;

	/** tracks name quality index modifiers */
	private TrackPhysicalEnum[] tracksQIModifiers;
	
	/** all artist tag of all tracks contained in folders at the same level */
	private String[] artistNames;
	
	/** the instance of matcher used for comparison */ 
	private StringMatcher matcher;

	/* ----------------------- CONSTRUCTORS ----------------------- */
	public PhysicalContext(final Album pAlbum) {
		album = pAlbum;
		albumName = album.getName();
		artistName = album.getFile().getParentFile().getName();
		tracksName = album.getTracksName();
		artistQIModifiers = new ArtistPhysicalEnum[5];
		albumQIModifiers = new AlbumPhysicalEnum[8];
		tracksQIModifiers = new TrackPhysicalEnum[6];
		matcher = MatcherFactory.getInstance().getMatcher(MatcherConfig.FILE);
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * Retrieves all artist tag of all tracks contained in folders at the same level<br/>
	 * Retrieves it and the associated modifiers from cache if one of the album located at this folder level 
	 * have been already computed, else look for its in the <code>Album</code> objects , computes the 
	 * modifiers and caches the result for future usage.
	 * Checks if the given artist name contains words defined as validers or invaliders
	 */
	private void processArtistContext() {
		final String parentFolderPath = album.getFile().getParentFile().getPath();
		final ContextCache cache = ContextCache.getInstance();
		CachedContextBean context = null;
		final StringPattern artistNamePattern = matcher.getPattern(album.getArtistName());
		int otherMatchCount = 0;
		boolean someDifferent = false;
		// if the artist context have been already computed and cached
		if ((context = cache.get(parentFolderPath)) != null) {
			// retrieve data from cache
			artistNames = context.getArtistNames();
			artistQIModifiers = context.getArtistQIModifiers();
		} else {
			// retrieves all albums located at the same folder level than the current one 
			// and compute all modifiers
			final List<String> artistTagNameList = new LinkedList<String>();
			final Album[] albums = Library.getInstance().getAlbumsLocatedIn(
					parentFolderPath);
			
			for (int i = 0; i < albums.length; i++) {
				Iterator<Track> trackIt;
				// if all track tagged with same artist name
				if (albums[i].getTagState() == TagEnum.ALL_SAME_TAGS) {
					// retrieve the artist name of the first track
					artistTagNameList.add(albums[i].getFirstTrack()
							.getArtistName());
				} else {
					// retrieve the artist name off all tracks
					trackIt  = albums[i].getTrackIterator();
					String artistName;
					Track track;
					while (trackIt.hasNext()) {
						track =  trackIt.next();
						artistName = track.getArtistName();
						artistTagNameList.add(artistName);
						// modify the modifiers according to the current artist name
						if (track.isTagged()) {
							if (artistNamePattern.match(track.getArtistName())) {
								otherMatchCount ++;
							} else {
								someDifferent = true;
							}
						}
					}
				}
			}
			if (otherMatchCount == artistTagNameList.size()) {
				artistQIModifiers[0] = ArtistPhysicalEnum.ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST;
			} else {
				if (someDifferent) {
					artistQIModifiers[4] = ArtistPhysicalEnum.SOME_DIFFERENT_ARTIST_IN_TREE;
				}
				if (otherMatchCount > 0) {
					artistQIModifiers[3] = ArtistPhysicalEnum.OTHER_ALBUM_ARTIST_MATCH;
				}
			}
			artistNames = artistTagNameList
					.toArray(new String[artistTagNameList.size()]);
		}
		// store current data in cache
		context.setArtistQIModifiers(artistQIModifiers);
		context.setArtistNames(artistNames);
		cache.put(parentFolderPath, context);
		
		// Checks if the given artist name contains words defined as validers or invaliders
		if (Config.getConfig().getListAsPattern(Config.P_ARTIST_NAME_INVALIDERS)
				.matcher(artistName).matches()) {
			artistQIModifiers[1] = ArtistPhysicalEnum.CONTAINS_INVALIDER_WORD;
		}
		if (Config.getConfig().getListAsPattern(Config.P_ARTIST_NAME_VALIDERS)
				.matcher(artistName).matches()) {
			artistQIModifiers[1] = ArtistPhysicalEnum.CONTAINS_VALIDER_WORD;
		}
	}
	
	private void processAlbumContext() {
		
	}
	
	private void processTrackContext() {
		
	}

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

	/* ------------------------- CONSTANTS --------------------------- */
	/** all words (regexp) defined as invalid in a track title. */
	private final static String[] TRACK_TITLE_INVALIDERS = Config.getConfig()
			.getList(Config.P_TRACK_TITLE_INVALIDERS);

	/** all words (regexp) defined as invalid in an artist name. */
	private final static String[] ARTIST_NAME_INVALIDERS = Config.getConfig()
			.getList(Config.P_ARTIST_NAME_INVALIDERS);

	/** all words (regexp) that prove that an artist name must be the valid one. */
	private final static String[] ARTIST_NAME_VALIDERS = Config.getConfig()
			.getList(Config.P_ARTIST_NAME_VALIDERS);

	/** all words (regexp) that prove that an album name must the a valid one. */
	private final static String[] ALBUM_NAME_INVALIDERS = Config.getConfig()
			.getList(Config.P_ALBUM_NAME_INVALIDERS);
}

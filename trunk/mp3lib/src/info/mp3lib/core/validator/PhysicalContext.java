package info.mp3lib.core.validator;

import info.mp3lib.config.Config;
import info.mp3lib.core.Album;
import info.mp3lib.core.Library;
import info.mp3lib.core.TagEnum;
import info.mp3lib.core.Track;
import info.mp3lib.util.string.MatcherConfig;
import info.mp3lib.util.string.MatcherFactory;
import info.mp3lib.util.string.StringMatcher;
import info.mp3lib.util.string.CompiledStringMatcher;
import info.mp3lib.util.string.StringUtils;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Denotes all data deduced from the physical context of an album and the
 * quality index associated to these values /!\ all new modifiers added must be
 * added in the config.properties file too
 * 
 * @author Gab
 */
/*
 * NOTES: => ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST - impossible de ne
 * considerer l'album courant comme les autres sinon le fait qu'il ne soit pas
 * tagge l'handicape à mort. (pas le role de physical context) TODO reflechir a
 * une solution pr permettre a une minorité d'album de ce contexte (mais
 * plusieurs) de ne pas être taggés et de qd mm profiter du bonus (pas
 * prioritaire)
 * Fixé par la supression du systeme de cache ! etudier la possibilité de le retablir
 * (a mon avis plutot complexe, penser a supprimer le pkg cache sinon)
 */
public class PhysicalContext implements Context {

	/* ------------------------ QUALITY INDEX MODIFIERS ------------------------ */
	/**
	 * Denotes the possible value modifiers for the quality index of the artist
	 * name deduction from the physical context<br/>
	 * The final QI returned by the <code>getArtistQI()</code> method is a sum
	 * of all modifiers
	 */
	public static enum ArtistPhysicalEnum {
		/**
		 * Different artist tag field are present in tracks contained in
		 * directories at the same level (including this)
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getConfig().getQIModifier(
				Config.PAR_SOME_DIFFERENT_ARTIST_IN_TREE)),

		/**
		 * this parent directory name contains one of the word defined as
		 * invalid in the program configuration ("compilation", ...)
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(Config.PAR_CONTAINS_INVALIDER_WORD)),

		/**
		 * this parent directory name contains one of the word defined as
		 * quality index improver in the program configuration ("discography",
		 * "full discography", ...)
		 */
		CONTAINS_VALIDER_WORD(Config.getConfig().getQIModifier(Config.PAR_CONTAINS_VALIDER_WORD)),

		/**
		 * Some tracks contained in folders at the same level (including this)
		 * have the artist tag field matching the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getConfig().getQIModifier(Config.PAR_OTHER_ALBUM_ARTIST_MATCH)),

		/**
		 * All directories at the same level are tagged (the current can not be)
		 * and artist field is the same for all<br/>
		 * this modifiers have a weak value because it overloads
		 * <code>OTHER_ALBUM_ARTIST_MATCH</code>
		 */
		ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST(Config.getConfig().getQIModifier(
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
	 * Denotes the possible value modifiers for the quality index of the album
	 * name deduction from the physical context<br/>
	 * The final QI returned by the <code>getAlbumQI()</code> method is a sum of
	 * all modifiers
	 */
	public static enum AlbumPhysicalEnum {
		/** This directory contains at less another folder */
		NOT_LEAF(Config.getConfig().getQIModifier(Config.PAL_NOT_LEAF)),

		/**
		 * this directory name contains one of the word defined as invalid in
		 * the program configuration ("compilation", "discography", ...)
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(Config.PAL_CONTAINS_INVALIDER_WORD)),

		/**
		 * Different artist tag field are present in tracks contained in
		 * directories at the same level
		 */
		SOME_DIFFERENT_ARTIST_IN_TREE(Config.getConfig().getQIModifier(
				Config.PAL_SOME_DIFFERENT_ARTIST_IN_TREE)),

		/**
		 * Some tracks contained in directories at the same level have the
		 * artist tag field matching the parent directory name
		 */
		OTHER_ALBUM_ARTIST_MATCH(Config.getConfig().getQIModifier(Config.PAL_OTHER_ALBUM_ARTIST_MATCH)),

		/**
		 * The first part of this directory name contains the parent directory name<br/>
		 * album name is the remaining sequence
		 */
		NAME_FIRST_PART_MATCH_PARENT(Config.getConfig()
				.getQIModifier(Config.PAL_NAME_FIRST_PART_MATCH_PARENT)),

		/**
		 * The first part of this directory name contains the artist tag field contained in some
		 * of same level directories
		 */
		NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST(Config.getConfig().getQIModifier(
				Config.PAL_NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST)),

		/**
		 * All directories at the same level (including this) are leaf
		 * directories and contains some music files
		 */
		ALL_ARTIST_TREE_WELL_FORMED(Config.getConfig().getQIModifier(Config.PAL_ALL_ARTIST_TREE_WELL_FORMED)),

		/**
		 * All directories at the same level are tagged (the current can not be)
		 * and artist field is the same for all
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
	 * Denotes the possible value modifiers for the quality index of the tracks
	 * name deduction from the physical context<br/>
	 * The final QI returned by the <code>getTracksQI()</code> method is a sum
	 * of all modifiers
	 */
	public static enum TrackPhysicalEnum {
		/** The parent folder contains at less another folder */
		NOT_LEAF(-1),

		/**
		 * The tracks names all contain the word defined as invalid in the
		 * program configuration ("track", "piste", , ...) this modifiers have a
		 * weak value because it overloads
		 * <code>REPEATING_SEQUENCE_NOT_IN_FOLDERNAME</code>
		 */
		CONTAINS_INVALIDER_WORD(Config.getConfig().getQIModifier(Config.PTR_CONTAINS_INVALIDER_WORD)),

		/**
		 * The tracks names all contain the same reapeating sequence wich is not
		 * include in the parent folders name (2 level up, 3 if the parent
		 * folder contain "CD.*\d" regex)
		 */
		REPEATING_SEQUENCE_NOT_IN_FOLDERNAME(Config.getConfig().getQIModifier(
				Config.PTR_REPEATING_SEQUENCE_IN_FOLDERNAME)),

		/**
		 * Once eliminated the common repeating sequence the variable sequence
		 * is short (less than 3 char) or only composed of decimal characters
		 */
		NO_ALPHADECIMAL_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(
				Config.PTR_NO_ALPHADECIMAL_VARIABLE_SEQUENCE)),

		/**
		 * Once eliminated the common repeating sequence all tracks have an
		 * important (more than 2 char) alphadecimal (not only decimal)
		 * remaining variable sequence
		 */
		BIG_VARIABLE_SEQUENCE(Config.getConfig().getQIModifier(Config.PTR_BIG_VARIABLE_SEQUENCE)),

		/**
		 * The tracks names all contain the same reapeating sequence wich is
		 * include in the parent folders name (2 level up, 3 if the parent
		 * folder contain "CD.*\d" regex)
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
	private final StringMatcher equalMatcher;
	
	/** the instance of matcher used to check if one String contains another */
	private final StringMatcher containMatcher;

	/* ----------------------- CONSTRUCTORS ----------------------- */
	public PhysicalContext(final Album pAlbum) {
		album = pAlbum;
		albumName = album.getName();
		artistName = album.getFile().getParentFile().getName();
		tracksName = album.getTracksName();
		artistQIModifiers = new ArtistPhysicalEnum[5];
		albumQIModifiers = new AlbumPhysicalEnum[8];
		tracksQIModifiers = new TrackPhysicalEnum[6];
		equalMatcher = MatcherFactory.getInstance().getMatcher(MatcherConfig.FILE);
		containMatcher = MatcherFactory.getInstance().getMatcher(MatcherConfig.FILE); // TODO create a specific matcher instance in configuration
		processArtistContext();
		processAlbumContext();
		processTrackContext();
	}

	/* ------------------------- METHODS --------------------------- */
	/**
	 * Computes the artist context for the current album. Retrieves all artist
	 * tag of all tagged tracks contained in folders at the same level<br/>
	 * Checks if the given artist name contains words defined as validers or
	 * invaliders
	 */
	private void processArtistContext() {
		final String parentFolderPath = album.getFile().getParentFile().getPath();
		final CompiledStringMatcher artistNameMatcher = equalMatcher.getCompiledMatcher(album.getArtistName());
		int otherMatchCount = 0;
		int totalCheckedTracks = 0;
		boolean someDifferent = false;
		final Album[] albums = Library.getInstance().getAlbumsLocatedIn(parentFolderPath);
		Track track;
		final List<String> artistTagNameList = new LinkedList<String>();
		for (int i = 0; i < albums.length; i++) {
			Iterator<Track> trackIt;
			String artistName;
			// excluding not tagged albums
			if (album.getTagState() != TagEnum.NO_TAGS) {
				// if all track tagged with same artist name
				if (albums[i].getTagState() == TagEnum.ALL_SAME_TAGS) {
					// retrieve the artist name of the first track
					track = albums[i].getFirstTrack();
					artistName = track.getTag().getFirstArtist();
					totalCheckedTracks++;
					artistTagNameList.add(artistName);
					// modify the modifiers according to the current artist name
					if (track.isTagged()) {
						if (artistNameMatcher.match(artistName)) {
							otherMatchCount++;
						} else {
							someDifferent = true;
						}
					}
				} else { // if the album[i] is partially tagged
					// retrieve the artist name off all tracks
					trackIt = albums[i].getTrackIterator();
					while (trackIt.hasNext()) {
						track = trackIt.next();
						artistName = track.getTag().getFirstArtist();
						totalCheckedTracks++;
						artistTagNameList.add(artistName);
						// modify the modifiers according to the current artist
						// name
						if (track.isTagged()) {
							if (artistNameMatcher.match(artistName)) {
								otherMatchCount++;
							} else {
								someDifferent = true;
							}
						}
					}
				}
			}
		}
		if (otherMatchCount == totalCheckedTracks) {
			artistQIModifiers[4] = ArtistPhysicalEnum.ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST;
		} else {
			if (someDifferent) {
				artistQIModifiers[0] = ArtistPhysicalEnum.SOME_DIFFERENT_ARTIST_IN_TREE;
			}
			if (otherMatchCount > 0) {
				artistQIModifiers[3] = ArtistPhysicalEnum.OTHER_ALBUM_ARTIST_MATCH;
			}
		}
		artistNames = artistTagNameList.toArray(new String[artistTagNameList.size()]);

		// Checks if the given artist name contains words defined as validers or
		// invaliders
		if (Config.getConfig().getListAsPattern(Config.P_ARTIST_NAME_INVALIDERS).matcher(artistName)
				.matches()) {
			artistQIModifiers[1] = ArtistPhysicalEnum.CONTAINS_INVALIDER_WORD;
		}
		if (Config.getConfig().getListAsPattern(Config.P_ARTIST_NAME_VALIDERS).matcher(artistName).matches()) {
			artistQIModifiers[2] = ArtistPhysicalEnum.CONTAINS_VALIDER_WORD;
		}
	}

	/**
	 * Computes the album context for the current album.
	 * Checks if the given artist name contains words defined as validers or invaliders.
	 * Retrieves modifiers pre-computed by processArtistContext method.
	 * @requirements : processArtistContext must be executed before
	 */
	private void processAlbumContext() {
		// retrieves modifiers pre_computed by processArtistContext method
		if (artistQIModifiers[0] != null) {
			albumQIModifiers[2] = AlbumPhysicalEnum.SOME_DIFFERENT_ARTIST_IN_TREE;
		}
		if (artistQIModifiers[3] != null) {
			albumQIModifiers[3] = AlbumPhysicalEnum.OTHER_ALBUM_ARTIST_MATCH;
		}
		if (artistQIModifiers[4] != null) {
			albumQIModifiers[7] = AlbumPhysicalEnum.ALL_ARTIST_TREE_TAGGED_WITH_SAME_ARTIST;
		}
		// Checks if the given artist name contains words defined as invaliders
		if (Config.getConfig().getListAsPattern(Config.P_ALBUM_NAME_INVALIDERS).matcher(albumName).matches()) {
			albumQIModifiers[1] = AlbumPhysicalEnum.CONTAINS_INVALIDER_WORD;
		}
		// check if the current album folder is a leaf of the file system tree
		final boolean leaf = isLeaf(album.getFile());
		if (!leaf) {
			albumQIModifiers[0] = AlbumPhysicalEnum.NOT_LEAF;
		} else {
			final File[] files = album.getFile().getParentFile().listFiles();
			// All directories at the same level (including this) are leaf directories and contains some music files
			boolean allTreeWellFormed = true;
			int j = 0;
			while (allTreeWellFormed && j < files.length) {
				if (files[j].isFile()) {
					allTreeWellFormed = isWellFormed(files[j]);
				}
				j++;
			}
			if (allTreeWellFormed) {
				albumQIModifiers[6] = AlbumPhysicalEnum.ALL_ARTIST_TREE_WELL_FORMED;
			}
		}
		// The first part of this directory name contains the parent directory name
		final CompiledStringMatcher artistNamePattern = containMatcher.getCompiledMatcher(album.getArtistName());
		if (artistNamePattern.match(album.getParentPath())) {
			albumQIModifiers[4] = AlbumPhysicalEnum.NAME_FIRST_PART_MATCH_PARENT;
		}
		// The first part of this directory name contains the artist tag field contained in some of same level directories
		boolean contained = false;
		int i = 0;
		while (!contained && i < artistNames.length) {
			contained = artistNamePattern.match(artistNames[i]);
			i++;
		}
		if (contained) {
			albumQIModifiers[5] = AlbumPhysicalEnum.NAME_FIRST_PART_MATCH_OTHER_ALBUM_ARTIST;
		}
	}
	
	/**
	 * Computes the tracks context for the current album.
	 * Checks if the tracks names contain words defined as validers or invaliders or have a common pattern
	 * (track01, track02, etc.).
	 * Retrieves modifiers pre-computed by processAlbumContext method.
	 * @requirements : processAlbumContext must be executed before
	 */
	private void processTrackContext() {
		// retrieves modifiers pre_computed by processAlbumContext method
		if (albumQIModifiers[0] != null) {
			tracksQIModifiers[0] = TrackPhysicalEnum.NOT_LEAF;
		}
		// Checks if the given artist name contains words defined as invaliders
		if (Config.getConfig().getListAsPattern(Config.P_TRACK_TITLE_INVALIDERS).matcher(albumName).matches()) {
			tracksQIModifiers[1] = TrackPhysicalEnum.CONTAINS_INVALIDER_WORD;
		}
		final String lcs = StringUtils.getLCSubstring(albumName, "TOTO"); // TODO
		StringBuilder sb = new StringBuilder();
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
	
	/* --------------------- CONVENIENT INNER METHODS------------------- */
	/**
	 * @param directory a file pointer denoting a directory
	 * @return true if the given folder is a leaf of the file system tree
	 */
	private boolean isLeaf(final File directory) {
		boolean leaf = true;
		if (directory.isFile()) {
			final File[] files = directory.listFiles();
			int j = 0;
			while (leaf && j < files.length) {
				if (files[j].isDirectory()) {
					leaf = false;
				}
				j++;
			}
		} else {
			throw new IllegalArgumentException("Given File must denotes a directory");
		}
		return leaf;
	}
	
	/**
	 * Checks if the given directory denotes a well-formed album
	 * @param directory a file pointer denoting a directory
	 * @return true if the given folder is a leaf and contains some supported music files
	 */
	private boolean isWellFormed(final File directory) {
		boolean leaf = true;
		boolean containsValidFile = false;
		if (directory.isFile()) {
			final File[] files = directory.listFiles();
			int j = 0;
			while (leaf && j < files.length) {
				if (files[j].isDirectory()) {
					leaf = false;
				}
				if (!containsValidFile && Config.getConfig().isSupported(files[j].getName())) {
					containsValidFile = false;
				}
				j++;
			}
		} else {
			throw new IllegalArgumentException("Given File must denotes a directory");
		}
		return leaf && containsValidFile;
	}

}
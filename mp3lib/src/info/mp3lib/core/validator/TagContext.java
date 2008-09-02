package info.mp3lib.core.validator;

import info.mp3lib.core.TagEnum;

public class TagContext implements Context {

	/**
	 * Denotes the possible values for the quality index of the artist name deduction from the tag context
	 */
	public enum ArtistTagEnum {
		NO_TAGS(0); // no artist is tagged

		private int value;

		private ArtistTagEnum(final int pValue) {
			value = pValue;
		}
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible values for the quality index of the album name deduction from the tag context
	 */
	public enum AlbumTagEnum {
		NO_TAGS(0); // no album is tagged

		private int value;

		private AlbumTagEnum(final int pValue) {
			value = pValue;
		}
		public int getValue() {
			return value;
		}
	};

	/**
	 * Denotes the possible values for the quality index of the tracks name deduction from the tag context
	 */
	public enum TrackTagEnum {
		NO_TAGS(0); // no track is tagged

		private int value;

		private TrackTagEnum(final int pValue) {
			value = pValue;
		}

		public int getValue() {
			return value;
		}
	};

	/** artist name quality index */
	private ArtistTagEnum artistQI;

	/** album name quality index */
	private AlbumTagEnum albumQI;

	/** tracks name quality index */
	private TagEnum tracksQI;

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
		return artistQI.value;
	}

	@Override
	public int getAlbumQI() {
		return albumQI.value;
	}

	@Override
	public int getTracksQI() {
		return tracksQI.getValue();
	}

}

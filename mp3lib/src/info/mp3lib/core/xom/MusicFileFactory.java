package info.mp3lib.core.xom;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

public class MusicFileFactory {

	public ITaggedMusicFile readMusicFile(final File fileArg, final List<File> unprocessable) {
		if (fileArg.isFile()) {
			try {
				return new Track(fileArg);
			} catch (InvalidParameterException e) {
				// NOT A MUSIC FILE
			}

		} else if (fileArg.exists()) {// fileArg is directory
			// all ITaggedMusicFile instance built with the content files of this directory 
			final List<ITaggedMusicFile>  contentList = new LinkedList<ITaggedMusicFile>();
			int cptTrack = 0;
			int cptArtist = 0;
			int cptAlbum = 0;
			int cptNull = 0;

			File[] listFiles = fileArg.listFiles();
			// retrieves all objects contained by this File
			for (File contentFile : listFiles) {
				ITaggedMusicFile result = readMusicFile(contentFile, unprocessable);
				if (result == null) {
					cptNull ++;
				} else {
					if (result instanceof Track) {
						cptTrack ++;
					} else if (result instanceof Album) {
						cptAlbum ++;
					} else if (result instanceof Artist) {
						cptArtist ++;
					}
					contentList.add(result);
				}
			}
			if (cptNull == 0) {
				if (cptTrack != 0) {
					if (cptArtist == 0) {
						if (cptAlbum == 0) {
							// if all contained object are Tracks creates an album and return it
							Album album = new Album(fileArg, contentList.toArray(new ITaggedMusicFile[contentList.size()]));
							setAlbumValue(album);
							return album;
						} else {
							// split album and tracks in two separated list
							final List<Track>  trackList = new LinkedList<Track>();
							final List<Album>  albumList = new LinkedList<Album>();
							for (ITaggedMusicFile taggedMusicFile : contentList) {
								if (taggedMusicFile instanceof Track) {
									trackList.add((Track)taggedMusicFile);
								} else {
									albumList.add((Album)taggedMusicFile);
								}
							}
							// create an album with all the track contained in this directory 
							// and add it to the the current album list
							final File albumDirectory = new File("#NULL#");
							Album album = new Album(albumDirectory, trackList.toArray(new ITaggedMusicFile[trackList.size()]));
							setAlbumValue(album);
							albumList.add(album);
							// create an artist with all contained album (including the new one) and return it
							Artist artist = new Artist(fileArg, albumList.toArray(new ITaggedMusicFile[albumList.size()]));
							setArtistValue(artist);
							return  artist;
						}

					}
				} else if (cptArtist == 0 && cptAlbum !=0) {
					Artist artist = new Artist(fileArg, contentList.toArray(new ITaggedMusicFile[contentList.size()]));
					setArtistValue(artist);
					// if all contained object are Album instances creates an Artist and return it
					return artist;
				}
			} else {
				if (cptArtist == 0 && cptAlbum == 0 && cptTrack == 0) {
					// no valid music files detected in 2 sub level
					return null;
				}
			}
		}
		return null; //TODO remove when all cases processed
	}

	/**
	 * Sets a value for album attribute for all the Track of given Album.
	 * @param album the album whose tracks will be processed
	 */
	private void setAlbumValue(Album album) {
		String albumName = album.getName();
		int length = album.getLength();
		for (int i = 0; i < length; i++) {
			((Track)album.getItem(i)).setAlbum(albumName);
		} 
	}
	
	/**
	 * Sets a value for artist attribute for all the Album and Track of given Artist.
	 * @param artist the Artist whose albums and tracks will be processed
	 */
	private void setArtistValue(Artist artist) {
		String artistName = artist.getName();
		int length = artist.getLength();
		for (int i = 0; i < length; i++) {
			final Album album = (Album)artist.getItem(i);
			album.setArtist(artistName);
			int albumLength = album.getLength();
			for (int j = 0; j < albumLength; j++) {
				((Track)album.getItem(j)).setArtist(artistName);
			}
		} 
	}
	
}

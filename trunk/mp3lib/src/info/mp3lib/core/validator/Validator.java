package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.core.IMusicFile;
import info.mp3lib.core.Track;
import info.mp3lib.util.cddb.CDDBquery;

import java.util.Iterator;
import java.util.List;

import entagged.audioformats.Tag;
import entagged.freedb.FreedbReadResult;

public class Validator {

	IMusicFile mf = null;
	FreedbReadResult[] dbResult = null;
	Tag tag = null;
	
	final private static int CDDB_ARTIST	= 0;
	final private static int CDDB_ALBUM		= 1;
	final private static int CDDB_TRACK		= 2;
	
	final private static int TAG_ARTIST		= 10;
	final private static int TAG_ALBUM		= 11;
	final private static int TAG_TRACK		= 12;

	final private static int CONTEXT_ARTIST	= 20;
	final private static int CONTEXT_ALBUM	= 21;
	final private static int CONTEXT_TRACK	= 22;
	
	/*
	cddb_tags
	tracks_tag
	context_info
	*/

	public Validator(IMusicFile mf) {
		this.mf = mf;
		retrieveCDDB();
		retrieveTAG();
	}
	
	public FreedbReadResult[] retrieveCDDB() {
		if (mf instanceof Album) {
			Album album = (Album) mf;
			
			// retrieve CDDB album infos
			CDDBquery cddb = new CDDBquery();
			dbResult = cddb.queryAlbumInfos(album);
			
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// TODO retrieve CDDB track infos
		}
		return dbResult;
	}
	
	/**
	 * Return one Tag of the IMusicFile :
	 * - FIRST track AudioFile Tag of an Album
	 * - AudioFile Tag of a Track
	 */
	public void retrieveTAG() {
		if (mf instanceof Album) {
			Album album = (Album) mf;
			Track track = null;
			// retrieve album infos
			List<IMusicFile> tracks = album.getContainedList();
			Iterator<IMusicFile> iterator = tracks.iterator();
			if (iterator.hasNext()) {
					track = (Track) iterator.next();
					tag = track.getTag();
			}
			
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// retrieve track infos
		}
	}
	
	public void retrieveCONTEXT() {
		
	}
	
	public void validate() {
		if (mf instanceof Album) {
			Album album = (Album) mf;
			
			// validation
			for (FreedbReadResult cddb : dbResult) {
				// with Tag infos
				if (cddb.getAlbum().equals(tag.getAlbum()))
				{
					// TODO indice de qualité + update XML
					generateIQV(CDDB_ALBUM, TAG_ALBUM);
//					album.getXMLElement().setAlbumName(cddb.getAlbum());
				}
				// with File names infos
				else if (cddb.getAlbum().equals(album.getName()))
				{
					// TODO indice de qualité + update XML
				}
				// with parent folder infos
//				else if (cddb.getArtist().equals(album.getParentFolderName()))
				{
					// TODO indice de qualité + update XML
				}
			}
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// TODO retrieve CDDB track infos
		}
	}
	
	public void generateIQV(int firstOperand, int secOperand) {
		
	}
	
}

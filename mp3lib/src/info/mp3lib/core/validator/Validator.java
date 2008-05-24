package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.core.IMusicFile;
import info.mp3lib.core.Track;
import info.mp3lib.util.cddb.CDDBquery;

import java.util.Iterator;
import java.util.List;

import entagged.freedb.FreedbReadResult;

public class Validator {

	IMusicFile mf = null;
	FreedbReadResult[] dbResult = null;
	/*
	cddb_tags
	tracks_tag
	context_info
	*/

	public Validator(IMusicFile mf) {
		this.mf = mf;
		// retrieve CDDB infos
		retrieveCDDB();
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
	
	public void retrieveTAG() {
		if (mf instanceof Album) {
			Album album = (Album) mf;
			Track track = null;
			// retrieve album infos
			List<IMusicFile> tracks = album.getContainedList();
			Iterator<IMusicFile> iterator = tracks.iterator();
			while (iterator.hasNext()) {
					track = (Track) iterator.next();
					track.getTag();
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
			for (FreedbReadResult info : dbResult) {
				// with Tag infos
				if (info.getAlbum().equals(album.getTagAlbum()))
				{
					// TODO indice de qualité + update XML
				}
				// with File names infos
				else if (info.getAlbum().equals(album.getName()))
				{
					// TODO indice de qualité + update XML
				}
				// with parent folder infos
				else if (info.getArtist().equals(album.getParentFolderName()))
				{
					// TODO indice de qualité + update XML
				}
			}
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// TODO retrieve CDDB track infos
		}
	}
	
	public void generateIQV() {
		
	}
	
}

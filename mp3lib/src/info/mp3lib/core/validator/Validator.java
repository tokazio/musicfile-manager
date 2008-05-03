package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.core.IMusicFile;
import info.mp3lib.core.Track;

import java.util.Iterator;
import java.util.List;

import entagged.audioformats.Tag;

public class Validator {

	IMusicFile mf = null;
	
	cddb_tags
	tracks_tag
	context_info

	public Validator(IMusicFile mf) {
		this.mf = mf;
	}
	
	public void retrieveCDDB() {
		if (mf instanceof Album) {
			Album album = (Album) mf;
			
			// TODO retrieve CDDB album infos
			
			
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// TODO retrieve CDDB track infos
		}
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
					track.getT
			}
			
		} else if (mf instanceof Track) {
			Track track = (Track) mf;
			
			// retrieve track infos
		}
	}
	
	public void retrieveCONTEXT() {
		
	}
	
	public void validate() {
		
	}
	
	public void generateIQV() {
		
	}
	
	private Tag get
	
}

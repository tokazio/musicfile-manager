package info.mp3lib.core.business.scan;

import info.mp3lib.core.types.MusicData;

import java.io.File;

import entagged.audioformats.Tag;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.audioformats.generic.GenericTag;

public class ScanData extends MusicData {
	
	private static final long serialVersionUID = -601051253164507347L;

	public ScanData(File file) throws CannotReadException {
		super(file);
		
		
		// *** Business specific implementation ***
		Tag tag = new GenericTag();//new TagField();
		
		// set tags Info when available
		if (tag.getArtist().isEmpty() ||
			tag.getAlbum().isEmpty() ||
			tag.getTitle().isEmpty())
		{
			// query CDDB (single file method..)
			
			// set tag info
			tag.setArtist("");
			tag.setAlbum("");
			tag.setTitle("");
			tag.setTrack("");
		}
	}
	
	void writeXML() {
		System.out.println(this.getClass()+": writeXML() implementation to do in an inherited format..");
	}
}

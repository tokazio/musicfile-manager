package info.mp3lib.core.business.scan;

import info.mp3lib.core.types.MusicData;

import java.io.File;

import entagged.audioformats.Tag;
import entagged.audioformats.generic.TagField;

public class ScanData extends MusicData {
	
	private static final long serialVersionUID = -601051253164507347L;

	public ScanData(File file) {
		super(file);
		
		
		// *** Business specific implementation ***
		Tag tag = new TagField();
		
		// set tags Info when available
		if (tag.getArtist().isEmpty() ||
			tag.getAlbum().isEmpty() ||
			tag.getTitle().isEmpty())
		{
			// query CDDB
			tag.setArtist("");
			tag.setAlbum("");
			tag.setTitle("");
			tag.setTrack("");
		}
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		System.out.println(this.getClass()+": writeData() implementation to do..");
	}
	
}

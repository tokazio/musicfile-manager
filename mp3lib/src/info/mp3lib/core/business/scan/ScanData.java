package info.mp3lib.core.business.scan;

import info.mp3lib.core.types.AbstractMusicData;

import java.io.File;

import org.w3c.dom.Node;

import entagged.audioformats.Tag;

public class ScanData extends AbstractMusicData {
	
	public ScanData(File file) {
		super(file);
		
		// *** Business specific implementation ***
		
		// set tags Info when available
		if (tag.getArtist().isEmpty() ||
			tag.getAlbum().isEmpty() ||
			tag.getTitle().isEmpty())
		{
			// query CDDB
			tag.setAlbum("");
		}
	}
	
}

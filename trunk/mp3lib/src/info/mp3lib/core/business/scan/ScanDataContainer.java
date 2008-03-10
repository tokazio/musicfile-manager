package info.mp3lib.core.business.scan;

import info.mp3lib.core.types.AbstractMusicDataContainer;

import java.util.ArrayList;

/**
 * Store a MusicData source (XML / File / Tag) to this Container
 * 
 * -> Implements writeToXml() method for this container.
 * 
 * @author AkS
 */
public class ScanDataContainer extends AbstractMusicDataContainer {
	
	private static final long serialVersionUID = 7589599919516298527L;
	
	ArrayList<ScanData> container = null;
	
	public void writeToXml()
	{
		// créé un nouveau noeud XML :
		// -> MuiscDataContainer + node
		// -> ` MusicData + filepath + tags + boolean isTagged
	}
	
}

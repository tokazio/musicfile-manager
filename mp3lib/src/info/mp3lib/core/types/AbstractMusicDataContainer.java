package info.mp3lib.core.types;

import java.util.ArrayList;

/**
 * Store a MusicData source (XML / File / Tag) to this Container
 *  
 * @author AkS
 */
public class AbstractMusicDataContainer extends ArrayList<AbstractMusicData> {
	
	private static final long serialVersionUID = 7208528853334813094L;
	
	ArrayList<AbstractMusicData> container = null;
	
	public void writeToXml()
	{
		// implementation générique ?
	}
	
}

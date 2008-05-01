package info.mp3lib.core.types;

import java.util.ArrayList;

/**
 * Store a MusicData source (XML / File / Tag) to this Container
 *  
 * @author AkS
 */
public class AbstractMusicDataContainer {
	
	private static final long serialVersionUID = 7208528853334813094L;
	
	ArrayList<AbstractMusicData> container = null;
	
	public ArrayList<AbstractMusicData> getContainer() {
		return container;
	}

	public void writeToXml()
	{
		System.out.println(this.getClass()+": No generic implementation done for writeToXml().");
		// implementation générique ?
	}
	
}

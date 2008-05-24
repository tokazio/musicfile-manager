package info.mp3lib.core.types;

import java.util.ArrayList;

/**
 * Store a MusicData source (XML / File / Tag) to this Container
 *  
 * @author AkS
 */
public interface IMusicDataContainer {
	
	ArrayList<IMusicData> container = null;
	
	abstract <T> ArrayList<T> getContainer();

	public void write();
	
}

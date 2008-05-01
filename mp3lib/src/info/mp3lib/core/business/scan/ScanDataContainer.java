package info.mp3lib.core.business.scan;

import info.mp3lib.core.types.IMusicDataContainer;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Store a MusicData source (XML / File / Tag) to this Container
 * 
 * -> Implements writeToXml() method for this container.
 * 
 * @author AkS
 */
public class ScanDataContainer implements IMusicDataContainer {
	
	private static final long serialVersionUID = 7589599919516298527L;
	
	ArrayList<ScanData> container = null;

	@SuppressWarnings("unchecked")
	public ArrayList<ScanData> getContainer() {
		return container;
	}


	@Override
	public void write()
	{
		// TODO créé un nouveau noeud XML :
		// TODO -> MuiscDataContainer + node
		
		Iterator<ScanData> iter = container.iterator();
		ScanData mData = null;
		
		while (iter.hasNext())
		{
			// write Music Data with ScanData implementation.
			mData = iter.next();
			mData.write();
		}
	}
	
}

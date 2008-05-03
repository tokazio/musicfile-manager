package info.mp3lib.core.dataScanner;

import info.mp3lib.core.Album;
import info.mp3lib.core.Artist;
import info.mp3lib.core.Track;

import java.io.File;

/**
 * Builds all the Album objects from the physical tree and stores them in a unique
 * Artist object
 */

public class DataScanner {
	private static final long serialVersionUID = -1963252596917406454L;

	Artist scanList = new Artist("unknown");

	/**
	 * Parcours récursivement une arborescence de répertoires :
	 * -> quand il contient des fichiers musicaux,
	 * 			- on créé un nouveau container de MusicData
	 * 			- on ajoute les fichiers au container
	 * 			- on ajoute le container à la liste scanList
	 * -> quand il ne contient que des répertoire, il traite
	 *    successivement (et récursivement) les sous répertoires présents.
	 * @param rootPath Point d'entrée du Scan de l'arborescence. 
	 */

	public void read(File path) //throws BusinessException
	{
		Track mData = null;
		Album mCont = null;

		// erreur d'initialisation
		if (path.isDirectory() == false)
			return;

		// - For each entry in this rootPath folder
		File[] fs = path.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory() == false) // files
			{
				// create new Container if first.
				if (mCont == null)
					mCont = new Album();

				// construct new ScanMusicData from this File.
				mData = new Track(fs[i]);

				// add MusicData to its Container.
				mCont.add(mData);
			} else // sub folders
			{
				// recursively check sub directories
				this.read(fs[i]);
			}
		}

		// store new unknown Album to scanList (unknown Artist)
		if (mCont != null)
			scanList.add(mCont);
	}

}

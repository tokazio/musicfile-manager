package info.mp3lib.core.main;

import info.mp3lib.core.dataScanner.DataScanner;

import java.io.File;
import java.io.IOException;

/**
 * Main-Class Application
 * 
 * @author Administrateur @
 */

public class Mp3lib {

	/**
	 * @param args
	 * 
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Mp3lib m = new Mp3lib();
		
		final String musicDirectory = "E:/mp3test";
		File musicFileDir = new File(musicDirectory); 
		DataScanner.getInstance().read(musicFileDir);
		
		System.out.println("Terminé.");
	}

	/**
	 * Ceci est une description
	 * 
	 * @author Administrateur
	 * @param s
	 *            Nom du fichier
	 * @return true si reussite, false sinon.
	 * @throws IOException
	 */
	public String testEx(String s) throws IOException {
		return "bla";
	}

}

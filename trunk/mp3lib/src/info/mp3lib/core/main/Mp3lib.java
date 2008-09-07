package info.mp3lib.core.main;

import info.mp3lib.core.Library;
import info.mp3lib.core.dao.fs.DataScanner;
import info.mp3lib.core.dao.xml.LibraryDAO;

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

	// Set entry point.
	final String musicDirectory = "E:/mp3test";
	// Set XML library file path
	final String xmlLibrary = "E:/zicfile.xml";
	
	
	// Launch SCAN procedure : Fill Library
	System.out.println("Scanning directory ["+musicDirectory+"]...");
	DataScanner.getInstance().read(new File(musicDirectory));
	System.out.println("Scan complete !");
	
	// Save library to XML
	LibraryDAO.getInstance().write(Library.getInstance(), xmlLibrary);
	
	// TODO: reload library from existing XML if user-requested ..
	
	// Launch TAG procedure : make best tag..
	System.out.println("Validating library ...");
	Library.getInstance().validate();
	System.out.println("Validation complete !");

    }

    /**
     * Ceci est une description
     * 
     * @author Administrateur
     * @param s
     *                Nom du fichier
     * @return true si reussite, false sinon.
     * @throws IOException
     */
    public String testEx(String s) throws IOException {
	return "bla";
    }

}

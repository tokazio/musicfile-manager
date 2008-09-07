package info.mp3lib.core.dao.xml;

import info.mp3lib.core.Album;
import info.mp3lib.core.Library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Data Access Object for the <code>Library</code> class
 * 
 * @author AkS
 */
public class LibraryDAO {

    /** the unique instance of the singleton */
    private static LibraryDAO instance;

    /** static access to the singleton */
    public static LibraryDAO getInstance() {
	if (instance == null) {
	    instance = new LibraryDAO();
	}
	return instance;
    }

    /** Constructor */
    private LibraryDAO() {
    }

    /** Apache log4j logger */
    private final static Logger LOGGER = Logger.getLogger(Album.class.getName());

    public Library read(final String filePath) {
	final Library result = null;
	final File xmlFile = new File(filePath);
	if (xmlFile.exists()) {
	    if (xmlFile.canRead()) {
		// TODO check extension, implement
	    } else {
		LOGGER.error(new StringBuffer("The given file [").append(filePath)
			.append("] is not readable").toString());
	    }
	} else {
	    LOGGER.error(new StringBuffer("The given file [").append(filePath).append("] does not exist")
		    .toString());
	}
	return result;
    }

    /**
     * Save the given library on the file system
     * 
     * @param lib the <code>Library</code> to persist
     * @filePath the absolute path of the file in which writing the XML tree
     * @return true if the given library is successfully persisted, false
     *         otherwise
     */
    public boolean write(Library lib, final String filePath) {
	boolean result = false;
	final File xmlFile = new File(filePath);
	try {
	    FileOutputStream fos = new FileOutputStream(xmlFile);
	    XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat()); // TODO
	    // set
	    // configurable

	    outputter.output(lib.getRootElement(), fos);
	    result = true;
	    fos.close();
	} catch (IOException e) {
	    LOGGER.error(new StringBuffer("Unable to persist the library in the file [").append(filePath)
		    .append("]:\n").append(e.getMessage()).toString());
	}
		return result;
    }
    
//    /** 
//     * Adds a path attribute denoting the physical location on the file system of the audioFile
//     * for all tracks Elements
//     * @param lib the <code>Library</code> to process
//     */
//    private void addPersistenceData(Library lib) {
//    	// retrieve the artist list
//    	final List<Element> artistlist = lib.getRootElement().getChildren(XMLMusicElement.ELT_ARTIST);
//    	for (Iterator it = artistlist.iterator(); it.hasNext();) {
//			Element element = (Element) it.next();
//			// retrieve the album list
//			
//		}
//    }

}

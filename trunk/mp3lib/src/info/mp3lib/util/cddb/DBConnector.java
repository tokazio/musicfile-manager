package info.mp3lib.util.cddb;

import info.mp3lib.config.ConfigurationException;
import info.mp3lib.core.Album;

public class DBConnector implements IDBQuery {

    /** PRIVATE STATIC ACCESS */
    private static DBConnector instance = null;
    
    private DBConnector() {
	if (instance == null) {
	    instance = new DBConnector();
	}
    }
    
    private static DBConnector getInstance() {
	if (instance == null) {
	    new DBConnector();
	}
	return instance;
    }
    /** -------------------- */
    
    /** PRIVATE INSTANCE ACCESS */
    private IDBQuery impl = null;
    
    private static IDBQuery getImpl() {
	if (getInstance().impl == null) {
	    String implName = "FreeDBQuery";
	    //TODO: read config
	    
//	    ResourceBundle.getBundle(baseName).getS
	    
	    try {
		getInstance().impl = (IDBQuery) Class.forName(implName).newInstance();
	    } catch (Exception e) {
		//TODO: CONFIG.JAVA MODIFICATION
		new ConfigurationException("Unable to load the implementation class for tag DataBase access defined in config file: "+e.getMessage());
	    }
	}
	return getInstance().impl;
    }
    
    
    /** */
    public ITagQueryResult[] queryAlbum(Album album) {
	return getImpl().queryAlbum(album);
    }
    
}

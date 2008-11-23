package info.mp3lib.util.cddb;

import info.mp3lib.config.Config;
import info.mp3lib.config.ConfigurationException;

public class DBConnector {

    /** ----- PUBLIC STATIC ACCESS ----- */
    
    public static IDBQuery getImpl() {
	if (getInstance().queryImplInstance == null) {
	    Class<IDBQuery> dbquery = Config.getConfig().getTagDatabaseAccessImpl();
	    try {
		getInstance().queryImplInstance = (IDBQuery) dbquery.newInstance();
	    } catch (Exception e) {
		System.out.println("ERROR: "+e.getMessage()+" / "+e.getLocalizedMessage()+" class: "+e.getClass()+" cause: "+e.getCause());
		new ConfigurationException(new StringBuffer("Unable to instanciate class implementation of IDBQuery [")
		.append(dbquery.getName()).append("]\nCause was: ").append(e.getMessage()).toString());
	    }
	}
	return getInstance().queryImplInstance;
    }
    
    /** ----- PRIVATE STATIC ACCESS ----- */
    
    private static DBConnector instance = null;
    
    private static DBConnector getInstance() {
	if (instance == null) {
	    instance = new DBConnector();
	}
	return instance;
    }
    
    /** ----- PRIVATE INSTANCE ACCESS ----- */
    
    private IDBQuery queryImplInstance = null;
    
}

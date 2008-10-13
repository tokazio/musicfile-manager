package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.util.cddb.DBResult;
import info.mp3lib.util.cddb.ITagQueryResult;

/**
 * Denotes the query result associated to an album.<br/>
 * @author AkS
 */
public class DBSelector implements TagSelector {

//    private Album album;
    private DBResult[] dbResults;
    
    /**
     * Retrieve CDDB Query Results.
     * @param pAlbum
     */
    public DBSelector(final Album pAlbum, ITagQueryResult[] dbQueryResults) {
//	album = pAlbum;
	this.dbResults = (DBResult[]) dbQueryResults;
	// -- query call
	// retrieve query answer
//	dbResults = DBConnector.getImpl().queryAlbum(album);
    }
    
    @Override
    public int selectTagsAlbum(Context context, ITagQueryResult result) {
	// on choisit la requete qui correspond le + au contexte parmis ts les resultats et on la renvoie
	int scoreMax = -1, iBestResult = -1, score = 0;
	for (int i = 0; i < dbResults.length; i++) {
	    // TODO: compareTo ou autre fonction : compare un Context au TagResult CDDB
	    score = dbResults[i].compareTo(context);
	    if (scoreMax < score) {
		scoreMax = score;
		iBestResult = i;
	    }
	}
	
	result = dbResults[iBestResult];
	return scoreMax;
    }
}

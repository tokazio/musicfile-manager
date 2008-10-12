package info.mp3lib.util.cddb;


import entagged.freedb.FreedbReadResult;

public class DBResult extends FreedbReadResult implements ITagQueryResult {

    public DBResult(String freedbReadResult, boolean exactMatch) {
	super(freedbReadResult, exactMatch);
    }

    public DBResult(String freedbReadResult, String genre) {
	super(freedbReadResult, genre);
    }
    
}

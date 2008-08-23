package info.mp3lib.util.cddb;

import entagged.freedb.FreedbReadResult;

public class CDDBResult extends FreedbReadResult implements ITagQueryResult {

    public CDDBResult(String freedbReadResult, boolean exactMatch) {
	super(freedbReadResult, exactMatch);
    }

    public CDDBResult(String freedbReadResult, String genre) {
	super(freedbReadResult, genre);
    }
    
}

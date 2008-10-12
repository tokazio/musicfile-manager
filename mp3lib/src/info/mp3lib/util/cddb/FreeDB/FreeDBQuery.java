package info.mp3lib.util.cddb.FreeDB;

import info.mp3lib.core.Album;
import info.mp3lib.core.Track;
import info.mp3lib.util.cddb.DBResult;
import info.mp3lib.util.cddb.IDBQuery;

import java.util.Iterator;

import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;
import entagged.freedb.FreedbReadResult;

public class FreeDBQuery extends entagged.freedb.Freedb implements IDBQuery {
    
    private FreeDBResult[] freedbResult;

    public FreeDBQuery() {
	super();
    }
    
    /**
     * Build CDDB request for this album.
     * Results are saved in instance.
     * @param album (a contener of sized tracks)
     */
    public DBResult[] queryAlbum(Album album) {
//	Freedb freedb = new Freedb();
//	String[] serv;
//	try {
//	    serv = freedb.getAvailableServers();

	    freedbResult = queryAlbumInfos(album);

//	    for (int i = 0; i < freedbResult.length; i++) {
//		System.out.println("RESULT N°" + i);
//		System.out.println(freedbResult.toString());
//	    }
//
//	    for (int i = 0; i < serv.length; i++) {
//		System.out.println(serv[i]);
//	    }
//	} catch (FreedbException e) {
//	    e.printStackTrace();
//	}
	
	return freedbResult;
    }
    
    public FreeDBResult readResult(FreedbQueryResult query) throws FreedbException {
		
	    //Parse the result
	    return new FreeDBResult(super.read(query));
    }
    
    @Override
    public String[] getAvailableServers() throws FreedbException {
	return super.getAvailableServers();
    }

    public FreedbQueryResult[] query(float[] times) throws FreedbException {
	SimpleTrack[] tracks = new SimpleTrack[times.length];
	for (int i = 0; i < tracks.length; i++)
	    tracks[i] = new SimpleTrack(times[i]);
	return query(tracks);
    }
    
    public FreeDBResult[] queryAlbumInfos(Album album) {
	// retrieve track length
	float size[] = new float[album.getSize()];
	Iterator<Track> iterator = album.getTrackIterator();

	for (int tId = 0; iterator.hasNext(); tId++) {
	    size[tId] = iterator.next().getLength();
	}
	try {
	    // submit Album query
	    FreedbQueryResult[] result = null;
	    result = query(size);

	    // submit Track query 
	    freedbResult = new FreeDBResult[result.length];
	    for (int i = 0; i < result.length; i++) {
		freedbResult[i] = (FreeDBResult) readResult(result[i]);
	    }

	} catch (FreedbException e) {
	    e.printStackTrace();
	}

	// return album & tracks values
	return freedbResult;
    }
    
}

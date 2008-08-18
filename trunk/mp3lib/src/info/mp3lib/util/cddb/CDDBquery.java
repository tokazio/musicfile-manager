package info.mp3lib.util.cddb;

import info.mp3lib.core.Album;
import info.mp3lib.core.Track;

import java.util.Iterator;

import entagged.freedb.Freedb;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;
import entagged.freedb.FreedbReadResult;

public class CDDBquery extends entagged.freedb.Freedb {

    FreedbReadResult[] freedbResult;

    /**
     * Build CDDB request for this album.
     * Results are saved in instance.
     * @param album (a contener of sized tracks)
     */
    public CDDBquery(Album album, String query) {
	Freedb freedb = new Freedb();
	String[] serv;
	try {
	    serv = freedb.getAvailableServers();

	    freedbResult = queryAlbumInfos(album);

	    for (int i = 0; i < freedbResult.length; i++) {
		System.out.println("RESULT N°" + i);
		System.out.println(freedbResult.toString());
	    }

	    for (int i = 0; i < serv.length; i++) {
		System.out.println(serv[i]);
	    }

	} catch (FreedbException e) {
	    e.printStackTrace();
	}
    }

    public CDDBquery() {
    }

    public FreedbReadResult[] queryAlbumInfos(Album album) {
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
	    freedbResult = new FreedbReadResult[result.length];
	    for (int i = 0; i < result.length; i++) {
		freedbResult[i] = read(result[i]);
	    }

	} catch (FreedbException e) {
	    e.printStackTrace();
	}

	// return album & tracks values
	return freedbResult;
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

    /**
     * @return the freedbResult
     */
    public FreedbReadResult[] getFreedbResult() {
	return freedbResult;
    }

}

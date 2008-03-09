package info.mp3lib.util.cddb;

import info.mp3lib.core.xom.Album;
import info.mp3lib.core.xom.Track;

import java.io.File;

import entagged.freedb.Freedb;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;

public class CDDBquery extends entagged.freedb.Freedb {
	public static void main(String[] args) {
		new CDDBquery("","");
	}

	/**
	 * Construit une requette A PARTIR d'un album
	 */
	public CDDBquery(String directory, String query) {
		Freedb freedb = new Freedb();
		String[] serv;
		try {
			serv = freedb.getAvailableServers();
			// http://www.freedb.org/freedb_search.php?words=high+tone&allfields=YES
			// FreedbQueryResult[] r = freedb.query("metallica");
			
			FreedbQueryResult[] queryResult = freedb.query("hightone");
			System.out.println("request result: " + super.read(queryResult[0]));
			/*
			 * if (r.length == 0) System.out.println("baad"); for (int i = 0; i <
			 * r.length; i++) { System.out.println(r[i].getAlbum()); } //
			 * freedb.query(album);
			 * 
			 */
			for (int i = 0; i < serv.length; i++)
				System.out.println(serv[i]);
		} catch (FreedbException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String[] getAvailableServers() throws FreedbException {
		return super.getAvailableServers();
	}

	/**
	 * Créé un album de tracks
	 * @return
	 */
	public static Album createAlbum(File directory)
	{
		Album album = new Album(directory);
		return album;
	}
	/**
	 * Retrieve CDDB tags of Album in parameter
	 * @param album
	 * @return {@link FreedbQueryResult}[] if valid result else null
	 */
	public FreedbQueryResult[] queryAlbum(Album album)
	{
		float size[] = new float[album.getLength()];
		FreedbQueryResult[] result = null;
		for (int tId=0; tId < album.getLength(); tId++)
		{
			Track track = (Track) album.getItem(tId);
			size[tId] = track.getXMLLength();
			
		}
		try {
			result = this.query(size);
		} catch (FreedbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
    public FreedbQueryResult[] query(float[] times) throws FreedbException {
        SimpleTrack[] tracks = new SimpleTrack[times.length];
        for(int i = 0; i< tracks.length; i++)
            tracks[i] = new SimpleTrack(times[i]);
        return query(tracks);
    }
	
}

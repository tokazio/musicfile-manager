package info.mp3lib.util.cddb;

import info.mp3lib.core.Album;
import info.mp3lib.core.IMusicFile;
import info.mp3lib.core.Track;

import java.io.File;
import java.util.Iterator;

import entagged.freedb.Freedb;
import entagged.freedb.FreedbAlbum;
import entagged.freedb.FreedbException;
import entagged.freedb.FreedbQueryResult;
import entagged.freedb.FreedbReadResult;
import entagged.freedb.FreedbTrack;

public class CDDBquery extends entagged.freedb.Freedb {
	public static void main(String[] args) {
		new CDDBquery("G:/MP3/[ Ragga - Dancehall ]/Capleton/I testament","");
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
			
			File adir = new File(directory);
			Album album = new Album(adir);
			FreedbTrack[] tracks = new FreedbTrack[album.getSize()];
			
			for (int tId=0; tId < album.getSize(); tId++)
			{
				Track track = (Track) album.getItem(tId);
				FreedbTrack st = new SimpleTrack(track.getLength());
				tracks[tId] = st;
			}
			FreedbAlbum fab = new FreedbAlbum((FreedbTrack[]) tracks);
			
			FreedbQueryResult[] result = queryAlbum(album);
			
			FreedbQueryResult[] albumResult = query(fab);
			
			Iterator<IMusicFile> trackIT = album.getIterator();
			
			for (int i=0; i<result.length; i++) {
				System.out.println("RESULT N°"+i);
				FreedbReadResult readResult = read(result[i]);
				System.out.println(readResult.toString());
			}
			
			/*
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

	public CDDBquery() {}

	public FreedbReadResult[] queryAlbumInfos(Album album)
	{
		// return variable
		FreedbReadResult[] readResult = null;
		
		// retrieve track length
		float size[] = new float[album.getLength()];
		for (int tId=0; tId < album.getLength(); tId++)
		{
			size[tId] = ((Track) album.getItem(tId)).getLength();
		}
		try {
			// submit Album query
			FreedbQueryResult[] result = null;
			result = query(size);
			
			// submit Track query 
			readResult = new FreedbReadResult[result.length];
			for (int i=0; i<result.length; i++)
			{
				readResult[i] = read(result[i]);
			}
			
		} catch (FreedbException e) {
			e.printStackTrace();
		}
		
		// return album & tracks values
		return readResult;
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
			size[tId] = track.getLength();
			
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

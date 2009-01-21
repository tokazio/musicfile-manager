package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.util.cddb.DBConnector;
import info.mp3lib.util.cddb.ITagQueryResult;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

//TODO check utility of this f**king class !!
public class Validator {

	Album album = null;
	ITagQueryResult[] dbResults = null;

	/*
	 * cddb_tags tracks_tag context_info
	 */

	public Validator(Album album) {
		this.album = album;
//		retrieveTAG();
//		retrieveCONTEXT();
//		retrieveCDDB();

	}

	public void validate() {
	    ITagQueryResult bestOne = null, bestPhyResult = null, bestTagResult = null;
	    int bestOneScore = -1, bestPhyScore = -1, bestTagScore = -1;
	    
	    
	    // create physical context
	    Context physicalContext = new PhysicalContext(album);
	    // create tag context
	    Context tagContext = new TagContext(album);

	    System.out.println("BEST ONE IS ....... :");
	    
	    // call cddb
	    dbResults = DBConnector.getImpl().queryAlbum(album);
	    if (dbResults != null)
	    {
		System.out.println("DBRESULT: "+dbResults[0].getAlbum());
	    
		// select best result
		DBSelector dbSelector = new DBSelector(album, dbResults);
		bestPhyScore = dbSelector.selectTagsAlbum(physicalContext, bestPhyResult);
		bestTagScore = dbSelector.selectTagsAlbum(tagContext, bestTagResult);
	    
		if (bestPhyScore <= bestTagScore) {
		    bestOneScore = bestTagScore;
		    bestOne = bestTagResult;
		} else {
		    bestOneScore = bestPhyScore;
		    bestOne = bestPhyResult;
		}
		
		System.out.println("-- ARTIST ["+bestOne.getArtist()+"] ALBUM ["+bestOne.getAlbum()+"] YEAR["+bestOne.getYear()+"] GENRE["+bestOne.getGenre()+"]");
	    }
	    else {
		System.out.println("NO MATCH ...");
		
		// TODO: select best result
//		TagSelector tagSelect = new XXXSelector(album);
//		tagSelect.selectTagsAlbum(physicalContext, bestOne);
//		tagSelect.selectTagsAlbum(tagContext, bestOne);
//		...
		
		// MOK:
//		bestOne = new TagResult();
//		bestOne.setAlbum()
	    }
	    
	    // TODO: (and to think) write album to tag / XML or what ......
	    album.write(bestOne);
	}
	
	public void retrieveCDDB() {
		// retrieve CDDB album infos
		dbResults = DBConnector.getImpl().queryAlbum(album);
	}

	/**
	 * Retrieve Tags of a this Album with FIRST track infos ..
	 * 
	 */
	public void retrieveTAG() {
//		Track track = null;
//		// retrieve album infos
//		Iterator<Track> iterator = album.getTrackIterator();
//		if (iterator.hasNext()) {
//			track = iterator.next();
//			tagInfos.get("FileTag").album = track.getTag().getFirstAlbum();
//			tagInfos.get("FileTag").artist = track.getTag().getFirstArtist();
//			tagInfos.get("FileTag").year = Integer.parseInt(track.getTag().getFirstYear());
//		}
//		tagInfos.get("FileTag").size = track.getLength();
	}

	public void retrieveCONTEXT() {
	    
	}

	public void updateXMLAlbum(TagInfos finalTag) {
		// TODO Pour les albums, l'id peut correspondre a leur ordre de sortie
		// Pour les tracks, il correspond au numéro de piste
		album.setId(finalTag.id);
		
		album.moveTo(finalTag.artist);
		
		album.setName(finalTag.album);
		album.setSize(finalTag.size);
		album.setYear(finalTag.year);
	}

	public void doValidate() {
		/*
		 * 	A REVOIR PROCESSUS DE VALIDATION ...
		 * 
		 * dans l'idéal les différentes étapes seraient :
		 *	- TESTER LA PERTINENCE DU CONTEXTE (album...)
		 *	- TESTER LA PERTINENCE DES TAGS AVEC LE CONTEXTE
		 *	- TESTER LA PERTINENCE DE CDDB AVEC LES TAGS ET LE CONTEXTE
		 *
		 * pour chaque étape on peut aussi :
		 * 	- GENERER UN INDICE DE QUALITE
		 * 	- PROPOSER UN TAG CORRIGE ET REGENERER L'INDICE
		 * 
		 * les parametres qui serviront a generer l'indice de qualité se basent sur :
		 *	- LE FAIT QUE CERTAINS CHAMPS SOIT VIDES OU NON
		 *	- LE FAIT QUE LES COMPARAISONS SOIENT QUASIMENT SIMILAIRES OU NON
		 *	- L'ORIGINE DES TAGS
		 *	- LES ELEMENTS QUI CONFIRMENT LA PERTINENCE DU TAG
		 * 
		 * une fois toutes les étapes terminées :
		 *	- SELECTION DU TAG AVEC LE MEILLEUR INDICE
		 *	- UPDATE XML des TAGS (Artist + Album + Tracks) avec IQV
		 *	  (pas de gestion des doublons ici)
		 * 
		 * 
		 * a noter :
		 *	*	cddb renvoie des infos sur un album ET ses tracks a la fois
		 *		(on peut traiter à la fois Artists, Albums et Tracks pour valider le tout ..)
		 *	*	tagInfos est actuellement un tableau initialisé a la mano
		 *		(avec 10 valeurs de + que le nombre de résultats CDDB ..)
		 *	*	voir si ca fait pas trop de tags stockés a chaque fois mé bon fo ce ki fo !!
		 *		(faire un minimum gaffe au performances kan mem ...)
		 *
		 */
		
		// Validation TAGS / Context
		FileTagValidation();
		
		// Validation CDDB (for each possibilities)
		if (dbResults.length != 0) {
			for (int i = 0; i < dbResults.length; i++) {
				tagInfos.put("cddb_"+i, DBResultValidation(dbResults[i]));
			}
		} else {
			// no CDDB result : low level IQV => back to TAG infos
		}

		// TODO SUP analyse les differences entre tous les tagInfos
		//		et fait les corrections éventuelles sur le tag final
		TagInfos finalTag = makeBestResult();
		
		// TODO: re-génération de l'IQV final
		updateFinalIQV(finalTag);
		
		// Update XML
		updateXMLAlbum(finalTag);
	    
	}
	
	HashMap<String,TagInfos> tagInfos = new HashMap<String,TagInfos>();

	final private static int CDDB_ARTIST = 0;
	final private static int CDDB_ALBUM = 1;
	final private static int CDDB_TRACK = 2;

	final private static int TAG_ARTIST = 10;
	final private static int TAG_ALBUM = 11;
	final private static int TAG_TRACK = 12;

	final private static int CONTEXT_ARTIST = 20;
	final private static int CONTEXT_ALBUM = 21;
	final private static int CONTEXT_TRACK = 22;
	
	private class TagInfos {
		int origine = -1;
		int confirm = -1;
		int IQV		= -1;

		public int id		= -1;
		public int size		= -1;
		public int year		= -1;
		public String album = null;
		public String artist= null;
		
		public int generateIQV(int origine, int confirm) {
			if (origine == CDDB_ALBUM ||
				origine == CDDB_ARTIST ||
				origine == CDDB_TRACK)
			{
				if (confirm == TAG_ALBUM ||
					confirm == TAG_ARTIST ||
					confirm == TAG_TRACK)
				{
					this.IQV = 100;
				} else if (confirm == CONTEXT_ALBUM
						|| confirm == CONTEXT_ARTIST
						|| confirm == CONTEXT_TRACK)
				{
					this.IQV = 80;
				}
			} else if (origine == TAG_ALBUM
					|| origine == TAG_ARTIST
					|| origine == TAG_TRACK)
			{
				if (confirm == CONTEXT_ALBUM ||
					confirm == CONTEXT_ARTIST ||
					confirm == CONTEXT_TRACK)
				{
					this.IQV = 60;
				}
			} else if (origine == CONTEXT_ALBUM || origine == CONTEXT_ARTIST
					|| origine == CONTEXT_TRACK)
			{
				this.IQV = 30;
			}
			return this.IQV;
		}
	}

	
	
	/**
	 * Generate Parameters like IQV to select the best one later
	 * - origine du tag
	 * - nivo de confirmation interne
	 * - ...
	 * @param cddb
	 * @return
	 */
	public TagInfos DBResultValidation(ITagQueryResult cddb) {
		TagInfos tag = new TagInfos();
		
		// With TAG infos ..
		if (cddb.getAlbum().equals(album.getName())) {
			tag.generateIQV(CDDB_ALBUM, TAG_ALBUM);
		}
		if (cddb.getArtist().equals(album.getName())) {
			tag.generateIQV(CDDB_ARTIST, TAG_ARTIST);
		}
		/*
		final int tracksNumber = cddb.getTracksNumber();
		final List<Tag> tagTracks = ((AudioFile) album.getTrack();
		for (int t = 0; t < tracksNumber && t < tagTracks.size(); t++) {
			if (cddb.getTrackTitle(t).equals(
					tagTracks.get(t).getTrack())) {
				tag.generateIQV(CDDB_TRACK, TAG_TRACK);
			}
		}
		*/
		// With Context Infos ...
		if (cddb.getAlbum().equals(album.getName())) {
			tag.generateIQV(CDDB_ALBUM, CONTEXT_ALBUM);
		} else if (cddb.getArtist().equals(album.getName())) {
			tag.generateIQV(CDDB_ALBUM, CONTEXT_ARTIST);
		}
		return tag;
	}

	public TagInfos FileTagValidation() {
		// TODO
		if (album.isTagged()) {
			// TODO check pertinence of each tag fields ..
			// TODO generate IQV
		} // else if ( etc... )
		return tagInfos.get("FileTag");
	}
	
	public TagInfos makeBestResult() {
		TagInfos tag = new TagInfos();
		// select best result
		Set<String> keys = tagInfos.keySet();
		Iterator<String> it = keys.iterator();
		int max = 0;
		String key, maxKey = null;
		while (it.hasNext()) {
			key = it.next(); 
			if (tagInfos.get(key).IQV > max) {
				max = tagInfos.get(key).IQV;
				maxKey = key;
			}
		}
		if (maxKey != null) {
			tag = tagInfos.get(maxKey);
		}
		// TODO correct fields with others tags if necessary
		return tag;
	}
	
	public int updateFinalIQV(TagInfos finalTag) {
		// TODO re calcul eventuel ...
		return finalTag.IQV;
	}
	
}

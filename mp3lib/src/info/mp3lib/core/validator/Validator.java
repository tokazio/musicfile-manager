package info.mp3lib.core.validator;

import info.mp3lib.core.Album;
import info.mp3lib.core.IMusicFile;
import info.mp3lib.core.Track;
import info.mp3lib.util.cddb.CDDBquery;

import java.util.Iterator;
import java.util.List;

import entagged.audioformats.AudioFile;
import entagged.audioformats.Tag;
import entagged.freedb.FreedbReadResult;

public class Validator {

	Album album = null;
	FreedbReadResult[] cddbInfos = null;
	TagInfos[] tagInfos = null;

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

	final private static int CDDB_ARTIST = 0;
	final private static int CDDB_ALBUM = 1;
	final private static int CDDB_TRACK = 2;

	final private static int TAG_ARTIST = 10;
	final private static int TAG_ALBUM = 11;
	final private static int TAG_TRACK = 12;

	final private static int CONTEXT_ARTIST = 20;
	final private static int CONTEXT_ALBUM = 21;
	final private static int CONTEXT_TRACK = 22;

	/*
	 * cddb_tags tracks_tag context_info
	 */

	public Validator(IMusicFile mf) {
		album = (Album) mf;
		retrieveCDDB();
		retrieveTAG();
		retrieveCONTEXT();
	}

	public void retrieveCDDB() {
		// retrieve CDDB album infos
		CDDBquery cddb = new CDDBquery();
		cddbInfos = cddb.queryAlbumInfos(album);
	}

	/**
	 * Retrieve Tags of a this Album with FIRST track infos ..
	 * 
	 */
	public void retrieveTAG() {
		Track track = null;
		// retrieve album infos
		List<IMusicFile> tracks = album.getContainedList();
		Iterator<IMusicFile> iterator = tracks.iterator();
		if (iterator.hasNext()) {
			track = (Track) iterator.next();
			tagInfos[0].album = track.getTag().getFirstAlbum();
			tagInfos[0].artist = track.getTag().getFirstArtist();
			tagInfos[0].year = Integer.parseInt(track.getTag().getFirstYear());
		}
		tagInfos[0].size = tracks.size();
	}

	public void retrieveCONTEXT() {

	}

	public void updateXMLAlbum(TagInfos finalTag) {
		// TODO Pour les albums, l'id peut correspondre a leur ordre de sortie
		// Pour les tracks, il correspond au num�ro de piste
		album.getXMLElement().setId(finalTag.id);
		
		// TODO WARNING : manipulation de xml pour creer ou d�placer un node XML
		// quand on change le nom de l'artiste sur la m�thode setArtist() !
		album.getXMLElement().setArtist(finalTag.artist);
		
		album.getXMLElement().setName(finalTag.album);
		album.getXMLElement().setSize(finalTag.size);
		album.getXMLElement().setYear(finalTag.year);
	}

	public void validate() {
		/*
		 * 	A REVOIR PROCESSUS DE VALIDATION ...
		 * 
		 * dans l'id�al les diff�rentes �tapes seraient :
		 *	- TESTER LA PERTINENCE DU CONTEXTE (bof ..)
		 *	- TESTER LA PERTINENCE DES TAGS AVEC LE CONTEXTE
		 *	- TESTER LA PERTINENCE DE CDDB AVEC LES TAGS ET LE CONTEXTE
		 *
		 * pour chaque �tape on peut aussi :
		 * 	- GENERER UN INDICE DE QUALITE
		 * 	- PROPOSER UN TAG CORRIGE ET REGENERER L'INDICE
		 * 
		 * les parametres qui serviront a generer l'indice de qualit� se basent sur :
		 *	- LE FAIT QUE CERTAINS CHAMPS SOIT VIDES OU NON
		 *	- LE FAIT QUE LES COMPARAISONS SOIENT QUASIMENT SIMILAIRES OU NON
		 *	- L'ORIGINE DES TAGS
		 *	- LES ELEMENTS QUI CONFIRMENT LA PERTINENCE DU TAG D'ORIGINE
		 * 
		 * une fois toutes les �tapes termin�es :
		 *	- SELECTION DU TAG AVEC LE MEILLEUR INDICE
		 *	- UPDATE XML des TAGS (Artist + Album + Tracks) avec IQV
		 *	  (pas de gestion des doublons a ce niveau !)
		 *
		 * 
		 * a noter :
		 *	*	cddb renvoie des infos sur un album ET ses tracks a la fois
		 *		(on peut traiter � la fois Artists, Albums et Tracks pour valider le tout ..)
		 *	*	tagInfos est actuellement un tableau initialis� a la mano
		 *		(avec 10 valeurs de + que le nombre de r�sultats CDDB ..)
		 *	*	voir si ca fait pas trop de tags stock�s a chaque fois m� bon fo ce ki fo !!
		 *		(faire un minimum gaffe au performances kan mem ...)
		 *
		 */
		
		// cr�ation des diff�rents tag possibles pour un album et ses tracks
		// TODO faire un hash map pour diff�rencier chaque �tape ..
		tagInfos = new TagInfos[cddbInfos.length+10];
		
		// Validation TAGS / Context
		// TODO commencer par valider les tags si il faut..
		if (album.isTagged()) {
			// TODO check pertinence of each tag fields ..
			// TODO generate IQV
		} // else if ( etc... )
		
		// Validation CDDB (for each possibilities)
		if (cddbInfos.length != 0) {
			for (int i = 0; i < cddbInfos.length; i++) {
				tagInfos[i+1] = CDDBResultValidation(cddbInfos[i]);
			}
		} else {
			// no CDDB result : low level IQV => back to TAG infos
		}

		// TODO SUP analyse les differences entre tous les tagInfos
		//		et fait les corrections �ventuelles sur le tag final
		TagInfos finalTag = makeProperResult();
		
		// TODO: g�n�ration de l'IQV final
		finalTag.IQV = generateFinalIQV(finalTag);
		
		// Update XML
		updateXMLAlbum(finalTag);
	}

	/**
	 * Generate Parameters like IQV to select the best one later
	 * - origine du tag
	 * - nivo de confirmation interne
	 * - ...
	 * @param cddb
	 * @return
	 */
	public TagInfos CDDBResultValidation(FreedbReadResult cddb) {
		TagInfos tag = new TagInfos();
		
		// With TAG infos ..
		if (cddb.getAlbum().equals(((AudioFile)album.getFile()).getTag().getAlbum())) {
			tag.generateIQV(CDDB_ALBUM, TAG_ALBUM);
		}
		if (cddb.getArtist().equals(((AudioFile)album.getFile()).getTag().getArtist())) {
			tag.generateIQV(CDDB_ARTIST, TAG_ARTIST);
		}
		final int tracksNumber = cddb.getTracksNumber();
		final List<Tag> tagTracks = ((AudioFile) album.getFile()).getTag().getTrack();
		for (int t = 0; t < tracksNumber && t < tagTracks.size(); t++) {
			if (cddb.getTrackTitle(t).equals(
					tagTracks.get(t).getTrack())) {
				tag.generateIQV(CDDB_TRACK, TAG_TRACK);
			}
		}
		// With Context Infos ...
		if (cddb.getAlbum().equals(album.getName())) {
			tag.generateIQV(CDDB_ALBUM, CONTEXT_ALBUM);
		} else if (cddb.getArtist().equals(album.getArtist())) {
			tag.generateIQV(CDDB_ALBUM, CONTEXT_ARTIST);
		}
		return tag;
	}

	public TagInfos makeProperResult() {
		// TODO select best result
		int i = 0;
		TagInfos tag = tagInfos[i];
		// TODO correct if necessary
		return tag;
	}
	
	public int generateFinalIQV(TagInfos finalTag) {
		// TODO re calcul eventuel ...
		return finalTag.IQV;
	}
	
}

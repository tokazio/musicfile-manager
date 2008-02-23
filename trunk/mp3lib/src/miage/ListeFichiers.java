package miage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import miage.sgbd.DataProvider;
import miage.sgbd.SqlProvider;

import entagged.audioformats.AudioFile;
import entagged.audioformats.exceptions.CannotReadException;
import entagged.tageditor.optionpanels.FreedbOptionPanel;
import entagged.tageditor.optionpanels.GeneralOptionPanel;
import entagged.tageditor.optionpanels.OptionDialog;
import entagged.tageditor.optionpanels.OptionPanelInterface;
import entagged.tageditor.resources.PreferencesManager;

/**
 * Classe permettant la recherche de fichiers physique
 * @author Nicolas Velin
 */
public class ListeFichiers {

	// PARTIE OBJET

	private Vector hierarchieDossier;

	public ListeFichiers(File dossier) {
		hierarchieDossier = new Vector();
		while(dossier != null) {
			hierarchieDossier.add(dossier);
			dossier = dossier.getParentFile();
		}
	}

	/**
	 *
	 * @param listeDossiers
	 * @return
	 */
	public boolean isChild(Vector listeDossiers) {
		boolean child = false;

		String folder1, folder2;
		int i = 0, j;
		int imax = hierarchieDossier.size();
		int jmax = listeDossiers.size();
		while(!child && i < imax) {
			folder1 = ((File)hierarchieDossier.get(i)).getAbsolutePath();
			j = 0;
			while(!child && j < jmax) {
				folder2 = ((File)listeDossiers.get(j)).getAbsolutePath();
				if(folder1.compareToIgnoreCase(folder2) == 0)
					child = true;
				j++;
			}
			i++;
		}

		return child;
	}

	/**
	 *
	 * @param dossier
	 * @return
	 */
	public boolean isChild(File dossier) {
		boolean child = false;

		String folder1;
		String folder2 = dossier.getAbsolutePath();
		int i = 0;
		int imax = hierarchieDossier.size();
		while(!child && i < imax) {
			folder1 = ((File)hierarchieDossier.get(i)).getAbsolutePath();
			if(folder1.compareToIgnoreCase(folder2) == 0)
				child = true;
			i++;
		}

		return child;
	}


	// PARTIE STATIQUE

	public final static String MP3 = "MP3";
	public final static String WMA = "WMA";
	public final static String OGG = "OGG";
	public final static String FLAC = "FLAC";
	public final static String MPC = "MPC";
	public final static String APE = "APE";
	private static ArrayList fichiers;

	/**
	 * Recherche des fichiers dans un dossiers et ses sous dossiers ayant un certain type
	 * @param element C'est un fichier ou un dossier
	 * @param type Les différents types audios recherchés
	 */
	public static void rechercheFichierRecursive(String element, ArrayList type) {
		File f = new File(element);
		if(f.exists()) {
			// L'élément est un dossier
			if(f.isDirectory()) {
				String [] elements = f.list();
				if(elements != null)
					for(int i = 0 ; i < elements.length ; i++)
						rechercheFichierRecursive(element + File.separatorChar + elements[i], type);
			}
			// L'élément est un fichier
			else {
				StringTokenizer str = new StringTokenizer(element, ".");
				String extAudio = "";
				while(str.hasMoreTokens())
					extAudio = str.nextToken();
				if(type.contains(extAudio.toUpperCase())) {
					try {
						AudioFile af = entagged.audioformats.AudioFileIO.read(f);
						fichiers.add(af);
					}
					catch (CannotReadException e) {
						e.printStackTrace();
					}
				}

			}
		}
	}

	/**
	 * Lance la recherche de fichiers récursive
	 * @param racine le dossier de départ
	 * @return la liste des fichiers de type AudioFile
	 */
	public static ArrayList rechercheFichier(String racine) {
		ArrayList type = new ArrayList();
		type.add(MP3);
		type.add(WMA);
		type.add(OGG);
		type.add(FLAC);
		type.add(MPC);
		type.add(APE);
		fichiers = new ArrayList();
		rechercheFichierRecursive(racine,type);
		return fichiers;
	}

	/**
	 * Lance la recherche de fichiers dans un Thread et ajoute le contenu dans la table
	 * @param listeDossier les dossiers de départ
	 * @param owner la fenetre principale
	 */
	public static void rechercheFichierThread(final String[] listeDossier) {
		Thread performer = new Thread(new Runnable() {
			public void run() {
				ArrayList liste;
				for(int i = 0 ; i < listeDossier.length ; i++) {
					liste = ListeFichiers.rechercheFichier(listeDossier[i]);
					DataProvider.insererFichiers(liste);
				}
				int NbFichiers = SqlProvider.getNbFichier();
				JOptionPane.showMessageDialog(null, "L'indexation des fichiers est terminée.\n" + NbFichiers + " fichiers ont été indexés.", "Indexation des fichiers", JOptionPane.INFORMATION_MESSAGE);
			}
		}, "Performer");
		performer.start();
	}
	
	public static void play(JFrame owner, String url) {
		String prog = PreferencesManager.get("entagged.mediaplayer");
		boolean error = true;
		while(error) {
			if(prog == null || !new File(prog).exists()) {
				OptionPanelInterface general = new GeneralOptionPanel(owner);
				OptionPanelInterface freedb = new FreedbOptionPanel();
				OptionPanelInterface[] opts = new OptionPanelInterface[] {general, freedb};
				OptionDialog eod = new OptionDialog(owner,opts);
				eod.setVisible(true);
			}
			prog = PreferencesManager.get("entagged.mediaplayer");

			try {
				Runtime.getRuntime().exec("\"" + prog + "\" \"" + url + "\"");
				error = false;
			}
			catch(IOException e1) {
				JOptionPane.showMessageDialog(null, "Erreur dans le choix de votre lecteur ! A Traduire", "Erreur", JOptionPane.WARNING_MESSAGE);
				prog = null;
			}
		}
	}
	
}

package miage;

import java.io.File;
import java.util.ArrayList;

import miage.sgbd.Connexion;
import miage.sgbd.SqlProvider;

/**
 * Classe exécutant des programmes de test 
 * @author Nicolas Velin ; Clément Fourel
 */
public class Test {
	
	private static Connexion connexion;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		connexion = Connexion.getInstance();
		ArrayList<Object[]> artistesDoubles = SqlProvider.getArtistesDoubles();
		for(int i = 0 ; i < artistesDoubles.size() ; i++)
			System.out.println(artistesDoubles.get(i)[1].toString());
		//SqlProvider.createDataBase();
		connexion.shutdown();
		gui_sgbd();
	}
	
	private static void gui_sgbd() {
		String url = "jdbc:hsqldb:file:" + entagged.tageditor.resources.PreferencesManager.USER_PROPERTIES_DIR + "" + File.separatorChar + "database";
		String [] str = {"--url",url};
		//org.hsqldb.util.DatabaseManagerSwing.main(str);
	}
}
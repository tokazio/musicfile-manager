package info.mp3lib.util.string.test;

import info.mp3lib.util.string.StringMatcher;

public class TestStringMatcher {

	public static void main(String[] args) {
		//String s1 = "ACGGCTAT";
		//String s2 = "ACTGTAT";
		//String s1 = "Birélie Lagrène & Sylvain Luc";
		//String s2 = "birelie et sylvain";
		String s2 = "Meï_Teï_Shô_-_Lô-bâ_-_01._Stand_up_and_fight_again";
		String s1 = "blabla - Stand up and fight again";
		long start = System.currentTimeMillis();// calcul du temps d'execution
		StringMatcher sm = new StringMatcher(s1, s2);
		//System.out.println(sm);
		String[] sAlign = sm.align();
		long msecs = System.currentTimeMillis() - start;
		System.out.println("Tps d'exécution : "+msecs+" ms.");
		System.out.println(sAlign[0]);
		System.out.println(sAlign[1]);
		System.out.println("scoreMax = "+sAlign[2]);
		System.out.println("scoreMin = "+sAlign[3]);
		System.out.println("score = "+sAlign[4]);
		System.out.println("pourcentage = "+sAlign[5]);
		
		//ITaggedMusicFile t = new Album(new File(""));
	}

}

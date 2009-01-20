package info.mp3lib.util.string;

public class KiriStringMatcher {
	private String str1;
	private char[] ch1;
	private int lg_ch1;
	
	private String str2;
	private char[] ch2;
	private int lg_ch2;
	
	private int[][] matrix;
	private static int gapCost = -1;
	private static int substCost = -5;
	private static int matchCost = 2;
	
	public KiriStringMatcher() {
		str1 = "";
		lg_ch1 = 0;
		
		str2 = "";
		lg_ch2 = 0;
	}
	
	public KiriStringMatcher(String s1, String s2) {
		lg_ch1 = s1.length();
		str1 = new String(s1);
		//System.out.println("s1 avant : "+str1);
		s1 = StringUtils.normalize(str1, false, true, false); // remove accents
		//System.out.println("s1 apres : "+s1);
		ch1 = new char[lg_ch1];
		ch1 = s1.toCharArray();
		
		lg_ch2 = s2.length();
		str2 = new String(s2);
		//System.out.println("s2 avant : "+str2);
		s2 = s1 = StringUtils.normalize(str1, false, true, false); // remove accents
		//System.out.println("s2 apres : "+s2);
		ch2 = new char[lg_ch2];
		ch2 = s2.toCharArray();
		
		matrix = new int[lg_ch2+1][lg_ch1+1];
		fillMatrix();
	}
	
	private void fillMatrix() {
		// initialise matrix
		matrix[0][0] = 0;
		for (int i = 1; i <= lg_ch2; i++) {
			matrix[i][0] = i*gapCost;
		}
		for (int i = 1; i <= lg_ch1; i++) {
			matrix[0][i] = i*gapCost;
		}
		
		// rempli la matrice
		int val_match, val_del,	val_ins;
		for (int i = 0; i < lg_ch2; i++) {
			for (int j = 0; j < lg_ch1; j++) {
				if(compare(ch1[j], ch2[i])) { // case diagonale
					val_match = matrix[i][j] + matchCost;
				}
				else {
					val_match = matrix[i][j] + subst(ch1[j], ch2[i]);
				}
				val_del = matrix[i][j+1] + gapCost; // case a gauche
				val_ins = matrix[i+1][j] + gapCost; // case en haut
				matrix[i+1][j+1] = max(val_match, val_del, val_ins);
			}
		} 
	}
	
	public String[] align() {
		String[] sAlign = new String[6];
		sAlign[0]=""; // chaine1
		sAlign[1]=""; // chaine2
		int i = lg_ch2;
		int j = lg_ch1;
		int score=0;
		int cumul=0;
		while ((i > 0) && (j > 0)) {
			if (compare(ch1[j-1], ch2[i-1])) { // match
				score += matchCost;
				sAlign[0] += ch1[j-1];
				sAlign[1] += ch2[i-1];
				cumul++;
				i--;
				j--;
			}
			else if (    (matrix[i-1][j-1] >= matrix[i-1][j])
					  && (matrix[i-1][j-1] >= matrix[i][j-1])) { // substitution
				score += substCost;
				sAlign[0] += ch1[j-1];
				sAlign[1] += ch2[i-1];
				cumul++;
				i--;
				j--;
			}
			else if (    (matrix[i-1][j] >= matrix[i-1][j-1])
				      && (matrix[i-1][j] >= matrix[i][j-1])) { // deletion
				if(cumul==1) { // 1 lettre isolée alignée
					sAlign[0] = sAlign[0].substring(0, sAlign[0].length()-1);
					sAlign[0] += "--";
					sAlign[1] += ch2[i-1];
					score -= matchCost;
					score += (2*gapCost);
					j++;
					cumul = 0;
				}
				else {
					score += gapCost;
					sAlign[0] += "-";
					sAlign[1] += ch2[i-1];
				}
				i--;
			}
			else { // insertion
				if(cumul==1) { // 1 lettre isolée alignée
					sAlign[1] += sAlign[1].substring(0, sAlign[1].length()-1);
					sAlign[1] += "--";
					sAlign[0] += ch1[j-1];
					score -= matchCost;
					score += (2*gapCost);
					i++;
					cumul = 0;
				}
				else {
					score += gapCost;
					sAlign[0] += ch1[j-1];
					sAlign[1] += "-";
				}
				j--;
			}
		}
		
		while (i > 0) { // chaine 2 terminée, fini de remplir la chaine 1 par des gaps
			sAlign[0] += "-";
			sAlign[1] += ch2[i-1];
			i--;
			score += gapCost;
		}
		while (j > 0) {// chaine 1 terminée, fini de remplir la chaine 2 par des gaps
			sAlign[0] += ch1[j-1];
			sAlign[1] += "-";
			j--;
			score += gapCost;
		}
		
		sAlign[0] = reverseString(sAlign[0]);
		sAlign[1] = reverseString(sAlign[1]);

		sAlign[2] = String.valueOf(scoreMax());
		sAlign[3] = String.valueOf(scoreMin());
		sAlign[4] = String.valueOf(score);
		sAlign[5] = String.valueOf(pourcentSimil(scoreMax(), scoreMin(), score));
		
		return sAlign;
	}
	
	public String toString() {
		String s = new String("Chaine 1 : "+str1+"\nChaine 2 : "+str2+"\nMatrice :\n");
		for (int i = 0; i <= lg_ch2; i++) {
			for (int j = 0; j <= lg_ch1; j++) {
				s += matrix[i][j]+" ";
			}
			s += "\n";
		}
		s+="\n";
		return s;
	}
	
	private String reverseString(String s) {
		// remet dans le bon sens les deux chaines
		String revString = "";
		int lg = s.length();
		
		// chaine 1
		for (int i = lg; i > 0; i--) {
			revString += s.substring(i-1, i);
		}
		return revString;
	}
	
	private int max(int a, int b, int c) {
		int max = 0;
		if ((a >= b) && (a >= c)) {	max = a;}
		else if ((b >= a) && (b >= c)) { max = b;}
		else { max = c;	}
		return max;
	}
	
	private boolean compare(char c1, char c2) {
		if (Character.toUpperCase(c1) == Character.toUpperCase(c2)) { // ne tiend pas compte de la casse
			return true;
		}
		else if(   ((c1 == ' ') || (c1 == '-') || (c1 == '_') || (c1 == '\'') || (c1 == '\"'))
				&& ((c2 == ' ') || (c2 == '-') || (c2 == '_') || (c2 == '\'') || (c2 == '\"'))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	private int scoreMax() {
		int score = 0;
		if (lg_ch1 > lg_ch2) {
			score += lg_ch2*matchCost; // toute la sous-chaine match
			score += (lg_ch1 - lg_ch2)*(gapCost); // prise en compte des caracteres supplémentaires dans ch1 par des gap
		}
		else if (lg_ch1 < lg_ch2) {
			score += lg_ch1*matchCost; // toute la sous-chaine match
			score += (lg_ch2 - lg_ch1)*(gapCost); // prise en compte des caracteres supplémentaires dans ch1 par des gap
		}
		else { // lg_ch1 == lg_ch2
			score += lg_ch1*matchCost; // les deux chaines sont identiques
		}
		return score;
	}
	
	private int scoreMin() {
		int score = 0;
		if (lg_ch1 > lg_ch2) {
			score += lg_ch2*substCost; // toute la sous-chaine match
			score += (lg_ch1 - lg_ch2)*(gapCost); // prise en compte des caracteres supplémentaires dans ch1 par des gap
		}
		else if (lg_ch1 < lg_ch2) {
			score += lg_ch1*substCost; // toute la sous-chaine match
			score += (lg_ch2 - lg_ch1)*(gapCost); // prise en compte des caracteres supplémentaires dans ch1 par des gap
		}
		else { // lg_ch1 == lg_ch2
			score += lg_ch1*substCost; // les deux chaines sont identiques
		}
		return score;
	}
	
	private int pourcentSimil(int max, int min, int score) {
		int adjust = 0 - min;
		min = 0;
		max += adjust;
		score += adjust;
		int pourcent = (score*100) / max;
		return pourcent;
	}
	private int subst(char c1, char c2) {
		return substCost;
	}
}

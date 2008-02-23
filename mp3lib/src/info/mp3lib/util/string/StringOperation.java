package info.mp3lib.util.string;

import java.util.*;
public abstract class StringOperation
{
    private static final int MIN = 192; // Index du 1er caractere accentué
    private static final int MAX = 255; // Index du dernier caractere accentué
    private static final Vector<String> map = initMap();// Vecteur de correspondance entre accent/sans accent
    
    // Initialisation du tableau de correspondance entre caracteres accentues et leur homologues non accentues
    private static Vector<String> initMap() {
    	Vector<String> Result = new Vector<String>();
	   java.lang.String car = null;

		car = new java.lang.String("A");
		Result.add(car); // À => '\u00C0'  alt-0192
		Result.add(car); // Á => '\u00C1'  alt-0193
		Result.add(car); // Â => '\u00C2'  alt-0194
		Result.add(car); // Ã => '\u00C3'  alt-0195
		Result.add(car); // Ä => '\u00C4'  alt-0196
		Result.add(car); // Å => '\u00C5'  alt-0197
		car = new java.lang.String("AE");
		Result.add(car); // Æ => '\u00C6'  alt-0198
		car = new java.lang.String("C");
		Result.add(car); // Ç => '\u00C7'  alt-0199
		car = new java.lang.String("E");
		Result.add(car); // È => '\u00C8'  alt-0200
		Result.add(car); // É => '\u00C9'  alt-0201
		Result.add(car); // Ê => '\u00CA'  alt-0202
		Result.add(car); // Ë => '\u00CB'  alt-0203
		car = new java.lang.String("I");
		Result.add(car); // Ì => '\u00CC'  alt-0204
		Result.add(car); // Í => '\u00CD'  alt-0205
		Result.add(car); // Î => '\u00CE'  alt-0206
		Result.add(car); // Ï => '\u00CF'  alt-0207
		car = new java.lang.String("D");
		Result.add(car); // Ð => '\u00D0'  alt-0208
		car = new java.lang.String("N");
		Result.add(car); // Ñ => '\u00D1'  alt-0209
		car = new java.lang.String("O");
		Result.add(car); // Ò => '\u00D2'  alt-0210
		Result.add(car); // Ó => '\u00D3'  alt-0211
		Result.add(car); // Ô => '\u00D4'  alt-0212
		Result.add(car); // Õ => '\u00D5'  alt-0213
		Result.add(car); // Ö => '\u00D6'  alt-0214
		car = new java.lang.String("*");
		Result.add(car); // × => '\u00D7'  alt-0215
		car = new java.lang.String("0");
		Result.add(car); // Ø => '\u00D8'  alt-0216
		car = new java.lang.String("U");
		Result.add(car); // Ù => '\u00D9'  alt-0217
		Result.add(car); // Ú => '\u00DA'  alt-0218
		Result.add(car); // Û => '\u00DB'  alt-0219
		Result.add(car); // Ü => '\u00DC'  alt-0220
		car = new java.lang.String("Y");
		Result.add(car); // Ý => '\u00DD'  alt-0221
		car = new java.lang.String("Þ");
		Result.add(car); // Þ => '\u00DE'  alt-0222
		car = new java.lang.String("B");
		Result.add(car); // ß => '\u00DF'  alt-0223
		car = new java.lang.String("a");
		Result.add(car); // à => '\u00E0'  alt-0224
		Result.add(car); // á => '\u00E1'  alt-0225
		Result.add(car); // â => '\u00E2'  alt-0226
		Result.add(car); // ã => '\u00E3'  alt-0227
		Result.add(car); // ä => '\u00E4'  alt-0228
		Result.add(car); // å => '\u00E5'  alt-0229
		car = new java.lang.String("ae");
		Result.add(car); // æ => '\u00E6'  alt-0230
		car = new java.lang.String("c");
		Result.add(car); // ç => '\u00E7'  alt-0231
		car = new java.lang.String("e");
		Result.add(car); // è => '\u00E8'  alt-0232
		Result.add(car); // é => '\u00E9'  alt-0233
		Result.add(car); // ê => '\u00EA'  alt-0234
		Result.add(car); // ë => '\u00EB'  alt-0235
		car = new java.lang.String("i");
		Result.add(car); // ì => '\u00EC'  alt-0236
		Result.add(car); // í => '\u00ED'  alt-0237
		Result.add(car); // î => '\u00EE'  alt-0238
		Result.add(car); // ï => '\u00EF'  alt-0239
		car = new java.lang.String("d");
		Result.add(car); // ð => '\u00F0'  alt-0240
		car = new java.lang.String("n");
		Result.add(car); // ñ => '\u00F1'  alt-0241
		car = new java.lang.String("o");
		Result.add(car); // ò => '\u00F2'  alt-0242
		Result.add(car); // ó => '\u00F3'  alt-0243
		Result.add(car); // ô => '\u00F4'  alt-0244
		Result.add(car); // õ => '\u00F5'  alt-0245
		Result.add(car); // ö => '\u00F6'  alt-0246
		car = new java.lang.String("/");
		Result.add(car); // ÷ => '\u00F7'  alt-0247
		car = new java.lang.String("0");
		Result.add(car); // ø => '\u00F8'  alt-0248
		car = new java.lang.String("u");
		Result.add(car); // ù => '\u00F9'  alt-0249
		Result.add(car); // ú => '\u00FA'  alt-0250
		Result.add(car); // û => '\u00FB'  alt-0251
		Result.add(car); // ü => '\u00FC'  alt-0252
		car = new java.lang.String("y");
		Result.add(car); // ý => '\u00FD'  alt-0253
		car = new java.lang.String("þ");
		Result.add(car); // þ => '\u00FE'  alt-0254
		car = new java.lang.String("y");
		Result.add(car); // ÿ => '\u00FF'  alt-0255
		Result.add(car); //   => '\u00FF'  alt-0255

		return Result;
    }

    /** Transforme une chaine pouvant contenir des accents dans une version sans accent
     *  @param chaine Chaine a convertir sans accent
     *  @return Chaine dont les accents ont été supprimé
     **/
    public static java.lang.String sansAccent(java.lang.String chaine)
    {  java.lang.StringBuffer Result  = new StringBuffer(chaine);
      
       for(int bcl = 0 ; bcl < Result.length() ; bcl++)
       {   int carVal = chaine.charAt(bcl);
           if( carVal >= MIN && carVal <= MAX )
           {  // Remplacement
              java.lang.String newVal = (java.lang.String)map.get( carVal - MIN );
              Result.replace(bcl, bcl+1,newVal);
           }   
       }
       return Result.toString();
   }
}
package javascool;


public class Conversion {

	/**
	 * cette fonction foncvertit un nombre decimal en base 16
	 * @param nb le nombre a convertir
	 * @return le nombre en base 16 resultat de la conversion
	 */
	public static String decimalToHexa(int nb){
		String res = null;
		try{
			res = Integer.toString(nb, 16);
		}catch(Exception e){System.err.println("fonction DecimalToHexa le nombre donne n'est pas un nombre" +
		"decimal");}

		return res;
	}

	/**
	 * cette fonction convertit un nombre decimal en nombre binaire
	 * @param nb le nombre decimal a convertir
	 * @return le nombre binaire sous forme de tableau resultant de la conversion
	 */
	public static int[] decimalToBinaire(int nb){
		int[] res = null;
		try{
			String tmp = Integer.toString(nb, 2);
			char[] tmp2 = tmp.toCharArray();
			res = new int[tmp2.length];
			for(int i=0; i <tmp2.length; i++)
				res[i] = Integer.parseInt(Character.toString(tmp2[i]));
		}catch(Exception e){System.err.println("fonction DecimalToBinaire le nombre donne n'est pas un nombre" +
		"decimal");}

		return res;
	}


	/**
	 * cette fonction convertit un nombre binaire en nombre decimal
	 * @param tab le nombre binaire sous forme de tablea d'entiers a convertir
	 * @return la conversion du nombre binaire en decimal
	 */
	public static int binaireToDecimal(int[] tab){
		String tmp="";
		int res = 0;
		try{
			for(int t: tab)
				tmp+=t+"";
			res = Integer.parseInt(tmp, 2);
		}catch(Exception e){System.err.println("fonction BinaireToDecimal le nombre donne n'est pas un nombre" +
		"binaire");}
		return res;
	}


	/**
	 * cette fonction convertit un nombre binaire en nombre hexadecimal
	 * @param tab le tableau representant le nombre binaire a traduire
	 * @return le nombre hexadecimal correspondant au nombre hexadecimal
	 */
	public static String binaireToHexa(int[] tab){
		String tmp="";
		String res = null;
		try{
			for(int t: tab)
				tmp+=t+"";
			int tmp2 = Integer.parseInt(tmp, 2);
			res  = Integer.toString(tmp2, 16);
		}catch(Exception e){System.err.println("fonction BinaireToHexa le nombre donne n'est pas un nombre" +
		"binaire");}
		return res;
	}


	/**
	 * cette fonction convertit un nombre hexadecimal en nombre binaire 
	 * @param s le nombre decimal a convertir en hexadecimal
	 * @return  le nombre hexadecimal resultant de la conversion
	 */
	public static int[] hexaToBinaire(String s){
		int[] res = null;
		try{
			int a = Integer.parseInt(s, 16);
			String tmp = Integer.toString(a, 2);
			char[] tmp2 = tmp.toCharArray();
			 res = new int[tmp2.length];
			for(int i=0; i< tmp2.length; i++)
				res[i] = Integer.parseInt(Character.toString(tmp2[i]));
		}catch(Exception e){System.err.println("fonction HexaToBinaire le nombre donne n'est pas un nombre" +
		"hexadecimal");}
		return res;
	}

	/**
	 * cette fonction convertit un nombre Hexadecimal en nombre binaire
	 * @param s le nombre hexadecimal a convertir en decimal
	 * @return le nombre decimal resultant de la conversion 
	 */
	public static int hexaToDecimal(String s){
		int res = 0;
		try{
			res = Integer.parseInt(s, 16);
		}catch(Exception e){System.err.println("fonction  HexaToDecimal le nombre donne n'est pas un nombre" +
		"hexadecimal");}
		return res;
	}
}

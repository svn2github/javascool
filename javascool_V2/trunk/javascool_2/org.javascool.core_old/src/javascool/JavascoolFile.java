package javascool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

public class JavascoolFile {

	private static BufferedReader br;
	private static String text;
	private static int index;
	private static PrintWriter pw;
	private static int nbRead = 1;

	/**
	 * cette fonction permet d'ouvrir un fichier en lecture
	 * @param path
	 */
	public static void openReadFile(String path){
		FileReader fr;
		String line ="";
		nbRead=1;
		try {
			text="";
			index=0;
			fr = new FileReader(path);
			br = new BufferedReader (fr) ;
			while ((line = br.readLine()) != null){
				text+=line;
			}


		} catch (Exception e) {
			System.err.println("Erreur d'ouverture du fichier en lecture");
		}
	}

	/**
	 * cette fonction permet d'ouvrir en ecriture une fichier
	 * @param path le chemin du fichier a ouvrir en ecriture
	 */
	public static void openWriteFile(String path){
		try {
			pw = new PrintWriter(path);	
		} catch (Exception e) {
			System.err.println("Erreur d'ouverture du fichier en ecriture");
		}
	}

	/**
	 * cette fonction lit le nombres de caractéres spécifié sur le fichier
	 * @param n le nombres de caracteres à lire
	 * @return
	 */
	public static int[] readBit(int n){
		int[] res = new int[n];
		try{
			String lu = (String) text.subSequence(index, n*nbRead);
			index+=n;
			for(int i=0; i <lu.length(); i++){
				char c = lu.charAt(i);
				res[i] = Integer.parseInt(Character.toString(c));
			}
			nbRead++;
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("erreur de lecture du caractere sur le fichier");
		}
		return res;
	}

	/**
	 * cette mehode permet de fermer un fichier ouvert en lecture
	 */
	public static void closeReadFile(){
		try{
			br.close();
		}catch(Exception e){
			System.err.println("Erreur de fermeture du fichier");
		}
	}

	/**
	 * cette mehode permet de fermer un fichier ouvert en ecriture
	 */
	public static void closeWriteFile(){
		try{
			pw.close();
		}catch(Exception e){
			System.err.println("Erreur de fermeture du fichier");
		}
	}

	
	/**
	 * cette fonction permet de connaitre le nombre de caractere present dans le fichier
	 * @param path
	 * @return
	 */
	public static int nbBitToFile(String path){
		int res=0;
		try{
			openReadFile(path);
			res = text.length();	
			closeReadFile();
		}catch(Exception e){
			System.err.println("Erreur de comptage du nombres de bits du fichier");
		}
		return res;

	}

	/**
	 * cette fonction permet d'ecrire une chaine de caractere dans le fichier
	 * @param s la chaine a ecrire dans le fichier
	 */
	public static void printFile(String s){
		try{
			pw.print(s);
		}catch(Exception e){
			System.err.println("Erreur d'ecriture sur le fichier");
		}
	}

	
	/**
	 * cette fonction permet d'ecrire une chaine de caractere dans un 
	 * fichier et effectue un retour a la ligne
	 * @param s
	 */
	public static void printlnFile(String s){
		try{
			pw.println(s);
		}catch(Exception e){
			System.err.println("Erreur d'ecriture sur le fichier");
		}
	}
}
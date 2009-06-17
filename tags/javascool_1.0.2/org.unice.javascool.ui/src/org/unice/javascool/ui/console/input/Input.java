package org.unice.javascool.ui.console.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.regex.MatchResult;

//TODO : dans le catch il faut tuer le scanner et un refaire un nouveau
/*
 *     InputMismatchException - if the next token is not a valid boolean 
    NoSuchElementException - if input is exhausted 
    IllegalStateException - if this scanner is closed
 */

/*
 * Les nombres flottants contiennent un . et non pas une ,
 * ScanBool renvoie tjs false sauf si en entr�e on a mis true
 * 
 */


/*
 * Singleton
 */

/*
 * Pour tester 
 * 
 Input.Initialize();
			new Thread(new Runnable(){
				public void run() {
					try{
						int b = -1 ;
						
						System.out.println("true or else ?");
						if (Input.scanBoolean()){
							System.out.println("alors");
						}
						else {
							System.out.println("sinon");
						}
						
						System.out.println("===================");
						float f ;
						System.out.println("1/2 = ?");
						f = Input.scanFloat();
						System.out.println("resultat "+f);
						
						System.out.println("===================");
						char c;
						System.out.println("premire lettre de ton nom ?");
						c = Input.scanChar();
						System.out.println("resultat :"+c);
						
						System.out.println("===================");
						
						System.out.println("Quel est ton age ?");
						b = Input.scanInt();
						System.out.println("Age : "+b);
						
						System.out.println("===================");
						String s ;
						System.out.println("Nom Pr�nom ?");
						s = Input.scanString();
						System.out.println("nom prenom :"+s);
						
						System.out.println("===================");
						double d ;
						System.out.println("65000 + 65000 = ?");
						d = Input.scanDouble();
						System.out.println("resultat "+d);
						
						
						
						Input.Kill();
						
					}
					catch(Exception e){
						//renvoie une exception si l'execution est coup�e pendant un scan
						e.printStackTrace();
					}
				
				}
			}).start();

 */


public class Input {

	static Input in = null ;
	private static Scanner scanner ;

	public static Input Initialize(){
		if (in == null){
			in = new Input();
		}
		return in ;
	}

	public static void Kill(){
		scanner.close();
	}


	private Input(){
		scanner = new Scanner(System.in);
	}


	//Tout est faux sauf true comme l'indique parseBoolean
	public static boolean scanBoolean() {
		//fait un appel implicite a system.in.read();

		String t  = "true" ;
		scanner.findInLine("(\\w+)");
		MatchResult result = scanner.match();

		for (int i=1; i<=result.groupCount(); i++){
			if (t.compareToIgnoreCase(result.group(i)) == 0){
				//supprime ce qui reste dans le buffer de scanner
				scanner.nextLine();
				return Boolean.parseBoolean(scanner.match().group(i));
			}
		}
		
		//supprime ce qui reste dans le buffer de scanner
		scanner.nextLine();
		return false;
	}


	public static int scanInt() {
		//fait un appel implicite a system.in.read();
		scanner.findInLine("(\\d+)");
		int res =  Integer.parseInt(scanner.match().group(0));
		//supprime ce qui reste dans le buffer de scanner
		scanner.nextLine();
		return res ;
	}

	public static double scanDouble() {
		//fait un appel implicite a system.in.read();
		scanner.findInLine("(\\d+(.\\d*)?)");
		double res =  Double.parseDouble(scanner.match().group(0));
		//supprime ce qui reste dans le buffer de scanner
		scanner.nextLine();
		return res ;
	}

	public static Float scanFloat() {
		//fait un appel implicite a system.in.read();
		scanner.findInLine("(\\d+(.\\d*)?)");
		Float res =  Float.parseFloat(scanner.match().group(0));
		//supprime ce qui reste dans le buffer de scanner
		scanner.nextLine(); 
		return res ;
	}

	public static char scanChar() {
		return scanner.nextLine().charAt(0);
	}

	public static String scanString() {
		return scanner.nextLine();
	}

}



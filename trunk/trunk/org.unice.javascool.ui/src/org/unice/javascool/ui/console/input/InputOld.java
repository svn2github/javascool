package org.unice.javascool.ui.console.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/*
 * Singleton
 */

/*
 * Pour tester 
 * 
 * new Thread(new Runnable(){
		public void run() {
			try{
				int b = -1 ;
				Input in = Input.getInput();
				System.out.println("Quel est ton age ?");
				b = Input.scanInt();
				System.out.println("Age : "+b);
				
				System.out.println("===================");
				String s ;
				System.out.println("Pr�nom ?");
				s = Input.scanString();
				System.out.println("prenom "+s);
				
				System.out.println("===================");
				float f ;
				System.out.println("1/2 = ?");
				f = Input.scanFloat();
				System.out.println("resultat "+f);
				
				System.out.println("===================");
				double d ;
				System.out.println("65000 + 65000 = ?");
				d = Input.scanDouble();
				System.out.println("resultat "+d);
				
				System.out.println("===================");
				char c;
				System.out.println("premire lettre de ton nom ?");
				c = Input.scanChar();
				System.out.println("resultat :"+c);
				
			}
			catch(Exception e){

			}
		}
	}).start();
	
	
 */


public class InputOld {

	static InputOld in = null ;
	
	public static InputOld getInput(){
		if (in == null){
			in = new InputOld();
			System.out.println("ok");
		}
		return in ;
	}
	
	public static void killInput(){
		in = null ;
	}
	
	
	//variables necessaires
	private static BufferedReader br ;

	private InputOld(){
		br = new BufferedReader( new InputStreamReader( System.in ) );
	}

	public static int scanInt() throws NumberFormatException, IOException{
		return  Integer.parseInt(br.readLine());
	}

	public static double scanDouble() throws NumberFormatException, IOException{
		return Double.parseDouble(br.readLine());
	}

	public static Float scanFloat() throws NumberFormatException, IOException{
		return Float.parseFloat(br.readLine());
	}

	public static char scanChar() throws IOException{
		return br.readLine().charAt(0);
	}

	public static String scanString() throws IOException{
		return br.readLine();
	}

}



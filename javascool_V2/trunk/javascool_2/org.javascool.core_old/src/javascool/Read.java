package javascool;


public class Read {

	public static String readString(){
		String res=null;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			res = scanner.nextLine();
		}catch(Exception e){
			System.err.println("erreur de lecture de string");
		}
		return res;
	}

	public static char readChar(){
		char res = (char)0;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			res = scanner.nextLine().charAt(0);
		}catch(Exception e){
			System.err.println("erreur de lecture de char");

		}
		return res;
	}



	public static int readInt(){

		int res = 0;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			scanner.findInLine("(\\d+)");
			res =  Integer.parseInt(scanner.match().group(0));
			scanner.nextLine();
		}catch(Exception e){
			System.err.println("erreur de lecture de l'entier");
		}
		return res ;
	}


	public static float readFloat(){
		float res = 0;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			scanner.findInLine("(\\d+(.\\d*)?)");
			res =  Float.parseFloat(scanner.match().group(0));
			scanner.nextLine(); 
		}catch(Exception e){
			System.err.println("erreur de lecture du flottant");
		}
		return res ;
	}


	public static double readDouble(){
		double res = 0;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			scanner.findInLine("(\\d+(.\\d*)?)");
			res =  Double.parseDouble(scanner.match().group(0));
			scanner.nextLine();
		}catch(Exception e){
			System.err.println("erreur de lecture du double");
		}
		return res ;
	}


	public static boolean readBoolean(){
		boolean res=false;
		try{
			java.util.Scanner scanner = new java.util.Scanner(System.in);
			String t = scanner.nextLine();
			if(t.equals("true"))
				return true;
			return false;
		}catch(Exception e){
				System.err.println("erreur de lecture de booleen");
			}
			return res;
		}

	}

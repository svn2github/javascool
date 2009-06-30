package compiler;

public class Ctest2 {
	 public static void test (String args[]) {
		
		 Konsol.echo("Bonjour, qui est tu ?");
		    String nom = Konsol.readString();
		    Konsol.echo ("Echanté "+nom+" ! Quel age as tu ?");
		     int age = Konsol.readInteger();
		     for(int n = 0; n < 100; n++)
		    	 Konsol.echo("He je suis plus vieux que toi !!");
		 System.out.println("salut");
	 }
}

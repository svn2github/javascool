package compiler;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Classe0 implements InterfaceClasse {
	static{
		String prenom= null;
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("donner un Nom");
		try {
		prenom = clavier.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		System.out.println("hello  "+prenom );
	   }
	public String nom() { return "   objet de Classe0";}
	}

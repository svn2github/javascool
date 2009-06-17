import java.rmi.Naming;
import java.rmi.RemoteException;

import org.unice.javascool.orphyRMI.IOrphy;
import org.unice.javascool.orphyRMI.OrphyRegister;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.actions.OrphyAction;

public class Orphy {	

	private static IOrphy orphy;
	
	/**
	 * Retourne la valeur de l'entr&eacute;e pass&eacute;e en param&egrave;tre en fonction du type de capteur fournit. Les
	 * differents types de capteurs possibles sont ajout&eacute; dans OrphyAction par la classe Capteur.
	 *
	 * @param entree l'entr&eacute;e d'orphy sur laquelle on va faire l'acquisition
	 * @param typeCapteur le type de capteur branch&eacute; sur l'entr&eacute;e
	 * @return            la valeur de l'entr&eacute;e en fonction du type de capteur
	 */
	public static double acquisitionEntree(String entree, String typeCapteur){
		
		try {
			if(entree.compareTo("G")==0){
				if(orphy.isReading())
					Thread.yield();
				return orphy.getAnalogInput(10, typeCapteur);
			}
			else if(entree.compareTo("H")==0){
				if(orphy.isReading())
					Thread.yield();
				return orphy.getAnalogInput(11, typeCapteur);
			}
			else return 0.0;
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;
	}
	
	/**
	 * Fonction permettant de savoir si Orphy est utilis&eacute; actuellement par l'interface graphique
	 *
	 * @return true si l'interface graphique est lanc&eacute;, false sinon.
	 */
	public static boolean isUIUsed(){
		try {
			return orphy.UIUsed();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Fonction permettant de savoir si Orphy est utilis&eacute; actuellement par du code javascool
	 *
	 * @return true si du code javascool utilise orphy, false sinon.
	 */
	public static boolean isCodeUsed(){
		try {
			return orphy.CodeUsed();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Code de la maccro "demarrer" destin&eacute;e &agrave; etre utilis&eacute;e dans du code javascool pour initialiser Orphy
	 *
	 */
	public static void demarrer(){
		final String string = JavaScoolServerRegister.getProtocol() + OrphyRegister.getServer();
		try {
			orphy = (IOrphy)Naming.lookup(string);
		}catch(Exception e){
			System.out.println("Activez Orphy en lancant l'interace graphique Orphy \nau moin une fois s'il vous plait.");
		}
		try {
			if(!orphy.UIUsed() && !orphy.CodeUsed()){
				String port = orphy.findPort("Code");
				if(port.compareTo("") != 0){
					if(orphy.openPort(port,"Code") == -1){
						System.out.println("impossible de demarrer Orphy.");
						return;
					}
				}
				else
					System.out.println("Aucun Orphy n'est actuellement branche.");
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Code de la maccro "arreter" destin&eacute;e &agrave; etre utilis&eacute;e dans du code javascool pour arreter Orphy proprement
	 *
	 */
	public static void arreter(){
		try {
			if(!orphy.UIUsed()){
				try {
					orphy.close("Code");
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
}

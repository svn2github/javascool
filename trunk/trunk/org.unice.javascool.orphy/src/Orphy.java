import java.rmi.Naming;
import java.rmi.RemoteException;

import org.unice.javascool.orphyRMI.IOrphy;
import org.unice.javascool.orphyRMI.OrphyRegister;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.actions.OrphyAction;

public class Orphy {	

	private static IOrphy orphy;
	
	
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
	
	public static boolean isUIUsed(){
		try {
			return orphy.UIUsed();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean isCodeUsed(){
		try {
			return orphy.CodeUsed();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

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

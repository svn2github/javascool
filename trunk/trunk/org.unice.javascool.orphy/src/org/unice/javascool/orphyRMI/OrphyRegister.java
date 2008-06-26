package org.unice.javascool.orphyRMI;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

import org.unice.javascool.orphy.Orphy;
import org.unice.javascool.util.rmi.JavaScoolServerRegister;
import org.unice.javascool.util.rmi.UtilServer;

public class OrphyRegister {
	private static Process myProcess;



	public static String getServer(){
		return "OrphyServer";
	}


	public static void main(String[] args){
		//Runtime.getRuntime().addShutdownHook(new Thread(myRunnable));
		System.setSecurityManager(new RMISecurityManager());
		Orphy orphy;
		try {
			orphy = new Orphy();
			String request=JavaScoolServerRegister.getProtocol()+getServer();
			Naming.rebind(request,orphy);
			return;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public static void attachOrphyServer(){
		try{
			String dirRootString=UtilServer.getPluginFolder(org.unice.javascool.editor.Activator.PLUGIN_ID);
			String dirString=dirRootString+"ServerSide";
			File dir=new File(dirString);
			//System.out.println("file "+dir.toURI().toASCIIString());
			String[] cmd=new String[]{"java","-Djava.rmi.server.codebase="+dir.toURI().toASCIIString(),"-Djava.security.policy="+dirRootString+".java.policy","org.unice.javascool.orphyRMI.OrphyRegister"};
			ProcessBuilder pb=new ProcessBuilder(cmd);
			pb.directory(dir);
			pb.environment().put("CLASSPATH",".");
			pb.redirectErrorStream(true);
			myProcess=pb.start();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}


	public static Process getMyProcess() {
		return myProcess;
	}
}
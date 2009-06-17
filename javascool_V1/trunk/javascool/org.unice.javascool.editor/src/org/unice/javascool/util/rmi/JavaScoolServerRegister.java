package org.unice.javascool.util.rmi;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

public class JavaScoolServerRegister {

	private static String server=null;
	final static Runnable myRunnable=new Runnable(){
		@Override public void run(){
			try {
				Naming.unbind(getProtocol()+getServer());
			} catch (IOException e) {
			} catch (NotBoundException e) {
			}
		}
	};

	public static String getPort(){
		return "1099";
	}

	public static String getProtocol(){
		return "rmi://127.0.0.1:"+getPort()+"/";
	}

	public static String getServer(){
		return "JavaScoolServer";
	}


	public static void main(String[] args){
		//Runtime.getRuntime().addShutdownHook(new Thread(myRunnable));
		System.setSecurityManager(new RMISecurityManager());
		JavaScoolServer server;
		try {
			server = new JavaScoolServer();
			String request=getProtocol()+getServer();
			Naming.rebind(request,server);
			return;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return;
	}
}

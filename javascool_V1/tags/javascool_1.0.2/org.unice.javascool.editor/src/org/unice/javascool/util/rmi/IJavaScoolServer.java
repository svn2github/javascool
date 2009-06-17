package org.unice.javascool.util.rmi;

import java.rmi.Remote;
import java.util.HashMap;

public interface IJavaScoolServer extends Remote {
	
	boolean isRunning() throws java.rmi.RemoteException;
	
	void run(String cp,String pg,String[] args) throws java.rmi.RemoteException;
	
	void stop() throws java.rmi.RemoteException;
	
	HashMap<String,Object> getFields(String cp,String pg) throws java.rmi.RemoteException; 

}

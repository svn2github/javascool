package org.unice.javascool.orphyRMI;

import java.rmi.*;

public interface IOrphy extends Remote {

	public boolean UIUsed() throws RemoteException;
	
	public boolean CodeUsed() throws RemoteException;
	
	public boolean isReading() throws RemoteException;
	
	public void resetSerialPort() throws RemoteException;
	
	public int openPort(String port, String from) throws RemoteException;
	
	public String findPort(String from) throws RemoteException;
	
	public double[] getProgramedInput(String type, int nombreAcqu, int interval, int analogInput) throws RemoteException;
	
	public boolean isAnalogInputEnabled(int analogInput) throws RemoteException;
	
	public double getAnalogInput(int analogInput, String type) throws RemoteException;	
	
	public int reset() throws RemoteException;
	
	public void close(String from) throws RemoteException;
}

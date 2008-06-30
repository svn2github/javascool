package org.unice.javascool.orphy;

import gnu.io.*;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

import org.unice.javascool.orphyRMI.IOrphy;

public class Orphy extends UnicastRemoteObject implements IOrphy{


	private boolean UIUtilisation, CodeUtilisation = false;

	private boolean READING;
	private BufferedReader bufRead; 
	private BufferedWriter bufWrite;
	private InputStream inStream;	//port's read stream
	private OutputStream outStream;	//port's write stream
	private CommPortIdentifier portId;	//port's id
	private SerialPort sPort;	//serial port
	private String port;

	private static Orphy orphy;

	
	/**
	 * Constructeur
	 * 
	 * @throws RemoteException
	 */
	public Orphy() throws RemoteException{
		READING = false;
		port = "";
	}

	public SerialPort getSport(){
		return sPort;
	}

	public boolean UIUsed(){
		return UIUtilisation;
	}

	public boolean CodeUsed(){
		return CodeUtilisation;
	}

	public boolean isReading(){
		return READING;
	}


	/** 
	 * Ouvre un port avec l'API rxtx pour pouvoir communiquer avec
	 *
	 * @param port le port à ouvrir
	 * @param from spécifie depuis quelle utilisation d'Orphy on ouvre un port, "code" ou "ui"
	 * return -1 si l'ouverture n'a pas reussie
	 */
	public int openPort(String port, String from){
		try {
			portId = CommPortIdentifier.getPortIdentifier(port);
		} catch (NoSuchPortException e) {
			System.out.println(Messages.getString("Orphy.0")); //$NON-NLS-1$
			return -1;
		}

		try {
			sPort = (SerialPort) portId.open(Messages.getString("Orphy.1"), 30000); //$NON-NLS-1$
		} catch (PortInUseException e) {
			System.out.println(Messages.getString("Orphy.2")); //$NON-NLS-1$
			return -1;
		}
		//regle les parametres de la connexion
		try {
			sPort.setSerialPortParams(
					9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
			System.out.println(Messages.getString("Orphy.3")); //$NON-NLS-1$
			return -1;
		}
		//r√©cup√©ration du flux de lecture et ecriture du port
		try {

			outStream = sPort.getOutputStream();
			inStream = sPort.getInputStream();
			bufRead =
				new BufferedReader(
						new InputStreamReader(sPort.getInputStream()));
			bufWrite =  new BufferedWriter(new OutputStreamWriter(sPort.getOutputStream())); 
		} catch (IOException e) {
			System.out.println(Messages.getString("Orphy.4")); //$NON-NLS-1$
			return -1;
		}
/*
		try {
			sPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_OUT);
			sPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN);
		} catch (UnsupportedCommOperationException e) {
			e.printStackTrace();
		}*/

		if(from.compareTo("UI") == 0)
			UIUtilisation = true;
		else
			CodeUtilisation = true;

		return 0;
	}
	
	/** 
	 * Methode programmant une acquisition de précision avec le systeme de programmation interne d'orphy,
	 *	elle est utilisé quand l'intervalle d'acquisition descende en dessous de 0.01 seconde
	 *
	 * @param type le type de sonde dont on veut faire l'acquisition
	 * @param nombreAcqu le nombre d'acquisition à faire
	 * @param interval l'intervalle entre chaque acquisition
	 * @param analogInput l'entrée d'orphy sur laquelle on veut faire l'acquisition
	 * return le tableau des acquisitions effectuées
	 */
	public synchronized double[] getProgramedInput(String type, int nombreAcqu, int interval, int analogInput){
		double[] result = new double[nombreAcqu];
		int i = 0;
		double coef = 1;

		if(type.compareTo("Thermom\u00e8tre")==0)
			coef = 11;
		else if(type.compareTo("Capteur de pression")==0)
			coef = 2;
		else if(type.compareTo("Conductim\u00e8tre")==0)
			coef = 3.0/5.0;

		try {
		//	bufWrite.write("WPE 1 200 10 5000 10");
			bufWrite.write("WPE"+ " " + 1 + " " + nombreAcqu + " 10 " + interval*100+ " " + analogInput);
			bufWrite.newLine();
			bufWrite.flush();
			bufWrite.write("WGOR");
			bufWrite.newLine();
			bufWrite.flush();
				try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String res = bufRead.readLine();
			res = res.concat(",");
			int indexStart = 0;
			int indexEnd = res.indexOf(",");
			while(indexEnd != -1){
				double tmp = Integer.parseInt(res.substring(indexStart, indexEnd))/65535.0*20.0*coef;
				//	System.out.println(tmp);
				indexStart = indexEnd + 1;
				indexEnd = res.indexOf(",", indexStart + 1);
				result[i++] = tmp;
			}
		} catch (IOException e) {
			//if it fails... retry ^^(i guess some bugs of orphy, or some bug of me :s)
			getProgramedInput(type, nombreAcqu, interval, analogInput);
		}
		return result;
	}

	/** Methode permettant de savoir si une entrée est utilisée
	 *
	 * @param analogInput l'entrée à vérifier
	 * return true si l'entrée est utilisée, false sinon
	 */
	public boolean isAnalogInputEnabled(int analogInput){

		int sint;
		try{
			outStream.write(64 + analogInput);
			sint = inStream.read();
			sint = inStream.read();
		} catch(IOException e){
			return false;
		}
		try {
			outStream.flush();
		} catch (IOException e) {
			return false;
		}
		if(sint == 255 || sint == 0 || sint ==-1){
			return false;
		}
		return true;
	}

	/** Methode retournant la valeur d'un port orphy en fonction de son type de capteur.
	 *
	 * @param analogInput l'entrée à vérifier
	 * @param type le type du capteur dont on veut la valeur
	 * return la valeur de l'acquisition
	 */
	public synchronized double getAnalogInput(int analogInput, String type){
		int pint;
		int sint = 0;
		double res = 0;
		try{
			/*
			bufWrite.write("WASC");
			bufWrite.newLine();
			bufWrite.flush();
			bufWrite.write("WEA " + analogInput);
			bufWrite.newLine();
			bufWrite.flush();
			*/
			outStream.write(64 + analogInput);
			pint = inStream.read();
			sint = inStream.read();
		//	System.out.println(pint + " et " + sint);
		//	pint = bufRead.readLine().trim();
			
		} catch(IOException e){
			return -10;
		}
		try {
			outStream.flush();
		} catch (IOException e) {
			return -15;
		}
		 
		/*
		pint = (int)pres;
		sint = (int)sres;
		
		if(pint.compareTo("")!=0)
			res = Integer.parseInt();
		*/
		if(sint > 127)
			sint = -(128 + (128 - sint));
		
		res =( pint + sint*256.0 )/65535.0 * 20.0;

		if(type.compareTo("Thermom\u00e8tre")==0)
			res = res * 11;
		else if(type.compareTo("Capteur de pression")==0)
			res = res * 2;
		else if(type.compareTo("Voltm\u00e8tre")==0)
			res = res + 0;
		else if(type.compareTo("Conductim\u00e8tre")==0)
			res = res * (3.0/5.0);




		return res;
	}

	/** Methode permettant la réinitialisation du port série, son utilisation est du à un bug inexpliqué concernant l'acquisition en continu
	 *
	 */
	public void resetSerialPort(){
		sPort.close();
		try {
			bufRead.close();
			outStream.close();
			bufWrite.close();
			inStream.close();
		} catch (IOException e) {
		}
		try {
			sPort = (SerialPort) portId.open(Messages.getString("Orphy.1"), 30000); //$NON-NLS-1$
		} catch (PortInUseException e) {

		}
		try {
			sPort.setSerialPortParams(
					9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {

		}
		try {

			outStream = sPort.getOutputStream();
			inStream = sPort.getInputStream();
			bufRead =
				new BufferedReader(
						new InputStreamReader(sPort.getInputStream()));
			bufWrite =  new BufferedWriter(new OutputStreamWriter(sPort.getOutputStream())); 
		} catch (IOException e) {
		}

	}

	/** 
	 * Lit toute les entrées
	 */
	public void lireAllAnalogiques(){

		try{
			for(int i = 0; i < 12 ; i++){
				outStream.write(64 + i);
				System.out.println(inStream.read() + Messages.getString("Orphy.7") + inStream.read()); //$NON-NLS-1$
			}
		} catch(IOException e){
			System.out.println(Messages.getString("Orphy.8")); //$NON-NLS-1$
		}

	}

	/**
	 *	Demande à Orphy sa version
	 * return la version d'orphy
	 */
	public String askVersion(){
		String res = null;

		try{
			bufWrite.write("WVERSION");
			bufWrite.newLine();
			bufWrite.flush();

			res = bufRead.readLine();
		} catch(IOException e){
			return "";
		}

		return res;
	}

	/**
	 * Reset Orphy
	 * return -1 si le reset a réussi
	 */
	public int reset(){

		try{
			bufWrite.write("WRESET");
			bufWrite.newLine();
			bufWrite.flush();
		}
		catch(IOException e){
			return -1;
		}

		return 1;
	}

	/** Trouve le port série sur lequel un orphy est branché
	 *
	 * @param from précise d'où est appelé la fonction "Code" ou "ui"
	 *
	 * return le nom du port où est orphy, "" si il n'y a pas d'orphy branché
	 */
	public String findPort(String from) throws RemoteException{

		String res = null;

		//getting of the list of ports
		Enumeration<CommPortIdentifier> portList=CommPortIdentifier.getPortIdentifiers();
		//getting the names
		CommPortIdentifier idComPort;

		while (portList.hasMoreElements()){

			idComPort=(CommPortIdentifier)portList.nextElement();

			if(idComPort.getName().substring(0, 3).compareTo("COM") == 0){
				if(openPort(idComPort.getName(), from) != -1){
					// if reset works, then this is an ORPHY
					if(reset() == 1){
						res = idComPort.getName();
						close(from);
						port = res;
						return res;
					}
					// version of test with askVersion, supposed more logical and more usable if different versions of Orphy, but not stable, so aborted
					/*					
					if(askVersion().compareTo("")!=0){
						res = idComPort.getName();
						close();
						return res;
					}
					 */
					close(from);
				}

			}

		}

		return "";

	}

	/** Ferme les stream et le port série
	 *
	 * @param from précise d'où est appelé la fonction "Code" ou "ui"
	 *
	 */
	public void close(String from) throws RemoteException{
		try {
			bufRead.close();
			outStream.close();
			bufWrite.close();
			inStream.close();
		} catch (IOException e) {
		}
		sPort.close();

		if(from.compareTo("UI") == 0)
			UIUtilisation = false;
		else if(from.compareTo("Code") == 0)
			CodeUtilisation = false;
		else
			UIUtilisation = CodeUtilisation = false;

		//System.exit(0);
	}

	/** Récupère des informations concernant la derniere fonction d'orphy utilisée
	 *
	 * return le retour d'erreur
	 */
	public String getError(){
		String res = null;

		try{
			bufWrite.write("WERR");
			bufWrite.newLine();
			bufWrite.flush();

			res = bufRead.readLine();
		} catch(IOException e){
			return "";
		}

		return res;
	}

	/** Main de test "off-javascool"
	 *
	 *
	 */
	public static void main(String[] args){
		try {
			orphy = new Orphy();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		try {
			orphy.openPort(orphy.findPort("Code"),"Code");
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

		double[] res = new double[150];
		boolean values = true;
		Thread getValues = new Thread(new Runnable() {
			public void run() {
				int n = 0;
				while (true) {
					System.out.println("n : " + n++);
					/*
					if(n == 25){
						double[] res = orphy.getProgramedInput("Thermometre", 150, 5, 10);
						for(int i = 0; i<res.length; i++){
							System.out.println(res[i]);
						}
					}
					*/
					System.out.println("temperature :  " + orphy.getAnalogInput(10, "Thermom\u00e8tre"));
					orphy.resetSerialPort();
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		getValues.start();

		Thread prog = new Thread(new Runnable() {
			public void run() {
				int n = 0;
				while (true) {
					System.out.println("n : " + n++);
					if(n == 25){
						double[] res = orphy.getProgramedInput("Thermom\u00e8tre", 150, 5, 11);
						for(int i = 0; i<res.length; i++){
							System.out.println(res[i]);
						}
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		//prog.start();

		try {
			orphy.close("Code");System.out.println("coucou");

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}



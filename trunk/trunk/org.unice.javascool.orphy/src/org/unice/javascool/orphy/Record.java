package org.unice.javascool.orphy;

import java.util.ArrayList;
import java.io.*;

public class Record {
	private ArrayList<Double> temps;
	private ArrayList<Double> value;
	private String destination;
	
	public Record(){
		temps = new ArrayList<Double>();
		value = new ArrayList<Double>();
		destination = Messages.getString("Record.0"); //$NON-NLS-1$
	}
	
	public void add(double sec, double val){
			temps.add(sec);
			value.add(val);
	}
	
	public void save(String type){
		try {
			
			String abreviation = null;
			String unite = null;
			if(type.compareTo(Messages.getString("Record.1")) == 0){ //$NON-NLS-1$
				abreviation = Messages.getString("Record.2"); //$NON-NLS-1$
				unite = Messages.getString("Record.3"); //$NON-NLS-1$
			}
			else if (type.compareTo(Messages.getString("Record.4")) == 0){ //$NON-NLS-1$
				abreviation = Messages.getString("Record.5"); //$NON-NLS-1$
				unite = Messages.getString("Record.6"); //$NON-NLS-1$
			}
			else if (type.compareTo("Capteur de pression") == 0){
				abreviation = "P";
				unite = "hPa";
			}
			BufferedWriter fichier = new BufferedWriter( new FileWriter(destination));
			
			fichier.write(Messages.getString("Record.7") + type); //$NON-NLS-1$
			fichier.newLine();
			fichier.write(Messages.getString("Record.8") + abreviation); //$NON-NLS-1$
			fichier.newLine();
			fichier.write(Messages.getString("Record.9") + unite); //$NON-NLS-1$
			fichier.newLine();
			for(int i = 0; i < getSizeTemps() ; i++){
				fichier.write(temps.get(i)+Messages.getString("Record.10")+value.get(i)); //$NON-NLS-1$
				fichier.newLine();
			}
			fichier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(Messages.getString("Record.11")); //$NON-NLS-1$
			e.printStackTrace();
		}
	}
	
	public void setDestination(String newdest){
		destination = newdest;
	}
	
	public double getTemps(int i){
		return temps.get(i);
	}
	
	public double getValue(int i){
		return value.get(i);
	}
	
	public int getSizeTemps(){
		return temps.size();
	}
	
	public int getSizeValue(){
		return value.size();
	}
	
	public void reset(){
		temps.clear();
		value.clear();
	}
	
}

package org.javascool.util.bml;

@SuppressWarnings("serial")
public class BmlConverterException extends Exception {
	
	BmlConverterException(String f,int n,String ob,String ex){
		super("Erreur de strucutre sur le fichier "+f+" a la ligne "+n+"\n"+
				"Attendu: "+ob+" Obtenu: "+ex+"\n");
	}

}

/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.util.bml;

@SuppressWarnings("serial")
public class BmlConverterException extends Exception {
	
	BmlConverterException(String f,int n,String ob,String ex){
		super("Erreur de strucutre sur le fichier "+f+" a la ligne "+n+"\n"+
				"Attendu: "+ob+" Obtenu: "+ex+"\n");
	}

}

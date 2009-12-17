/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.editors;

import java.util.ArrayList;

import org.eclipse.swt.graphics.RGB;

public interface JVSColorConstants {
	
	//couleur des commentaires
	RGB JAVA_COMMENT = new RGB(51, 51, 255);
	//couleur des mots cles
	RGB PROC_INSTR = new RGB(153, 0, 0);
	//couleur des chaine de caracteres
	RGB STRING = new RGB(0, 128, 0);
	//couleur par defaut
	RGB DEFAULT = new RGB(0, 0, 0);
	//couleur des macros
	RGB MACRO = new RGB(255, 128, 0);
	//couleur des types
	RGB TYPE = new RGB(153, 0, 0);
	//couleur des nombres
	RGB NUMBER = new RGB(255, 51, 204);
	
	ArrayList<String> listPorts = new ArrayList<String>();
	String portName = "COM3";
	 
}

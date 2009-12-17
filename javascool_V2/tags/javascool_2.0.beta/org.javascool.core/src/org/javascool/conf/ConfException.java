/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.conf;

@SuppressWarnings("serial")
public class ConfException extends Exception {
	
	public ConfException(){
		super();
	}
	
	public ConfException(String mess){
		super(mess);
	}
}

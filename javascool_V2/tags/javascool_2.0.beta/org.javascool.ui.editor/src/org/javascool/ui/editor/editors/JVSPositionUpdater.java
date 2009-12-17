/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.editors;

import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.IPositionUpdater;

public class JVSPositionUpdater extends DefaultPositionUpdater {

	public static boolean singleton = false ;
	public static JVSPositionUpdater t ;

	public JVSPositionUpdater(String category) {
			super(category);
		// TODO Auto-generated constructor stub
	}

	public void setPosition(int offset){
		fOffset = offset ;
		fReplaceLength = 1 ;
		adaptToInsert();
	}

	
	public static IPositionUpdater getJVSPositionUpdater(String category){
		if (!singleton)
			t =  new JVSPositionUpdater(category);
		singleton = true ;
		return t ;
	}
	
}

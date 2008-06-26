package org.unice.javascool.editor.editors;

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

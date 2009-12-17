/*
 * Copyright (c) 2008-2010 Javascool (Java's Cool).  All rights reserved.
 *	this source file is placed under license CeCILL
 * see http://www.cecill.info/licences/Licence_CeCILL_V2-fr.html
 * or http://www.cecill.info/licences/Licence_CeCILL_V2-en.html
 */
package org.javascool.ui.editor.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class NewEditorAction implements IWorkbenchWindowActionDelegate {

	public static final String ID = "org.javascool.ui.editor.actions.newEditorAction";
	
	
	public static int nbFile = 0 ;

	@Override
	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(IAction action) {
		try {
			NewAction.action();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
	
	
	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

}
